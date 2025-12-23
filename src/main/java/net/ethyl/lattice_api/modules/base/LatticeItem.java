package net.ethyl.lattice_api.modules.base;

import net.ethyl.lattice_api.core.data.LatticeRegistries;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.common.types.modelTypes.LatticeItemModelType;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeItem<T extends Item> extends LatticeObject {
    protected final DeferredItem<T> deferredItem;
    protected final LatticeItemModelType modelType;

    protected LatticeItem(@NotNull RegistryId registryId, @NotNull DeferredItem<T> deferredItem, @NotNull LatticeItem.AppendableBuilder<T, ? extends LatticeItem<T>, ?> builder) {
        super(registryId);
        this.deferredItem = deferredItem;
        this.modelType = builder.modelType;
    }

    protected LatticeItem(@NotNull LatticeItem<T> latticeItem) {
        super(latticeItem.getRegistryId());
        this.deferredItem = latticeItem.getDeferred();
        this.modelType = latticeItem.getModelType();
    }

    public DeferredItem<T> getDeferred() {
        return this.deferredItem;
    }

    public T get() {
        return this.getDeferred().get();
    }

    public LatticeItemModelType getModelType() {
        return this.modelType;
    }

    @Override
    public LatticeObject clone() {
        return new LatticeItem<>(this);
    }

    public static class AppendableBuilder<T extends Item, I extends LatticeItem<T>, B extends AppendableBuilder<T, I, B>> {
        private final TriFunction<RegistryId, DeferredItem<T>, B, I> latticeFactory;
        private final Function<B, T> itemFactory;
        private LatticeItemModelType modelType = LatticeRegistries.Types.Item.BASIC;
        protected boolean hasDescription = false;
        protected final Item.Properties itemProperties = new Item.Properties().stacksTo(64);

        @SuppressWarnings("unchecked")
        protected B self() {
            return (B) this;
        }

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredItem<T>, B, I> latticeFactory, @NotNull Function<B, T> itemFactory) {
            this.latticeFactory = latticeFactory;
            this.itemFactory = itemFactory;
        }

        public T generate() {
            return this.itemFactory.apply(this.self());
        }

        public B modelType(@NotNull LatticeItemModelType modelType) {
            this.modelType = modelType;

            return this.self();
        }

        public boolean getHasDescription() {
            return this.hasDescription;
        }

        public B hasDescription() {
            this.hasDescription = true;

            return this.self();
        }

        public Item.Properties getItemProperties() {
            return this.itemProperties;
        }

        public B stackSize(int stackSize) {
            this.itemProperties.stacksTo(stackSize);

            return this.self();
        }

        public B fireResistant() {
            this.itemProperties.fireResistant();

            return this.self();
        }

        public I build(@NotNull RegistryId registryId, @NotNull DeferredItem<T> deferredItem) {
            return this.latticeFactory.apply(registryId, deferredItem, this.self());
        }
    }
}