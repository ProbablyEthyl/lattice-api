package net.ethyl.lattice_api.core.main;

import net.ethyl.lattice_api.LatticeApi;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.base.LatticeTag;
import net.ethyl.lattice_api.modules.common.modelTypes.item.LatticeItemModelType;
import net.ethyl.lattice_api.modules.common.tags.LatticeBlockTag;
import net.ethyl.lattice_api.modules.common.tags.LatticeItemTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DataGeneration {
    public static void onGatherDataEvent(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        PackOutput packOutput = dataGenerator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        BlockTagsProvider blockTagsProvider = new BlockTagGenerator(packOutput, lookupProvider, existingFileHelper);
        dataGenerator.addProvider(event.includeServer(), blockTagsProvider);
        dataGenerator.addProvider(event.includeServer(), new ItemTagGenerator(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));

        dataGenerator.addProvider(event.includeClient(), new ItemModelGenerator(packOutput, existingFileHelper));
    }

    public static class BlockTagGenerator extends BlockTagsProvider {
        public BlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, LatticeApi.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider provider) {
            for (LatticeTag<?> latticeTag : LatticeRegistries.getTags()) {
                if (latticeTag instanceof LatticeBlockTag lBlockTag) {
                    IntrinsicTagAppender<Block> tagAppender = tag(lBlockTag.get());

                    lBlockTag.getTagContent().forEach(blockSupplier -> tagAppender.add(blockSupplier.get()));
                }
            }
        }
    }

    public static class ItemTagGenerator extends ItemTagsProvider {
        public ItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, blockTags, LatticeApi.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider provider) {
            for (LatticeTag<?> latticeTag : LatticeRegistries.getTags()) {
                if (latticeTag instanceof LatticeItemTag lItemTag) {
                    IntrinsicTagAppender<Item> tagAppender = tag(lItemTag.get());

                    lItemTag.getTagContent().forEach(itemSupplier -> tagAppender.add(itemSupplier.get()));
                }
            }
        }
    }

    public static class ItemModelGenerator extends ItemModelProvider {
        public ItemModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
            super(output, LatticeApi.MOD_ID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            for (LatticeItem<?> latticeItem : LatticeRegistries.getItems()) {
                Item item = latticeItem.get();
                LatticeItemModelType modelType = latticeItem.getModelType();

                if (modelType == LatticeRegistries.ModelTypes.Item.BASIC) basicItem(item);
                else if (modelType == LatticeRegistries.ModelTypes.Item.HANDHELD) handheldItem(item);
            }
        }
    }

    public static void addListeners(@NotNull IEventBus modEventBus) {
        modEventBus.addListener(DataGeneration::onGatherDataEvent);
    }
}
