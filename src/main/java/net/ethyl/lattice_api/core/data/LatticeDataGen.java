package net.ethyl.lattice_api.core.data;

import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.core.utils.utility.ErrUtils;
import net.ethyl.lattice_api.mod.registries.LatticeTypes;
import net.ethyl.lattice_api.modules.base.*;
import net.ethyl.lattice_api.modules.common.RecipeTypes.*;
import net.ethyl.lattice_api.modules.common.blocks.*;
import net.ethyl.lattice_api.modules.common.items.equipment.tools.LatticeBasicAxe;
import net.ethyl.lattice_api.modules.common.items.equipment.tools.LatticeBasicHoe;
import net.ethyl.lattice_api.modules.common.items.equipment.tools.LatticeBasicPickaxe;
import net.ethyl.lattice_api.modules.common.items.equipment.tools.LatticeBasicShovel;
import net.ethyl.lattice_api.modules.common.items.equipment.weapons.LatticeBasicSword;
import net.ethyl.lattice_api.modules.common.tags.LatticeBlockTag;
import net.ethyl.lattice_api.modules.common.tags.LatticeItemTag;
import net.ethyl.lattice_api.modules.common.types.lootTypes.LatticeToolType;
import net.ethyl.lattice_api.modules.common.types.modelTypes.LatticeBlockModelType;
import net.ethyl.lattice_api.modules.common.types.modelTypes.LatticeItemModelType;
import net.ethyl.lattice_api.modules.common.types.lootTypes.LatticeLootTable;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.*;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LatticeDataGen {
    public static void onGatherDataEvent(GatherDataEvent event, @NotNull String modId, @NotNull Set<String> compatibleModsIds) {
        DataGenerator dataGenerator = event.getGenerator();
        PackOutput packOutput = dataGenerator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        HashSet<String> allModIds = new HashSet<>(compatibleModsIds);
        allModIds.add("minecraft");

        BlockTagsProvider blockTagsProvider = new BlockTagGenerator(packOutput, lookupProvider, modId, existingFileHelper);
        dataGenerator.addProvider(event.includeServer(), blockTagsProvider);
        dataGenerator.addProvider(event.includeServer(), new ItemTagGenerator(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), modId, existingFileHelper));

        dataGenerator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(ignored -> new BlockLootTableProvider(lookupProvider.join(), modId), LootContextParamSets.BLOCK)), lookupProvider));
        dataGenerator.addProvider(event.includeServer(), new RecipeGenerator(packOutput, lookupProvider, modId));

        CompletableFuture<HolderLookup.Provider> registryProvider = dataGenerator.addProvider(event.includeServer(), new DatapackGenerator(packOutput, lookupProvider, modId, allModIds)).getRegistryProvider();

        dataGenerator.addProvider(event.includeClient(), new ItemModelGenerator(packOutput, modId, allModIds, existingFileHelper, lookupProvider, registryProvider));
        dataGenerator.addProvider(event.includeClient(), new BlockModelGenerator(packOutput, modId, existingFileHelper));

        dataGenerator.addProvider(event.includeClient(), new AtlasGenerator(packOutput, lookupProvider, registryProvider, modId, allModIds, existingFileHelper));
    }

    public static class BlockTagGenerator extends BlockTagsProvider {
        public BlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, modId, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider provider) {
            for (LatticeTag<?> latticeTag : LatticeRegistries.getTags()) {
                if (!isNotFromMod(latticeTag, this.modId, Set.of())) {
                    if (latticeTag instanceof LatticeBlockTag latticeBlockTag) {
                        IntrinsicTagAppender<Block> tagAppender = tag(latticeBlockTag.get());
                        latticeBlockTag.getKeyTagContent().forEach(tagKeySupplier -> tagAppender.addTag(tagKeySupplier.get()));
                        latticeBlockTag.getTagContent().forEach(blockSupplier -> tagAppender.add(blockSupplier.get()));
                    }
                }
            }

            for (LatticeBlock<?> latticeBlock : LatticeRegistries.getBlocks()) {
                Block block = latticeBlock.get();

                if (latticeBlock instanceof LatticeWallBlock) {
                    this.tag(BlockTags.WALLS).add(block);
                }

                LatticeToolType toolType = latticeBlock.getToolType();
                TagKey<Block> toolTag = null;

                if (toolType == LatticeTypes.ToolType.PICKAXE) {
                    toolTag = BlockTags.MINEABLE_WITH_PICKAXE;
                } else if (toolType == LatticeTypes.ToolType.AXE) {
                    toolTag = BlockTags.MINEABLE_WITH_AXE;
                } else if (toolType == LatticeTypes.ToolType.SHOVEL) {
                    toolTag = BlockTags.MINEABLE_WITH_SHOVEL;
                } else if (toolType == LatticeTypes.ToolType.HOE) {
                    toolTag = BlockTags.MINEABLE_WITH_HOE;
                }

                if (toolTag != null) {
                    this.tag(toolTag).add(block);
                }
            }
        }
    }

    public static class ItemTagGenerator extends ItemTagsProvider {
        public ItemTagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(packOutput, lookupProvider, blockTags, modId, existingFileHelper);
        }

        @Override
        protected void addTags(@NotNull HolderLookup.Provider provider) {
            for (LatticeTag<?> latticeTag : LatticeRegistries.getTags()) {
                if (!isNotFromMod(latticeTag, this.modId, Set.of())) {
                    if (latticeTag instanceof LatticeItemTag latticeItemTag) {
                        IntrinsicTagAppender<Item> tag = this.tag(latticeItemTag.get());
                        latticeItemTag.getKeyTagContent().forEach(tagKeySupplier -> tag.addTag(tagKeySupplier.get()));
                        latticeItemTag.getTagContent().forEach(itemSupplier -> tag.add(itemSupplier.get()));
                    }
                }
            }

            for (LatticeItem<?> latticeItem : LatticeRegistries.getItems()) {
                TagKey<Item> itemTag;

                if (latticeItem.get() instanceof ArmorItem) {
                    itemTag = ItemTags.TRIMMABLE_ARMOR;
                } else {
                    itemTag = switch (latticeItem) {
                        case LatticeBasicSword ignored -> ItemTags.SWORDS;
                        case LatticeBasicPickaxe ignored -> ItemTags.PICKAXES;
                        case LatticeBasicAxe ignored -> ItemTags.AXES;
                        case LatticeBasicShovel ignored -> ItemTags.SHOVELS;
                        case LatticeBasicHoe ignored -> ItemTags.HOES;
                        default -> null;
                    };
                }

                if (itemTag != null) {
                    this.tag(itemTag).add(latticeItem.get());
                }
            }

            for (LatticeTrimMaterial latticeTrimMaterial : LatticeRegistries.getTrimMaterials()) {
                if (!isNotFromMod(latticeTrimMaterial, this.modId, Set.of())) {
                    this.tag(ItemTags.TRIM_MATERIALS).add(latticeTrimMaterial.getTrimIngredient());
                }
            }

            for (LatticeTrimPattern latticeTrimPattern : LatticeRegistries.getTrimPatterns()) {
                if (!isNotFromMod(latticeTrimPattern, this.modId, Set.of())) {
                    this.tag(ItemTags.TRIM_TEMPLATES).add(latticeTrimPattern.getTrimItem());
                }
            }
        }
    }

    public static class BlockLootTableProvider extends BlockLootSubProvider {
        private final String modId;

        protected BlockLootTableProvider(HolderLookup.Provider registries, @NotNull String modId) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
            this.modId = modId;
        }

        @Override
        protected void generate() {
            for (LatticeBlock<?> latticeBlock : LatticeRegistries.getBlocks()) {
                if (isNotFromMod(latticeBlock, this.modId, Set.of())) {
                    continue;
                }

                LatticeLootTable lootType = latticeBlock.getLootType();
                Supplier<Item> dropItem = lootType.getDrop();
                Block block = latticeBlock.get();
                float minDrops = lootType.getMinDrops();
                float maxDrops = lootType.getMaxDrops();

                if (latticeBlock instanceof LatticeSlabBlock latticeSlabBlock) {
                    SlabBlock slabBlock = latticeSlabBlock.get();

                    if (lootType == LatticeTypes.LootTable.SELF) {
                        this.add(slabBlock, this.createSlabDrops(latticeSlabBlock, null, 1.0f, 1.0f, false));
                    } else if (lootType == LatticeTypes.LootTable.SILK_TOUCH) {
                        this.add(slabBlock, this.createSlabDrops(latticeSlabBlock, null, 1.0f, 1.0f, true));
                    } else if (lootType == LatticeTypes.LootTable.OTHER) {
                        this.add(slabBlock, this.createSlabDrops(latticeSlabBlock, dropItem, 1.0f, 1.0f, false));
                    } else if (lootType == LatticeTypes.LootTable.AMOUNT) {
                        this.add(slabBlock, this.createSlabDrops(latticeSlabBlock, dropItem, minDrops, maxDrops, false));
                    }
                } else {
                    if (lootType == LatticeTypes.LootTable.SELF) {
                        this.add(block, this.createDrops(latticeBlock, null, 1.0f, 1.0f, false));
                    } else if (lootType == LatticeTypes.LootTable.SILK_TOUCH) {
                        this.add(block, this.createDrops(latticeBlock, null, 1.0f, 1.0f, true));
                    } else if (lootType == LatticeTypes.LootTable.OTHER) {
                        this.add(block, this.createDrops(latticeBlock, dropItem, 1.0f, 1.0f, false));
                    } else if (lootType == LatticeTypes.LootTable.AMOUNT) {
                        this.add(block, this.createDrops(latticeBlock, dropItem, minDrops, maxDrops, false));
                    }
                }
            }
        }

        private LootTable.Builder createSlabDrops(@NotNull LatticeSlabBlock latticeSlabBlock, @Nullable Supplier<Item> dropItem, float minDrops, float maxDrops, boolean silkTouchRequired) {
            SlabBlock slabBlock = latticeSlabBlock.get();
            Item drop = dropItem == null ? slabBlock.asItem() : dropItem.get();

            return LootTable.lootTable()
                    .withPool(
                            silkTouchRequired ?
                                    LootPool.lootPool()
                                            .add(
                                                    this.applyExplosionDecay(
                                                            slabBlock,
                                                            LootItem.lootTableItem(slabBlock)
                                                                    .when(this.hasSilkTouch())
                                                                    .apply(
                                                                            SetItemCountFunction.setCount(ConstantValue.exactly(1.0f))
                                                                    )
                                                                    .apply(
                                                                            SetItemCountFunction.setCount(ConstantValue.exactly(2.0f))
                                                                                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slabBlock)
                                                                                            .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE))
                                                                                    )
                                                                    )
                                                    )
                                            ) :
                                    LootPool.lootPool()
                                            .add(
                                                    LootItem.lootTableItem(slabBlock)
                                                            .when(this.hasSilkTouch())
                                                            .apply(
                                                                    SetItemCountFunction.setCount(ConstantValue.exactly(1.0f))
                                                            )
                                                            .apply(
                                                                    SetItemCountFunction.setCount(ConstantValue.exactly(2.0f))
                                                                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slabBlock)
                                                                                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE))
                                                                            )
                                                            )
                                            )
                                            .add(
                                                    this.applyExplosionDecay(
                                                            slabBlock,
                                                            LootItem.lootTableItem(drop)
                                                                    .when(this.doesNotHaveSilkTouch())
                                                                    .apply(
                                                                            SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops))
                                                                    )
                                                                    .apply(
                                                                            SetItemCountFunction.setCount(UniformGenerator.between(minDrops * 2, maxDrops * 2))
                                                                                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slabBlock)
                                                                                            .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE))
                                                                                    )
                                                                    )
                                                    )
                                            )
                    );
        }

        private LootTable.Builder createDrops(@NotNull LatticeBlock<?> latticeBlock, @Nullable Supplier<Item> dropItem, float minDrops, float maxDrops, boolean silkTouchRequired) {
            Block block = latticeBlock.get();
            Item drop = dropItem == null ? block.asItem() : dropItem.get();

            return LootTable.lootTable()
                    .withPool(
                            silkTouchRequired ?
                                    LootPool.lootPool()
                                            .add(
                                                    this.applyExplosionDecay(
                                                            block,
                                                            LootItem.lootTableItem(block)
                                                                    .when(this.hasSilkTouch())
                                                                    .apply(
                                                                            SetItemCountFunction.setCount(ConstantValue.exactly(1.0f))
                                                                    )
                                                    )
                                            ) :
                                    LootPool.lootPool()
                                            .add(
                                                    LootItem.lootTableItem(block)
                                                            .when(this.hasSilkTouch())
                                                            .apply(
                                                                    SetItemCountFunction.setCount(ConstantValue.exactly(1.0f))
                                                            )
                                            )
                                            .add(
                                                    this.applyExplosionDecay(
                                                            block,
                                                            LootItem.lootTableItem(drop)
                                                                    .when(this.doesNotHaveSilkTouch())
                                                                    .apply(
                                                                            SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops))
                                                                    )
                                                    )
                                            )
                    );
        }

        @Override
        protected @NotNull Iterable<Block> getKnownBlocks() {
            return LatticeRegistries.getBlocks().stream().filter(latticeBlock -> !isNotFromMod(latticeBlock, this.modId, Set.of())).map(latticeBlock -> (Block) latticeBlock.get()).toList();
        }
    }

    public static class RecipeGenerator extends RecipeProvider implements IConditionBuilder {
        private final String modId;

        public RecipeGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String modId) {
            super(output, registries);
            this.modId = modId;
        }

        @Override
        protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
            for (LatticeTrimPattern latticeTrimPattern : LatticeRegistries.getTrimPatterns()) {
                if (!isNotFromMod(latticeTrimPattern, this.modId, Set.of())) {
                    trimSmithing(recipeOutput, latticeTrimPattern.getTrimItem(), latticeTrimPattern.getResourceKey().location());
                }
            }

            for (LatticeRecipe latticeRecipe : LatticeRegistries.getRecipes()) {
                if (!isNotFromMod(latticeRecipe, this.modId, Set.of())) {
                    RegistryId registryId = latticeRecipe.getRegistryId();
                    ResourceLocation recipeId = registryId.toResourceLoc();
                    RecipeCategory recipeCategory = latticeRecipe.getCategory();
                    List<Supplier<Item>> results = latticeRecipe.getResult();
                    int resultCount = latticeRecipe.getResultCount();
                    int resultAmount = results.size();

                    if (results.isEmpty()) {
                        incompleteObjectErr(registryId);
                    }

                    switch (latticeRecipe) {
                        case LatticeShapedRecipe latticeShapedRecipe -> {
                            Map<Character, List<Supplier<Item>>> ingredients = latticeShapedRecipe.getDefined();

                            if (resultAmount > 1 && ingredients.values().stream().noneMatch(ingredientList -> ingredientList.size() == resultAmount)) {
                                incompleteObjectErr(registryId);
                            }

                            for (int i = 0; i < resultAmount; i++) {
                                Item resultItem = this.getIndex(results, i);
                                ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(recipeCategory, resultItem, resultCount);
                                String prefix = "", suffix = "";

                                latticeShapedRecipe.getPattern().values().forEach(builder::pattern);

                                for (Map.Entry<Character, List<Supplier<Item>>> entry : ingredients.entrySet()) {
                                    List<Supplier<Item>> ingredientList = entry.getValue();

                                    if (resultAmount == 1 || ingredientList.size() != resultAmount) {
                                        builder.define(entry.getKey(), Ingredient.of(ingredientList.stream().map(itemSupplier -> new ItemStack(itemSupplier.get()))));
                                    } else if (latticeShapedRecipe instanceof LatticeShapedDyeRecipe latticeShapedDyeRecipe && latticeShapedDyeRecipe.isDyeable(entry.getKey())) {
                                        int finalI = i;
                                        builder.define(entry.getKey(), Ingredient.of(IntStream.range(0, ingredientList.size()).filter(index -> index != finalI).mapToObj(index -> new ItemStack(this.getIndex(ingredientList, index)))));
                                        prefix = "dye";
                                    } else {
                                        Item ingredient = this.getIndex(ingredientList, i);
                                        builder.define(entry.getKey(), ingredient);

                                        if (!(latticeShapedRecipe instanceof LatticeShapedDyeRecipe)) {
                                            suffix = "from_" + getKeyPath(ingredient);
                                        }
                                    }
                                }

                                saveRecipe(builder, recipeOutput, recipeId, prefix, suffix, resultItem, "shaped", resultAmount, latticeRecipe, false);
                            }
                        }
                        case LatticeShapelessRecipe latticeShapelessRecipe -> {
                            Map<Integer, List<Supplier<Item>>> ingredients = latticeShapelessRecipe.getIngredients();

                            if (resultAmount > 1 && ingredients.values().stream().noneMatch(ingredientList -> ingredientList.size() == resultAmount)) {
                                incompleteObjectErr(registryId);
                            }

                            for (int i = 0; i < resultAmount; i++) {
                                Item resultItem = this.getIndex(results, i);
                                ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(recipeCategory, resultItem, resultCount);
                                String prefix = "", suffix = "";

                                for (Map.Entry<Integer, List<Supplier<Item>>> entry : ingredients.entrySet()) {
                                    List<Supplier<Item>> ingredientList = entry.getValue();

                                    if (resultAmount == 1 || ingredientList.size() != resultAmount) {
                                        builder.requires(Ingredient.of(ingredientList.stream().map(itemSupplier -> new ItemStack(itemSupplier.get()))));
                                    } else if (latticeShapelessRecipe instanceof LatticeShapelessDyeRecipe latticeShapelessDyeRecipe && latticeShapelessDyeRecipe.isDyeable(entry.getKey())) {
                                        int finalI = i;
                                        builder.requires(Ingredient.of(IntStream.range(0, ingredientList.size()).filter(index -> index != finalI).mapToObj(index -> new ItemStack(this.getIndex(ingredientList, index)))));
                                        prefix = "dye";
                                    } else {
                                        Item ingredient = this.getIndex(ingredientList, i);
                                        builder.requires(ingredient);

                                        if (!(latticeShapelessRecipe instanceof LatticeShapelessDyeRecipe)) {
                                            suffix = "from" + getKeyPath(ingredient);
                                        }
                                    }
                                }

                                latticeShapelessRecipe.getIngredientTags().values().forEach(builder::requires);
                                saveRecipe(builder, recipeOutput, recipeId, prefix, suffix, resultItem, "shapeless", resultAmount, latticeRecipe, false);
                            }
                        }
                        case LatticeCookingRecipe latticeCookingRecipe -> {
                            Map<Integer, Map<List<Supplier<Item>>, Float>> ingredients = latticeCookingRecipe.getIngredients();

                            if (resultAmount > 1 && ingredients.size() != resultAmount || latticeCookingRecipe.getSerializers().isEmpty()) {
                                incompleteObjectErr(registryId);
                            }

                            ingredients.forEach((index, ingredientData) -> {
                                Item resultItem = this.getIndex(results, index);
                                List<Supplier<Item>> ingredientList = ingredientData.keySet().stream().toList().getFirst();
                                float xp = ingredientData.values().stream().toList().getFirst();
                                String suffix = "from_" + getKeyPath(ingredientList.getLast().get());

                                for (Map.Entry<RecipeSerializer<?>, Integer> entry : latticeCookingRecipe.getSerializers().entrySet()) {
                                    RecipeBuilder builder = null;
                                    String type = "";
                                    RecipeSerializer<?> serializer = entry.getKey();
                                    int cookTime = entry.getValue();

                                    if (serializer == RecipeSerializer.SMELTING_RECIPE) {
                                        builder = this.smeltingRecipe(ingredientList, recipeCategory, () -> resultItem, xp, cookTime);
                                        type = "smelting";
                                    } else if (serializer == RecipeSerializer.BLASTING_RECIPE) {
                                        builder = this.blastingRecipe(ingredientList, recipeCategory, () -> resultItem, xp, cookTime);
                                        type = "blasting";
                                    } else if (serializer == RecipeSerializer.SMOKING_RECIPE) {
                                        builder = this.smokingRecipe(ingredientList, recipeCategory, () -> resultItem, xp, cookTime);
                                        type = "smoking";
                                    }

                                    if (builder != null) {
                                        this.saveRecipe(builder, recipeOutput, recipeId, "", suffix, resultItem, type, resultAmount, latticeRecipe, true);
                                    }
                                }
                            });
                        }
                        default -> {}
                    }
                }
            }
        }

        private void saveRecipe(@NotNull RecipeBuilder recipeBuilder, @NotNull RecipeOutput recipeOutput, ResourceLocation id, @NotNull String prefix, @NotNull String suffix, @NotNull Item result, @NotNull String type, int resultAmount, @NotNull LatticeRecipe latticeRecipe, boolean useTypeInId) {
            recipeBuilder.unlockedBy(latticeRecipe.getUnlockId(), has(latticeRecipe.getUnlockItem())).group(latticeRecipe.getGroup());
            recipeBuilder.save(recipeOutput, resultAmount != 1 ? id.getNamespace() + ":" + (prefix.isEmpty() ? "" : prefix + "_") + getKeyPath(result) + "_" + (suffix.isEmpty() ? "" : suffix + "_") + type : id.toString() + (useTypeInId ? "_" + type : ""));
        }

        private Item getIndex(@NotNull List<Supplier<Item>> ingredientList, int index) {
            return ingredientList.get(index).get();
        }

        private SimpleCookingRecipeBuilder smeltingRecipe(Collection<Supplier<Item>> ingredients, RecipeCategory recipeCategory, Supplier<Item> resultSupplier, float experience, int cookingTime) {
            return this.CookingRecipeBuilder(ingredients, recipeCategory, resultSupplier, experience, cookingTime, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new);
        }

        private SimpleCookingRecipeBuilder blastingRecipe(Collection<Supplier<Item>> ingredients, RecipeCategory recipeCategory, Supplier<Item> resultSupplier, float experience, int cookingTime) {
            return this.CookingRecipeBuilder(ingredients, recipeCategory, resultSupplier, experience, cookingTime, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new);
        }

        private SimpleCookingRecipeBuilder smokingRecipe(Collection<Supplier<Item>> ingredients, RecipeCategory recipeCategory, Supplier<Item> resultSupplier, float experience, int cookingTime) {
            return this.CookingRecipeBuilder(ingredients, recipeCategory, resultSupplier, experience, cookingTime, RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new);
        }

        private <T extends AbstractCookingRecipe> SimpleCookingRecipeBuilder CookingRecipeBuilder(Collection<Supplier<Item>> ingredients, RecipeCategory recipeCategory, Supplier<Item> resultSupplier, float experience, int cookingTime, RecipeSerializer<T> recipeSerializer, AbstractCookingRecipe.Factory<T> factory) {
            return SimpleCookingRecipeBuilder.generic(Ingredient.of(ingredients.stream().map(Supplier::get).map(ItemStack::new)), recipeCategory, resultSupplier.get(), experience, cookingTime, recipeSerializer, factory);
        }
    }

    public static class DatapackGenerator extends DatapackBuiltinEntriesProvider {
        public DatapackGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, @NotNull String modId, @NotNull Set<String> compatibleModIds) {
            super(
                    output,
                    registries,
                    new RegistrySetBuilder()
                            .add(Registries.TRIM_MATERIAL, context -> LatticeRegistries.getTrimMaterials().stream().filter(latticeTrimMaterial -> {
                                String trimModId = latticeTrimMaterial.getModId();

                                return trimModId.equals(modId) || compatibleModIds.contains(trimModId);
                            }).forEach(latticeTrimMaterial -> latticeTrimMaterial.bootstrap(context)))
                            .add(Registries.TRIM_PATTERN, context -> LatticeRegistries.getTrimPatterns().stream().filter(latticeTrimPattern -> {
                                String patternModId = latticeTrimPattern.getModId();

                                return patternModId.equals(modId) || compatibleModIds.contains(patternModId);
                            }).forEach(latticeTrimPattern -> latticeTrimPattern.bootstrap(context))),
                    Set.of(modId)
            );
        }
    }

    public static class ItemModelGenerator extends ItemModelProvider {
        private final Set<Item> NEEDS_OVERLAY = Set.of(Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS);
        private final String modId;
        private final Set<String> compatibleMods;
        private final HolderLookup.Provider lookupProvider;
        private final HolderLookup.Provider registryProvider;

        public ItemModelGenerator(PackOutput output, String modId, Set<String> compatibleMods, ExistingFileHelper existingFileHelper, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<HolderLookup.Provider> registryProvider) {
            super(output, modId, existingFileHelper);
            this.modId = modId;
            this.compatibleMods = compatibleMods;
            this.lookupProvider = lookupProvider.join();
            this.registryProvider = registryProvider.join();
        }

        @Override
        protected void registerModels() {
            for (LatticeItem<?> latticeItem : LatticeRegistries.getItems()) {
                Item item = latticeItem.get();

                if (item instanceof ArmorItem armorItem) {
                    this.armorItem(armorItem);
                } else if (!isNotFromMod(latticeItem, modId, Set.of()) && !latticeItem.isTransmuted()) {
                    LatticeItemModelType modelType = latticeItem.getModelType();

                    if (modelType == LatticeTypes.Item.BASIC) {
                        this.basicItem(item);
                    } else if (modelType == LatticeTypes.Item.HANDHELD) {
                        this.handheldItem(item);
                    }
                }
            }

            for (LatticeTrimPattern latticeTrimPattern : LatticeRegistries.getTrimPatterns()) {
                if (!isNotFromMod(latticeTrimPattern, this.modId, Set.of())) {
                    this.basicItem(latticeTrimPattern.getTrimItem());
                }
            }

            for (LatticeBlock<?> latticeBlock : LatticeRegistries.getBlocks()) {
                if (!isNotFromMod(latticeBlock, this.modId, Set.of())) {
                    if (latticeBlock.getModelType() == LatticeTypes.Block.CUSTOM) {
                        String path = getKeyPath(latticeBlock);
                        this.withExistingParent(path, this.modLoc("block/" + path));
                    } else if (latticeBlock instanceof LatticeWallBlock latticeWallBlock) {
                        this.wallItem(latticeWallBlock);
                    }
                }
            }
        }

        private void armorItem(@NotNull ArmorItem armorItem) {
            this.getTrimMaterials().forEach((trimResourceKey, modelIndex) -> {
                Holder<ArmorMaterial> armorMaterial = armorItem.getMaterial();
                TrimMaterial trimMaterial = getMaterial(this.lookupProvider, this.registryProvider, trimResourceKey);
                Map<Holder<ArmorMaterial>, String> overrides = trimMaterial != null ? trimMaterial.overrideArmorMaterials() : Map.of();
                String modId = getKeyNamespace(armorItem);
                String path = getKeyPath(armorItem);

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> null;
                };

                if (armorType == null) {
                    return;
                }

                String itemPath = armorItem.toString();
                String trimName = itemPath + "_" + trimResourceKey.location().getPath() + "_trim";
                ResourceLocation itemResourceLocation = ResourceLocation.parse(itemPath);
                ResourceLocation trimResourceLocation = ResourceLocation.parse("trims/items/" + armorType + "_trim_" + (overrides.containsKey(armorMaterial) ? overrides.get(armorMaterial) : trimResourceKey.location().getPath()));
                ResourceLocation trimNameResourceLocation = ResourceLocation.parse(trimName);

                existingFileHelper.trackGenerated(trimResourceLocation, PackType.CLIENT_RESOURCES, ".png", "textures");

                ItemModelBuilder typeBuilder = this.getBuilder(trimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", itemResourceLocation.getNamespace() + ":item/" + itemResourceLocation.getPath());

                if (NEEDS_OVERLAY.contains(armorItem)) {
                    typeBuilder.texture("layer1", itemResourceLocation.getNamespace() + ":item/" + itemResourceLocation.getPath() + "_overlay")
                            .texture("layer2", trimResourceLocation);
                } else {
                    typeBuilder.texture("layer1", trimResourceLocation);
                }

                ItemModelBuilder builder = this.withExistingParent(modId + ":" + path, mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResourceLocation.getNamespace() + ":item/" + trimNameResourceLocation.getPath()))
                        .predicate(mcLoc("trim_type"), modelIndex).end()
                        .texture("layer0", ResourceLocation.fromNamespaceAndPath(modId, "item/" + path));

                if (NEEDS_OVERLAY.contains(armorItem)) {
                    builder.texture("layer1", ResourceLocation.fromNamespaceAndPath(modId, "item/" + path) + "_overlay");
                }
            });
        }

        private String getModId(@NotNull LatticeBlock<?> latticeBlock) {
            return this.getModId(latticeBlock.get());
        }

        private String getModId(@NotNull Block block) {
            return getKeyNamespace(block);
        }

        protected void wallItem(@NotNull LatticeWallBlock latticeWallBlock) {
            Block baseBlock = latticeWallBlock.getDefaultBlock().get();

            this.withExistingParent(getKeyPath(latticeWallBlock), this.mcLoc("block/wall_inventory"))
                    .texture("wall", ResourceLocation.fromNamespaceAndPath(this.getModId(baseBlock), "block/" + getKeyPath(baseBlock)));
        }

        private LinkedHashMap<ResourceKey<TrimMaterial>, Float> getTrimMaterials() {
            return LatticeRegistries.getTrimMaterials().stream()
                    .filter(latticeTrimMaterial -> !isNotFromMod(latticeTrimMaterial, this.modId, this.compatibleMods))
                    .map(latticeTrimMaterial -> {
                        ResourceKey<TrimMaterial> key = latticeTrimMaterial.get();
                        TrimMaterial trimMaterial = getMaterial(this.lookupProvider, this.registryProvider, key);

                        return new AbstractMap.SimpleEntry<>(key, trimMaterial != null ? trimMaterial.itemModelIndex() : 0.0f);
                    })
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (existing, toAdd) -> existing,
                            LinkedHashMap::new
                    ));
        }
    }

    public static class BlockModelGenerator extends BlockStateProvider {
        private final String modId;

        public BlockModelGenerator(PackOutput packOutput, String modId, ExistingFileHelper existingFileHelper) {
            super(packOutput, modId, existingFileHelper);
            this.modId = modId;
        }

        @Override
        protected void registerStatesAndModels() {
            for (LatticeBlock<?> latticeBlock : LatticeRegistries.getBlocks()) {
                if (isNotFromMod(latticeBlock, this.modId, Set.of())) {
                    continue;
                }

                this.generateBlock(latticeBlock.getModelType(), latticeBlock.get(), latticeBlock.getDefaultBlock().get());
            }
        }

        private <T extends Block> void generateBlock(@NotNull LatticeBlockModelType modelType, @NotNull T block, @NotNull Block defaultBlock) {
            String path = getKeyPath(block);
            boolean generateItem = true;

            switch (block) {
                case StairBlock stairBlock -> {
                    if (modelType == LatticeTypes.Block.BASIC) {
                        this.stairsBlock(stairBlock, this.blockTexture(defaultBlock));
                    } else if (modelType == LatticeTypes.Block.WITH_SIDES) {
                        this.stairsBlockWithRenderType(stairBlock, this.getSide(defaultBlock), this.getBase(defaultBlock), this.getBase(defaultBlock), "cutout");
                    }
                }
                case SlabBlock slabBlock -> {
                    if (modelType == LatticeTypes.Block.BASIC) {
                        this.slabBlock(slabBlock, this.getBase(defaultBlock), this.getBase(defaultBlock));
                    } else if (modelType == LatticeTypes.Block.WITH_SIDES) {
                        this.slabBlock(slabBlock, this.getBase(defaultBlock), this.getSlab(defaultBlock), this.getBase(defaultBlock), this.getBase(defaultBlock));
                    }
                }
                case WallBlock wallBlock -> {
                    this.wallBlock(wallBlock, this.blockTexture(defaultBlock));
                    generateItem = false;
                }
                case HorizontalDirectionalBlock dirBlock -> {
                    ResourceLocation base = this.getBase(dirBlock);
                    ResourceLocation front = this.getFront(dirBlock);

                    if (modelType == LatticeTypes.Block.BASIC) {
                        this.horizontalBlock(dirBlock, this.models().orientable(path, base, front, base));
                    } else if (modelType == LatticeTypes.Block.WITH_SIDES) {
                        this.horizontalBlock(dirBlock, this.models().orientable(path, this.getSide(dirBlock), front, this.getBase(defaultBlock)));
                    } else if (modelType == LatticeTypes.Block.CUSTOM) {
                        this.horizontalBlock(dirBlock, this.models().getExistingFile(this.modLoc("block/" + path)));
                    }
                }
                default -> {
                    if (modelType == LatticeTypes.Block.BASIC) {
                        this.simpleBlockWithItem(block, this.cubeAll(block));
                    } else if (modelType == LatticeTypes.Block.WITH_SIDES) {
                        ResourceLocation base = this.getBase(block);

                        if (block == defaultBlock) {
                            this.simpleBlockWithItem(block, this.models().cubeColumn(path, this.getSide(block), base));
                        } else {
                            this.simpleBlockWithItem(block, this.models().cubeColumn(path, base, this.getBase(defaultBlock)));
                        }
                    } else if (modelType == LatticeTypes.Block.CUSTOM) {
                        this.simpleBlock(block, this.models().getExistingFile(this.modLoc("block/" + getKeyPath(block))));
                    }

                    generateItem = false;
                }
            }

            if (generateItem && modelType != LatticeTypes.Block.CUSTOM) {
                this.createBlockItem(block);
            }
        }

        private void createBlockItem(@NotNull Block block) {
            this.simpleBlockItem(block, new ModelFile.UncheckedModelFile(getKeyNamespace(block) + ":block/" + getKeyPath(block)));
        }

        private ResourceLocation getTexture(@NotNull Block block, @Nullable String extension) {
            return ResourceLocation.fromNamespaceAndPath(getKeyNamespace(block), "block/" + getKeyPath(block) + ((extension == null) ? "" : "_" + extension));
        }

        private ResourceLocation getBase(@NotNull LatticeBlock<?> latticeBlock) {
            return this.getBase(latticeBlock.get());
        }

        private ResourceLocation getBase(@NotNull Block block) {
            return this.getTexture(block, null);
        }

        private ResourceLocation getSide(@NotNull LatticeBlock<?> latticeBlock) {
            return this.getSide(latticeBlock.get());
        }

        private ResourceLocation getSide(@NotNull Block block) {
            return this.getTexture(block, "side");
        }

        private ResourceLocation getFront(@NotNull LatticeBlock<?> latticeBlock) {
            return this.getFront(latticeBlock.get());
        }

        private ResourceLocation getFront(@NotNull Block block) {
            return this.getTexture(block, "front");
        }

        private ResourceLocation getTop(@NotNull LatticeBlock<?> latticeBlock) {
            return this.getTop(latticeBlock.get());
        }

        private ResourceLocation getTop(@NotNull Block block) {
            return this.getTexture(block, "top");
        }

        private ResourceLocation getBottom(@NotNull LatticeBlock<?> latticeBlock) {
            return this.getBottom(latticeBlock.get());
        }

        private ResourceLocation getBottom(@NotNull Block block) {
            return this.getTexture(block, "bottom");
        }

        private ResourceLocation getSlab(@NotNull Block block) {
            return this.getTexture(block, "slab");
        }
    }

    public static class AtlasGenerator extends SpriteSourceProvider {
        private final Set<String> compatibleModIds;
        private final HolderLookup.Provider registryProvider;

        public AtlasGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<HolderLookup.Provider> registryProvider, String modId, Set<String> compatibleModIds, ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, modId, existingFileHelper);
            this.compatibleModIds = compatibleModIds;
            this.registryProvider = registryProvider.join();
        }

        @Override
        protected void gather() {
            this.atlas(BLOCKS_ATLAS)
                    .addSource(new DirectoryLister("item", "item/"))
                    .addSource(new PalettedPermutations(
                            Stream.of("helmet", "chestplate", "leggings", "boots").map(armorType -> ResourceLocation.withDefaultNamespace("trims/items/" + armorType + "_trim")).toList(),
                            ResourceLocation.withDefaultNamespace("trims/color_palettes/trim_palette"),
                            this.getTrimMaterials(false)
                    ));

            this.atlas(ResourceLocation.withDefaultNamespace("armor_trims")).addSource(new PalettedPermutations(
                    LatticeRegistries.getTrimPatterns().stream()
                            .filter(latticeTrimPattern -> !isNotFromMod(latticeTrimPattern, this.modid, this.compatibleModIds))
                            .flatMap(latticeTrimPattern -> Stream.of(
                                    ResourceLocation.fromNamespaceAndPath(latticeTrimPattern.getModId(), "trims/models/armor/" + latticeTrimPattern.getId()),
                                    ResourceLocation.fromNamespaceAndPath(latticeTrimPattern.getModId(), "trims/models/armor/" + latticeTrimPattern.getId() + "_leggings")
                            )).toList(),
                    ResourceLocation.withDefaultNamespace("trims/color_palettes/trim_palette"),
                    this.getTrimMaterials(true)
            ));
        }

        private Map<String, ResourceLocation> getTrimMaterials(boolean includeCompatible) {
            return LatticeRegistries.getTrimMaterials().stream()
                    .filter(latticeTrimMaterial -> !isNotFromMod(latticeTrimMaterial, this.modid, includeCompatible ? this.compatibleModIds : Set.of()))
                    .flatMap(latticeTrimMaterial -> {
                        String modId = latticeTrimMaterial.getModId();
                        String id = latticeTrimMaterial.getId();
                        TrimMaterial trimMaterial = getMaterial(this.lookupProvider.join(), this.registryProvider, latticeTrimMaterial.get());

                        return Stream.concat(
                                Stream.of(
                                        Map.entry(id, ResourceLocation.fromNamespaceAndPath(modId, "trims/color_palettes/" + id))
                                ),
                                trimMaterial == null ? Stream.empty() : trimMaterial
                                        .overrideArmorMaterials().values().stream().map(overrideId ->
                                                Map.entry(overrideId, ResourceLocation.fromNamespaceAndPath(modId, "trims/color_palettes/" + overrideId))
                                        )
                        );
                    }).collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (existing, replacement) -> existing
                    ));
        }
    }

    private static String getKeyNamespace(@NotNull LatticeBlock<?> latticeBlock) {
        return getKeyNamespace(latticeBlock.get());
    }

    private static String getKeyPath(@NotNull LatticeBlock<?> latticeBlock) {
        return getKeyPath(latticeBlock.get());
    }

    private static String getKeyNamespace(@NotNull Block block) {
        return getKey(block).getNamespace();
    }

    private static String getKeyPath(@NotNull Block block) {
        return getKey(block).getPath();
    }

    private static String getKeyNamespace(@NotNull LatticeItem<?> latticeItem) {
        return getKeyNamespace(latticeItem.get());
    }

    private static String getKeyPath(@NotNull LatticeItem<?> latticeItem) {
        return getKeyPath(latticeItem.get());
    }

    private static String getKeyNamespace(@NotNull Item item) {
        return getKey(item).getNamespace();
    }

    private static String getKeyPath(@NotNull Item item) {
        return getKey(item).getPath();
    }

    private static ResourceLocation getKey(@NotNull Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    private static ResourceLocation getKey(@NotNull Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    private static void incompleteObjectErr(@NotNull RegistryId registryId) {
        ErrUtils.incompleteObjectErr(registryId);
    }

    private static <I extends LatticeObject> boolean isNotFromMod(@NotNull I latticeObject, @NotNull String modId, @NotNull Set<String> compatibleModIds) {
        String targetId = latticeObject.getModId();

        return !targetId.equals(modId) && !compatibleModIds.contains(targetId);
    }

    private static boolean hasMaterial(@NotNull HolderLookup.Provider lookupProvider, @NotNull ResourceKey<TrimMaterial> trimMaterialResourceKey) {
        return lookupProvider.lookupOrThrow(Registries.TRIM_MATERIAL).get(trimMaterialResourceKey).isPresent();
    }

    private static TrimMaterial getMaterial(@NotNull HolderLookup.Provider lookupProvider, @NotNull HolderLookup.Provider registryProvider, @NotNull ResourceKey<TrimMaterial> trimMaterialResourceKey) {
        return lookupProvider.lookup(Registries.TRIM_MATERIAL)
                .flatMap(registryLookup -> registryLookup.get(trimMaterialResourceKey))
                .or(() -> registryProvider.lookup(Registries.TRIM_MATERIAL).flatMap(registryLookup -> registryLookup.get(trimMaterialResourceKey)))
                .map(Holder::value)
                .orElse(null);
    }

    public static void addListeners(@NotNull IEventBus modEventBus, @NotNull ModContainer modContainer, @NotNull Set<String> compatibleMods) {
        modEventBus.addListener((GatherDataEvent event) -> onGatherDataEvent(event, modContainer.getModId(), compatibleMods));
    }
}