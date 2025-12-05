package net.ethyl.lattice_api.modules.common.blocks;

import net.ethyl.lattice_api.core.content.blocks.BasicSlab;
import net.ethyl.lattice_api.core.content.blocks.BasicStair;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeSlabBlock extends LatticeBlock<SlabBlock> {
    private final Supplier<Block> defaultBlock;

    private LatticeSlabBlock(@NotNull RegistryId registryId, @NotNull DeferredBlock<SlabBlock> deferredBlock, @NotNull AppendableBuilder<? extends LatticeBlock<SlabBlock>, ?> builder) {
        super(registryId, deferredBlock, builder);
        this.defaultBlock = builder.defaultBlock;
    }

    public Supplier<Block> getDefaultBlock() {
        return this.defaultBlock;
    }

    public static Builder builder() {
        return new Builder(LatticeSlabBlock::new, BasicSlab::new);
    }

    public static class Builder extends AppendableBuilder<LatticeSlabBlock, Builder> {
        protected Builder(@NotNull TriFunction<RegistryId, DeferredBlock<SlabBlock>, Builder, LatticeSlabBlock> latticeFactory, @NotNull Function<Builder, SlabBlock> blockFactory) {
            super(latticeFactory, blockFactory);
        }
    }

    public static class AppendableBuilder<I extends LatticeBlock<SlabBlock>, B extends AppendableBuilder<I, B>> extends LatticeBlock.AppendableBuilder<SlabBlock, I, B> {
        public Supplier<Block> defaultBlock = () -> Blocks.STONE;

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<SlabBlock>, B, I> latticeFactory, @NotNull Function<B, SlabBlock> blockFactory) {
            super(latticeFactory, blockFactory);
        }

        public B defaultBlock(@NotNull Block block) {
            this.defaultBlock = () -> block;

            return this.self();
        }

        public B defaultBlock(@NotNull LatticeBlock<?> latticeBlock) {
            this.defaultBlock = latticeBlock::get;

            return this.self();
        }
    }
}
