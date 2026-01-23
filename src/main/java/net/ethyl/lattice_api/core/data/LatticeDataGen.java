package net.ethyl.lattice_api.core.data;

import net.ethyl.lattice_api.LatticeApi;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.*;
import net.ethyl.lattice_api.modules.common.RecipeTypes.*;
import net.ethyl.lattice_api.modules.common.blocks.LatticeBasicBlock;
import net.ethyl.lattice_api.modules.common.blocks.LatticeWallBlock;
import net.ethyl.lattice_api.modules.common.blocks.LatticeSlabBlock;
import net.ethyl.lattice_api.modules.common.blocks.LatticeStairBlock;
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
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.*;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class LatticeDataGen {
    public static void onGatherDataEvent(GatherDataEvent event, String modId) {
        DataGenerator dataGenerator = event.getGenerator();
        PackOutput packOutput = dataGenerator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        BlockTagsProvider blockTagsProvider = new BlockTagGenerator(packOutput, lookupProvider, modId, existingFileHelper);
        dataGenerator.addProvider(event.includeServer(), blockTagsProvider);
        dataGenerator.addProvider(event.includeServer(), new ItemTagGenerator(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), modId, existingFileHelper));

        dataGenerator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(ignored -> new BlockLootTableProvider(lookupProvider.join(), modId), LootContextParamSets.BLOCK)), lookupProvider));
        dataGenerator.addProvider(event.includeServer(), new RecipeGenerator(packOutput, lookupProvider, modId));

        dataGenerator.addProvider(event.includeClient(), new ItemModelGenerator(packOutput, modId, existingFileHelper));
        dataGenerator.addProvider(event.includeClient(), new BlockModelGenerator(packOutput, modId, existingFileHelper));
    }

    public static class BlockTagGenerator extends BlockTagsProvider {
        public BlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, modId, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider provider) {
            for (LatticeTag<?> latticeTag : LatticeRegistries.getTags()) {
                if (isNotFromMod(latticeTag, this.modId)) continue;

                if (latticeTag instanceof LatticeBlockTag latticeBlockTag) {
                    IntrinsicTagAppender<Block> tagAppender = tag(latticeBlockTag.get());

                    latticeBlockTag.getKeyTagContent().forEach(tagKeySupplier -> tagAppender.addTag(tagKeySupplier.get()));
                    latticeBlockTag.getTagContent().forEach(blockSupplier -> tagAppender.add(blockSupplier.get()));
                }
            }

            IntrinsicTagAppender<Block> wallsTagAppender = this.tag(BlockTags.WALLS);
            IntrinsicTagAppender<Block> pickaxeTagAppender = this.tag(BlockTags.MINEABLE_WITH_PICKAXE);
            IntrinsicTagAppender<Block> axeTagAppender = this.tag(BlockTags.MINEABLE_WITH_AXE);
            IntrinsicTagAppender<Block> shovelTagAppender = this.tag(BlockTags.MINEABLE_WITH_SHOVEL);
            IntrinsicTagAppender<Block> hoeTagAppender = this.tag(BlockTags.MINEABLE_WITH_HOE);

            for (LatticeBlock<?> latticeBlock : LatticeRegistries.getBlocks()) {
                LatticeToolType toolType = latticeBlock.getToolType();
                Block block = latticeBlock.get();

                if (latticeBlock instanceof LatticeWallBlock) {
                    wallsTagAppender.add(block);
                }

                if (toolType == LatticeRegistries.Types.ToolType.PICKAXE) {
                    pickaxeTagAppender.add(block);
                } else if (toolType == LatticeRegistries.Types.ToolType.AXE) {
                    axeTagAppender.add(block);
                } else if (toolType == LatticeRegistries.Types.ToolType.SHOVEL) {
                    shovelTagAppender.add(block);
                } else if (toolType == LatticeRegistries.Types.ToolType.HOE) {
                    hoeTagAppender.add(block);
                }
            }
        }
    }

    public static class ItemTagGenerator extends ItemTagsProvider {
        public ItemTagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(packOutput, lookupProvider, blockTags, modId, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider provider) {
            for (LatticeTag<?> latticeTag : LatticeRegistries.getTags()) {
                if (isNotFromMod(latticeTag, this.modId)) continue;

                if (latticeTag instanceof LatticeItemTag latticeItemTag) {
                    IntrinsicTagAppender<Item> tagAppender = this.tag(latticeItemTag.get());

                    latticeItemTag.getKeyTagContent().forEach(tagKeySupplier -> tagAppender.addTag(tagKeySupplier.get()));
                    latticeItemTag.getTagContent().forEach(itemSupplier -> tagAppender.add(itemSupplier.get()));
                }
            }

            IntrinsicTagAppender<Item> swordTagAppender = this.tag(ItemTags.SWORDS);
            IntrinsicTagAppender<Item> pickaxeTagAppender = this.tag(ItemTags.PICKAXES);
            IntrinsicTagAppender<Item> axeTagAppender = this.tag(ItemTags.AXES);
            IntrinsicTagAppender<Item> shovelTagAppender = this.tag(ItemTags.SHOVELS);
            IntrinsicTagAppender<Item> hoeTagAppender = this.tag(ItemTags.HOES);

            for (LatticeItem<?> latticeItem : LatticeRegistries.getItems()) {
                Item item = latticeItem.get();

                switch (latticeItem) {
                    case LatticeBasicSword ignored -> swordTagAppender.add(item);
                    case LatticeBasicPickaxe ignored -> pickaxeTagAppender.add(item);
                    case LatticeBasicAxe ignored -> axeTagAppender.add(item);
                    case LatticeBasicShovel ignored -> shovelTagAppender.add(item);
                    case LatticeBasicHoe ignored -> hoeTagAppender.add(item);
                    default -> {
                    }
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
                if (isNotFromMod(latticeBlock, this.modId)) continue;

                LatticeLootTable lootType = latticeBlock.getLootType();
                Block block = latticeBlock.get();

                if (latticeBlock instanceof LatticeSlabBlock latticeSlabBlock) {
                    SlabBlock slabBlock = latticeSlabBlock.get();

                    if (lootType == LatticeRegistries.Types.LootTable.SELF) {
                        this.add(slabBlock, this.createSlabDrops(latticeSlabBlock, null, 1.0f, 1.0f, false));
                    } else if (lootType == LatticeRegistries.Types.LootTable.SILK_TOUCH) {
                        this.add(slabBlock, this.createSlabDrops(latticeSlabBlock, null, 1.0f, 1.0f, true));
                    } else if (lootType == LatticeRegistries.Types.LootTable.OTHER) {
                        this.add(slabBlock, this.createSlabDrops(latticeSlabBlock, lootType.getDrop(), 1.0f, 1.0f, false));
                    } else if (lootType == LatticeRegistries.Types.LootTable.AMOUNT) {
                        this.add(slabBlock, this.createSlabDrops(latticeSlabBlock, lootType.getDrop(), lootType.getMinDrops(), lootType.getMaxDrops(), false));
                    }
                } else {
                    if (lootType == LatticeRegistries.Types.LootTable.SELF) {
                        this.add(block, this.createDrops(latticeBlock, null, 1.0f, 1.0f, false));
                    } else if (lootType == LatticeRegistries.Types.LootTable.SILK_TOUCH) {
                        this.add(block, this.createDrops(latticeBlock, null, 1.0f, 1.0f, true));
                    } else if (lootType == LatticeRegistries.Types.LootTable.OTHER) {
                        this.add(block, this.createDrops(latticeBlock, lootType.getDrop(), 1.0f, 1.0f, false));
                    } else if (lootType == LatticeRegistries.Types.LootTable.AMOUNT) {
                        this.add(block, this.createDrops(latticeBlock, lootType.getDrop(), lootType.getMinDrops(), lootType.getMaxDrops(), false));
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
            return LatticeRegistries.getBlocks().stream().filter(latticeBlock -> !isNotFromMod(latticeBlock, this.modId)).map(latticeBlock -> (Block) latticeBlock.get()).toList();
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
            for (LatticeRecipe latticeRecipe : LatticeRegistries.getRecipes()) {
                if (isNotFromMod(latticeRecipe, this.modId)) continue;

                RegistryId registryId = latticeRecipe.getRegistryId();
                ResourceLocation recipeId = registryId.toResourceLoc();
                RecipeCategory recipeCategory = latticeRecipe.getCategory();
                Collection<Supplier<Item>> result = latticeRecipe.getResult();
                int count = latticeRecipe.getResultCount();
                int resultAmount = result.size();
                String unlockId = latticeRecipe.getUnlockId();
                String groupId = latticeRecipe.getGroup();
                Item unlockItem = latticeRecipe.getUnlockItem().get();

                if (result.isEmpty()) {
                    incompleteObjectErr(registryId);
                }

                switch (latticeRecipe) {
                    case LatticeShapedRecipe latticeShapedRecipe -> {
                        Map<Character, Collection<Supplier<Item>>> ingredientList = latticeShapedRecipe.getDefined();

                        if (resultAmount > 1) {
                            AtomicBoolean failedTest = new AtomicBoolean(true);

                            ingredientList.values().forEach(ingredients -> {
                                if (ingredients.size() == resultAmount) {
                                    failedTest.set(false);
                                }
                            });

                            if (failedTest.get()) {
                                incompleteObjectErr(registryId);
                            }
                        }

                        AtomicInteger resultIndex = new AtomicInteger(0);

                        result.forEach(resultSupplier -> {
                            ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(recipeCategory, resultSupplier.get(), count);
                            AtomicReference<Supplier<Item>> reference = new AtomicReference<>(() -> ((List<Supplier<Item>>) result).get(resultIndex.get()).get());
                            AtomicReference<String> prefix = new AtomicReference<>("");
                            AtomicReference<String> suffix = new AtomicReference<>("");

                            latticeShapedRecipe.getPattern().values().forEach(builder::pattern);

                            ingredientList.forEach((c, ingredients) -> {
                                if (resultAmount == 1 || ingredients.size() != resultAmount) {
                                    builder.define(c, Ingredient.of(ingredients.stream().map(Supplier::get).map(ItemStack::new)));
                                } else {
                                    if (latticeShapedRecipe instanceof LatticeShapedDyeRecipe latticeShapedDyeRecipe && latticeShapedDyeRecipe.isDyeable(c)) {
                                        builder.define(c, Ingredient.of(IntStream.range(0, ingredients.size()).filter(i -> i != resultIndex.get()).mapToObj(i -> new ItemStack(((List<Supplier<Item>>) ingredients).get(i).get()))));
                                        prefix.set("dye");
                                    } else {
                                        int index = 0;

                                        for (Supplier<Item> ingredientSupplier : ingredients) {
                                            if (index == resultIndex.get() && reference.get() != ingredientSupplier.get()) {
                                                builder.define(c, ingredientSupplier.get());

                                                if (!(latticeShapedRecipe instanceof LatticeShapedDyeRecipe)) {
                                                    suffix.set("from_" + getKeyPath(ingredientSupplier.get()));
                                                }

                                                break;
                                            }

                                            index++;
                                        }
                                    }
                                }
                            });

                            latticeShapedRecipe.getDefinedTags().forEach(builder::define);

                            builder.unlockedBy(unlockId, has(unlockItem));
                            builder.group(groupId);
                            builder.save(recipeOutput, (resultAmount != 1 ? (recipeId.getNamespace() + ":" + (prefix.get().isEmpty() ? "" : prefix.get() + "_") + getKeyPath(reference.get().get()) + (suffix.get().isEmpty() ? "" : "_" + suffix.get()) + "_shaped") : recipeId).toString());
                            resultIndex.incrementAndGet();
                        });
                    }
                    case LatticeShapelessRecipe latticeShapelessRecipe -> {
                        Map<Integer, Collection<Supplier<Item>>> ingredientList = latticeShapelessRecipe.getIngredients();

                        if (resultAmount > 1) {
                            AtomicBoolean failedTest = new AtomicBoolean(true);

                            ingredientList.values().forEach(ingredients -> {
                                if (ingredients.size() == resultAmount) {
                                    failedTest.set(false);
                                }
                            });

                            if (failedTest.get()) {
                                incompleteObjectErr(registryId);
                            }
                        }

                        AtomicInteger resultIndex = new AtomicInteger(0);

                        result.forEach(resultSupplier -> {
                            ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(recipeCategory, resultSupplier.get(), count);
                            AtomicReference<Supplier<Item>> reference = new AtomicReference<>(() -> ((List<Supplier<Item>>) result).get(resultIndex.get()).get());
                            AtomicReference<String> prefix = new AtomicReference<>("");
                            AtomicReference<String> suffix = new AtomicReference<>("");

                            ingredientList.forEach((ingredientNumber, ingredients) -> {
                                if (resultAmount == 1 || ingredients.size() != resultAmount) {
                                    builder.requires(Ingredient.of(ingredients.stream().map(Supplier::get).map(ItemStack::new)));
                                } else {
                                    if (latticeShapelessRecipe instanceof LatticeShapelessDyeRecipe latticeShapelessDyeRecipe && latticeShapelessDyeRecipe.isDyeable(ingredientNumber)) {
                                        builder.requires(Ingredient.of(IntStream.range(0, ingredients.size()).filter(i -> i != resultIndex.get()).mapToObj(i -> new ItemStack(((List<Supplier<Item>>) ingredients).get(i).get()))));
                                        prefix.set("dye");
                                    } else {
                                        int index = 0;

                                        for (Supplier<Item> ingredientSupplier : ingredients) {
                                            if (index == resultIndex.get() && reference.get() != ingredientSupplier.get()) {
                                                builder.requires(ingredientSupplier.get());

                                                if (!(latticeShapelessRecipe instanceof LatticeShapelessDyeRecipe)) {
                                                    suffix.set("from_" + getKeyPath(ingredientSupplier.get()));
                                                }

                                                break;
                                            }

                                            index++;
                                        }
                                    }
                                }
                            });

                            latticeShapelessRecipe.getIngredientTags().values().forEach(builder::requires);

                            builder.unlockedBy(unlockId, has(unlockItem));
                            builder.group(groupId);
                            builder.save(recipeOutput, (resultAmount != 1 ? (recipeId.getNamespace() + ":" + (prefix.get().isEmpty() ? "" : prefix.get() + "_") + getKeyPath(reference.get().get()) + (suffix.get().isEmpty() ? "" : "_" + suffix.get()) + "_shapeless") : recipeId).toString());
                            resultIndex.incrementAndGet();
                        });
                    }
                    case LatticeCookingRecipe latticeCookingRecipe -> {
                        Map<Integer, Map<Collection<Supplier<Item>>, Float>> ingredientList = latticeCookingRecipe.getIngredients();

                        if (latticeCookingRecipe.getIngredients().isEmpty() || latticeCookingRecipe.getSerializers().isEmpty() || ingredientList.size() != resultAmount) {
                            incompleteObjectErr(registryId);
                        }

                        AtomicInteger resultIndex = new AtomicInteger(0);

                        result.forEach(resultSupplier -> {
                            AtomicReference<SimpleCookingRecipeBuilder> builder = new AtomicReference<>();
                            AtomicReference<Supplier<Item>> reference = new AtomicReference<>(() -> ((List<Supplier<Item>>) result).get(resultIndex.get()).get());
                            AtomicReference<String> suffix = new AtomicReference<>("");

                            ingredientList.forEach((index, ingredientData) -> ingredientData.forEach((ingredients, experience) -> {
                                String ingredientReference = getKeyPath(((List<Supplier<Item>>) ingredients).getLast().get());


                                if (resultIndex.get() == index) {
                                    latticeCookingRecipe.getSerializers().forEach((recipeSerializer, cookingTime) -> {
                                        String type = null;

                                        if (recipeSerializer == RecipeSerializer.SMELTING_RECIPE) {
                                            builder.set(this.smeltingRecipe(ingredients, recipeCategory, resultSupplier, experience, cookingTime));
                                            type = "smelting";
                                        } else if (recipeSerializer == RecipeSerializer.BLASTING_RECIPE) {
                                            builder.set(this.blastingRecipe(ingredients, recipeCategory, resultSupplier, experience, cookingTime));
                                            type = "blasting";
                                        } else if (recipeSerializer == RecipeSerializer.SMOKING_RECIPE) {
                                            builder.set(this.smokingRecipe(ingredients, recipeCategory, resultSupplier, experience, cookingTime));
                                            type = "smoking";
                                        }

                                        if (type != null) {
                                            suffix.set("_" + type + "_from_" + ingredientReference);
                                        }

                                        SimpleCookingRecipeBuilder recipeBuilder = builder.get();

                                        if (recipeBuilder != null) {
                                            recipeBuilder.unlockedBy(unlockId, has(unlockItem));
                                            recipeBuilder.group(groupId);
                                            recipeBuilder.save(recipeOutput, (resultAmount != 1 ? (recipeId.getNamespace() + ":" + getKeyPath(reference.get().get()) + suffix) : recipeId).toString());
                                        }
                                    });
                                }
                            }));

                            resultIndex.incrementAndGet();
                        });
                    }
                    default -> {
                    }
                }
            }
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

    public static class ItemModelGenerator extends ItemModelProvider {
        private final String modId;

        public ItemModelGenerator(PackOutput output, String modId, ExistingFileHelper existingFileHelper) {
            super(output, modId, existingFileHelper);
            this.modId = modId;
        }

        @Override
        protected void registerModels() {
            for (LatticeItem<?> latticeItem : LatticeRegistries.getItems()) {
                if (isNotFromMod(latticeItem, this.modId)) continue;

                Item item = latticeItem.get();
                LatticeItemModelType modelType = latticeItem.getModelType();

                if (modelType == LatticeRegistries.Types.Item.BASIC) this.basicItem(item);
                else if (modelType == LatticeRegistries.Types.Item.HANDHELD) this.handheldItem(item);
            }

            for (LatticeBlock<?> latticeBlock : LatticeRegistries.getBlocks()) {
                if (isNotFromMod(latticeBlock, this.modId)) continue;

                if (latticeBlock instanceof LatticeWallBlock latticeWallBlock) {
                    this.wallItem(latticeWallBlock);
                }
            }
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
                if (isNotFromMod(latticeBlock, this.modId)) continue;

                LatticeBlockModelType modelType = latticeBlock.getModelType();
                Block defaultBlock = latticeBlock.getDefaultBlock().get();
                boolean isSameBlock = latticeBlock.get() == defaultBlock;

                if (latticeBlock instanceof LatticeBasicBlock latticeBasicBlock) {
                    Block block = latticeBasicBlock.get();
                    String path = getKeyPath(latticeBlock);

                    if (modelType == LatticeRegistries.Types.Block.BASIC) {
                        this.simpleBlockWithItem(block, this.cubeAll(block));
                    } else if (modelType == LatticeRegistries.Types.Block.WITH_SIDES) {
                        if (isSameBlock) {
                            this.simpleBlockWithItem(block, this.models().cubeColumn(path, this.getSide(latticeBlock), this.getBase(latticeBlock)));
                        } else {
                            this.simpleBlockWithItem(block, this.models().cubeColumn(path, this.getBase(latticeBlock), this.getBase(defaultBlock)));
                        }
                    }
                } else {
                    switch (latticeBlock) {
                        case LatticeStairBlock latticeStairBlock -> {
                            StairBlock stairBlock = latticeStairBlock.get();

                            if (modelType == LatticeRegistries.Types.Block.BASIC) {
                                this.stairsBlock(stairBlock, this.blockTexture(latticeStairBlock.getDefaultBlock().get()));
                            } else if (modelType == LatticeRegistries.Types.Block.WITH_SIDES) {
                                this.stairsBlockWithRenderType(stairBlock, this.getSide(defaultBlock), this.getBase(defaultBlock), this.getBase(defaultBlock), "cutout");
                            }
                        }
                        case LatticeSlabBlock latticeSlabBlock -> {
                            SlabBlock slabBlock = latticeSlabBlock.get();

                            if (modelType == LatticeRegistries.Types.Block.BASIC) {
                                this.slabBlock(slabBlock, this.getBase(defaultBlock), this.getBase(defaultBlock));
                            } else if (modelType == LatticeRegistries.Types.Block.WITH_SIDES) {
                                this.slabBlock(slabBlock, this.getBase(defaultBlock), this.getSlab(defaultBlock), this.getBase(defaultBlock), this.getBase(defaultBlock));
                            }
                        }
                        case LatticeWallBlock latticeWallBlock -> {
                            WallBlock wallBlock = latticeWallBlock.get();
                            this.wallBlock(wallBlock, this.blockTexture(defaultBlock));

                            continue;
                        }
                        default -> {
                            this.createBlockItem(latticeBlock);

                            continue;
                        }
                    }

                    this.createBlockItem(latticeBlock);
                }
            }
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

        private void createBlockItem(@NotNull LatticeBlock<?> latticeBlock) {
            this.simpleBlockItem(latticeBlock.get(), new ModelFile.UncheckedModelFile(latticeBlock.getModId() + ":block/" + getKeyPath(latticeBlock)));
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
        LatticeApi.incompleteObjectErr(registryId);
    }

    public static <I extends LatticeObject> boolean isNotFromMod(@NotNull I latticeObject, @NotNull String modId) {
        return !latticeObject.getModId().equals(modId);
    }

    public static void addListeners(@NotNull IEventBus modEventBus, @NotNull ModContainer modContainer) {
        modEventBus.addListener((GatherDataEvent event) -> onGatherDataEvent(event, modContainer.getModId()));
    }
}