package net.ethyl.lattice_api.modules.common.blocks;

import net.ethyl.lattice_api.core.content.blocks.BasicSlab;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.base.LatticeObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeSlabBlock extends LatticeBlock<SlabBlock> {
    protected final Supplier<Block> defaultBlock;

    protected LatticeSlabBlock(@NotNull RegistryId registryId, @NotNull DeferredBlock<SlabBlock> deferredBlock, @NotNull AppendableBuilder<SlabBlock, ? extends LatticeBlock<SlabBlock>, ?> builder) {
        super(registryId, deferredBlock, builder);
        this.defaultBlock = builder.defaultBlock;
    }

    protected LatticeSlabBlock(@NotNull LatticeSlabBlock latticeSlabBlock) {
        super(latticeSlabBlock);
        this.defaultBlock = latticeSlabBlock.getDefaultBlock();
    }

    public Supplier<Block> getDefaultBlock() {
        return this.defaultBlock;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public LatticeObject clone() {
        return new LatticeSlabBlock(this);
    }

    public static Builder builder() {
        return new Builder(LatticeSlabBlock::new, BasicSlab::new);
    }

    public static class Builder extends AppendableBuilder<SlabBlock, LatticeSlabBlock, Builder> {
        protected Builder(@NotNull TriFunction<RegistryId, DeferredBlock<SlabBlock>, Builder, LatticeSlabBlock> latticeFactory, @NotNull Function<Builder, SlabBlock> blockFactory) {
            super(latticeFactory, blockFactory);
        }
    }

    public static class AppendableBuilder<T extends Block, I extends LatticeBlock<T>, B extends AppendableBuilder<T, I, B>> extends LatticeStairBlock.AppendableBuilder<T, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<T>, B, I> latticeFactory, @NotNull Function<B, T> blockFactory) {
            super(latticeFactory, blockFactory);
        }
    }
}
