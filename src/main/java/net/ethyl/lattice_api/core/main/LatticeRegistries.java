package net.ethyl.lattice_api.core.main;

import net.ethyl.lattice_api.LatticeApi;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.core.utils.RegistryUtils;
import net.ethyl.lattice_api.modules.base.*;
import net.ethyl.lattice_api.modules.common.modelTypes.item.LatticeItemModelType;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;

public class LatticeRegistries {
    private static final Collection<LatticeTag<?>> tags = new LinkedList<>();
    private static final Collection<LatticeModelType> modelTypes = new LinkedList<>();
    private static final Collection<LatticeItem<?>> items = new LinkedList<>();

    public static Tags createTags(@NotNull String modId) {
        return new Tags(modId);
    }

    public static ModelTypes createModelTypes(@NotNull String modId) {
        return new ModelTypes(modId);
    }

    public static Items createItems(@NotNull String modId) {
        return new Items(modId);
    }

    public static class Tags extends LatticeRegistry<LatticeTag<?>> {
        private Tags(@NotNull String modId) {
            super(modId);
        }

        public <T, I extends LatticeTag<T>, B extends LatticeTag.Builder<T, I, B>> I register(@NotNull String id, @NotNull B builder) {
            RegistryId registryId = createRegistryId(this.modId, id);
            checkDuplicate(this.registryContent, registryId);
            I latticeTag = builder.build(registryId);
            this.registryContent.add(latticeTag);

            return latticeTag;
        }

        @Override
        public void register(@NotNull IEventBus modEventBus) {
            this.registryContent.forEach(latticeTag -> checkDuplicate(tags, latticeTag.getRegistryId()));

            tags.addAll(this.registryContent);
        }
    }

    public static class ModelTypes extends LatticeRegistry<LatticeModelType> {
        private static final ModelTypes BUILT_IN = createModelTypes(LatticeApi.MOD_ID);

        private static final LatticeItemModelType BASIC_ITEM = BUILT_IN.register("basic_item", LatticeItemModelType.builder());
        private static final LatticeItemModelType HANDHELD_ITEM = BUILT_IN.register("handheld_item", LatticeItemModelType.builder());

        public static class Item {
            public static final LatticeItemModelType BASIC = ModelTypes.BASIC_ITEM;
            public static final LatticeItemModelType HANDHELD = ModelTypes.HANDHELD_ITEM;
        }

        private ModelTypes(@NotNull String modId) {
            super(modId);
        }

        public <I extends LatticeModelType, B extends LatticeModelType.Builder<I>> I register(@NotNull String id, @NotNull B builder) {
            RegistryId registryId = createRegistryId(this.modId, id);
            checkDuplicate(this.registryContent, registryId);
            I latticeModelType = builder.build(registryId);
            this.registryContent.add(latticeModelType);

            return latticeModelType;
        }

        @Override
        public void register(@NotNull IEventBus modEventBus) {
            this.registryContent.forEach(latticeModelType -> checkDuplicate(modelTypes, latticeModelType.getRegistryId()));

            modelTypes.addAll(this.registryContent);
        }
    }

    public static class Items extends LatticeRegistry<LatticeItem<?>> {
        private final DeferredRegister.Items ITEMS;

        private Items(@NotNull String modId) {
            super(modId);
            this.ITEMS = DeferredRegister.createItems(modId);
        }

        public <T extends Item, I extends LatticeItem<T>, B extends LatticeItem.Builder<T, I, B>> I register(@NotNull String id, @NotNull B builder) {
            RegistryId registryId = createRegistryId(this.modId, id);
            checkDuplicate(this.registryContent, registryId);
            I latticeItem = builder.build(registryId, ITEMS.register(id, builder::generate));
            this.registryContent.add(latticeItem);

            return latticeItem;
        }

        @Override
        public void register(@NotNull IEventBus modEventBus) {
            this.registryContent.forEach(latticeItem -> checkDuplicate(items, latticeItem.getRegistryId()));

            this.ITEMS.register(modEventBus);
            items.addAll(this.registryContent);
        }
    }

    public static Collection<LatticeTag<?>> getTags() {
        return new LinkedList<>(tags);
    }

    public static Collection<LatticeItem<?>> getItems() {
        return new LinkedList<>(items);
    }

    private static RegistryId createRegistryId(@NotNull String namespace, @NotNull String path) {
        return RegistryId.create(namespace, path);
    }

    private static void checkDuplicate(@NotNull Collection<? extends LatticeObject> collection, @NotNull RegistryId registryId) {
        RegistryUtils.checkDuplicate(collection, registryId);
    }

    public static void register(@NotNull IEventBus modEventBus) {
        ModelTypes.BUILT_IN.register(modEventBus);
    }
}