package net.ethyl.lattice_api.core.data;

import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.base.LatticeObject;
import net.ethyl.lattice_api.modules.base.LatticeTag;
import net.ethyl.lattice_api.modules.common.blocks.LatticeBasicBlock;
import net.ethyl.lattice_api.modules.common.blocks.LatticeSlabBlock;
import net.ethyl.lattice_api.modules.common.blocks.LatticeStairBlock;
import net.ethyl.lattice_api.modules.common.tags.LatticeBlockTag;
import net.ethyl.lattice_api.modules.common.tags.LatticeItemTag;
import net.ethyl.lattice_api.modules.common.types.modelTypes.LatticeBlockModelType;
import net.ethyl.lattice_api.modules.common.types.modelTypes.LatticeItemModelType;
import net.ethyl.lattice_api.modules.common.types.other.LatticeLootTable;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

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

                    latticeBlockTag.getTagContent().forEach(blockSupplier -> tagAppender.add(blockSupplier.get()));
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

                if (latticeTag instanceof LatticeItemTag lItemTag) {
                    IntrinsicTagAppender<Item> tagAppender = tag(lItemTag.get());

                    lItemTag.getTagContent().forEach(itemSupplier -> tagAppender.add(itemSupplier.get()));
                }
            }
        }
    }

    public static class ItemModelGenerator extends ItemModelProvider {
        public ItemModelGenerator(PackOutput output, String modId, ExistingFileHelper existingFileHelper) {
            super(output, modId, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            for (LatticeItem<?> latticeItem : LatticeRegistries.getItems()) {
                if (isNotFromMod(latticeItem, this.modid)) continue;

                Item item = latticeItem.get();
                LatticeItemModelType modelType = latticeItem.getModelType();

                if (modelType == LatticeRegistries.Types.Item.BASIC) basicItem(item);
                else if (modelType == LatticeRegistries.Types.Item.HANDHELD) handheldItem(item);
            }
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

                if (latticeBlock instanceof LatticeBasicBlock latticeBasicBlock) {
                    Block block = latticeBasicBlock.get();
                    String path = this.getPath(latticeBlock);

                    if (modelType == LatticeRegistries.Types.Block.BASIC) {
                        this.simpleBlockWithItem(block, this.cubeAll(block));
                    } else if (modelType == LatticeRegistries.Types.Block.WITH_SIDES) {
                        this.simpleBlockWithItem(block, this.models().cubeColumn(path, getSide(latticeBlock), getBase(latticeBlock)));
                    }
                } else {
                    if (latticeBlock instanceof LatticeStairBlock latticeStairBlock) {
                        StairBlock stairBlock = latticeStairBlock.get();
                        Block defaultBlock = latticeStairBlock.getDefaultBlock().get();

                        if (modelType == LatticeRegistries.Types.Block.BASIC) {
                            this.stairsBlock(stairBlock, this.blockTexture(latticeStairBlock.getDefaultBlock().get()));
                        } else if (modelType == LatticeRegistries.Types.Block.WITH_SIDES) {
                            this.stairsBlockWithRenderType(stairBlock, this.getSide(defaultBlock), this.getBase(defaultBlock), this.getBase(defaultBlock), "cutout");
                        }
                    } else if (latticeBlock instanceof LatticeSlabBlock latticeSlabBlock) {
                        SlabBlock slabBlock = latticeSlabBlock.get();
                        Block defaultBlock = latticeSlabBlock.getDefaultBlock().get();

                        if (modelType == LatticeRegistries.Types.Block.BASIC) {
                            this.slabBlock(slabBlock, this.getBase(defaultBlock), this.getBase(defaultBlock));
                        } else if (modelType == LatticeRegistries.Types.Block.WITH_SIDES) {
                            this.slabBlock(slabBlock, this.getBase(defaultBlock), this.getSlab(defaultBlock), this.getBase(defaultBlock), this.getBase(defaultBlock));
                        }
                    }

                    this.createBlockItem(latticeBlock);
                }
            }
        }

        private String getPath(@NotNull LatticeBlock<?> latticeBlock) {
            return this.getPath(latticeBlock.get());
        }

        private String getPath(@NotNull Block block) {
            return BuiltInRegistries.BLOCK.getKey(block).getPath();
        }

        private ResourceLocation getTexture(@NotNull Block block, @Nullable String extension) {
            return this.modLoc("block/" + this.getPath(block) + ((extension == null) ? "" : "_" + extension));
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
            this.simpleBlockItem(latticeBlock.get(), new ModelFile.UncheckedModelFile(latticeBlock.getModId() + ":block/" + this.getPath(latticeBlock)));
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

                if (lootType == LatticeRegistries.Types.LootTable.SELF) {
                    this.dropSelf(block);
                } else if (lootType == LatticeRegistries.Types.LootTable.SILK_TOUCH) {
                    this.dropWhenSilkTouch(block);
                } else if (lootType == LatticeRegistries.Types.LootTable.OTHER) {
                    this.dropOther(block, lootType.drop.get());
                } else if (lootType == LatticeRegistries.Types.LootTable.AMOUNT) {
                    this.add(block, sBlock -> this.createAmountDrops(sBlock, lootType.drop.get(), lootType.minDrops, lootType.maxDrops));
                }
            }
        }

        private LootTable.Builder createAmountDrops(Block block, Item item, float minDrops, float maxDrops) {
            HolderLookup.RegistryLookup<Enchantment> registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

            return this.createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops))).apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(Enchantments.FORTUNE)))));
        }

        @Override
        protected @NotNull Iterable<Block> getKnownBlocks() {
            return LatticeRegistries.getBlocks().stream().filter(latticeBlock -> !isNotFromMod(latticeBlock, this.modId)).map(latticeBlock -> (Block) latticeBlock.get()).toList();
        }
    }

    public static void addListeners(@NotNull IEventBus modEventBus, @NotNull String modId) {
        modEventBus.addListener((GatherDataEvent event) -> onGatherDataEvent(event, modId));
    }

    public static <I extends LatticeObject> boolean isNotFromMod(@NotNull I latticeObject, @NotNull String modId) {
        return !latticeObject.getModId().equals(modId);
    }
}