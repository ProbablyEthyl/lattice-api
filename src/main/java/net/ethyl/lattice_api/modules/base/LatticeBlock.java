package net.ethyl.lattice_api.modules.base;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.core.main.LatticeRegistries;
import net.ethyl.lattice_api.modules.common.modelTypes.LatticeBlockModelType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeBlock<T extends Block> extends LatticeObject {
    private final DeferredBlock<T> deferredBlock;
    private final LatticeBlockModelType modelType;

    protected LatticeBlock(@NotNull RegistryId registryId, @NotNull DeferredBlock<T> deferredBlock, @NotNull LatticeBlock.AppendableBuilder<T, ? extends LatticeBlock<T>, ?> builder) {
        super(registryId);
        this.deferredBlock = deferredBlock;
        this.modelType = builder.modelType;
    }

    public DeferredBlock<T> getDeferred() {
        return this.deferredBlock;
    }

    public T get() {
        return this.getDeferred().get();
    }

    public Item asItem() {
        return this.getDeferred().asItem();
    }

    public LatticeBlockModelType getModelType() {
        return this.modelType;
    }

    public static class AppendableBuilder<T extends Block, I extends LatticeBlock<T>, B extends AppendableBuilder<T, I, B>> {
        private final TriFunction<RegistryId, DeferredBlock<T>, B, I> latticeFactory;
        private final Function<B, T> blockFactory;
        private LatticeBlockModelType modelType = LatticeRegistries.ModelTypes.Block.BASIC;
        public boolean hasDescription = false;
        public int stackSize = 64;
        public float strength = 1f;
        public boolean instaBreak = false;

        @SuppressWarnings("unchecked")
        protected B self() {
            return (B) this;
        }

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<T>, B, I> latticeFactory, @NotNull Function<B, T> blockFactory) {
            this.latticeFactory = latticeFactory;
            this.blockFactory = blockFactory;
        }

        public T generate() {
            return this.blockFactory.apply(this.self());
        }

        public B modelType(@NotNull LatticeBlockModelType modelType) {
            this.modelType = modelType;

            return this.self();
        }

        public B hasDescription() {
            this.hasDescription = true;

            return this.self();
        }

        public B stackSize(int stackSize) {
            this.stackSize = stackSize;

            return this.self();
        }

        public B strength(float strength) {
            this.strength = strength;

            return this.self();
        }

        public B instaBreak() {
            this.instaBreak = true;

            return this.self();
        }

        public I build(@NotNull RegistryId registryId, @NotNull DeferredBlock<T> deferredBlock) {
            return this.latticeFactory.apply(registryId, deferredBlock, this.self());
        }
    }
}
