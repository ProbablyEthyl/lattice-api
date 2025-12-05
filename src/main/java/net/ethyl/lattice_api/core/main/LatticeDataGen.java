package net.ethyl.lattice_api.core.main;

import net.ethyl.lattice_api.LatticeApi;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.base.LatticeTag;
import net.ethyl.lattice_api.modules.common.blocks.LatticeBasicBlock;
import net.ethyl.lattice_api.modules.common.blocks.LatticeSlabBlock;
import net.ethyl.lattice_api.modules.common.blocks.LatticeStairBlock;
import net.ethyl.lattice_api.modules.common.modelTypes.LatticeBlockModelType;
import net.ethyl.lattice_api.modules.common.modelTypes.LatticeItemModelType;
import net.ethyl.lattice_api.modules.common.tags.LatticeBlockTag;
import net.ethyl.lattice_api.modules.common.tags.LatticeItemTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
                if (!latticeTag.getModId().equals(this.modId)) continue;

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
                if (!latticeTag.getModId().equals(this.modId)) continue;

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
                if (!latticeItem.getModId().equals(this.modid)) continue;

                Item item = latticeItem.get();
                LatticeItemModelType modelType = latticeItem.getModelType();

                if (modelType == LatticeRegistries.ModelTypes.Item.BASIC) basicItem(item);
                else if (modelType == LatticeRegistries.ModelTypes.Item.HANDHELD) handheldItem(item);
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
                if (!latticeBlock.getModId().equals(this.modId)) continue;

                LatticeBlockModelType modelType = latticeBlock.getModelType();

                if (latticeBlock instanceof LatticeBasicBlock latticeBasicBlock) {
                    Block block = latticeBasicBlock.get();
                    String path = this.getPath(latticeBlock);

                    if (modelType == LatticeRegistries.ModelTypes.Block.BASIC) {
                        this.simpleBlockWithItem(block, this.cubeAll(block));
                    } else if (modelType == LatticeRegistries.ModelTypes.Block.WITH_SIDES) {
                        this.simpleBlockWithItem(block, this.models().cubeColumn(path, getSide(latticeBlock), getBase(latticeBlock)));
                    }
                } else {
                    if (latticeBlock instanceof LatticeStairBlock latticeStairBlock) {
                        StairBlock stairBlock = latticeStairBlock.get();
                        Block defaultBlock = latticeStairBlock.getDefaultBlock().get();

                        if (modelType == LatticeRegistries.ModelTypes.Block.BASIC) {
                            this.stairsBlock(stairBlock, this.blockTexture(latticeStairBlock.getDefaultBlock().get()));
                        } else if (modelType == LatticeRegistries.ModelTypes.Block.WITH_SIDES) {
                            this.stairsBlockWithRenderType(stairBlock, this.getSide(defaultBlock), this.getBase(defaultBlock), this.getBase(defaultBlock), "cutout");
                        }
                    } else if (latticeBlock instanceof LatticeSlabBlock latticeSlabBlock) {
                        SlabBlock slabBlock = latticeSlabBlock.get();
                        Block defaultBlock = latticeSlabBlock.getDefaultBlock().get();

                        if (modelType == LatticeRegistries.ModelTypes.Block.BASIC) {
                            this.slabBlock(slabBlock, this.getBase(defaultBlock), this.getBase(defaultBlock));
                        } else if (modelType == LatticeRegistries.ModelTypes.Block.WITH_SIDES) {
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

    public static void addListeners(@NotNull IEventBus modEventBus, @NotNull String modId) {
        modEventBus.addListener((GatherDataEvent event) -> onGatherDataEvent(event, modId));
    }
}