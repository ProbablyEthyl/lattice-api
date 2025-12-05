package net.ethyl.lattice_api.modules.common.blocks;

import net.ethyl.lattice_api.core.content.blocks.BasicStair;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeStairBlock extends LatticeBlock<StairBlock> {
    private final Supplier<Block> defaultBlock;

    private LatticeStairBlock(@NotNull RegistryId registryId, @NotNull DeferredBlock<StairBlock> deferredBlock, @NotNull AppendableBuilder<? extends LatticeBlock<StairBlock>, ?> builder) {
        super(registryId, deferredBlock, builder);
        this.defaultBlock = builder.defaultBlock;
    }

    public Supplier<Block> getDefaultBlock() {
        return this.defaultBlock;
    }

    public static Builder builder() {
        return new Builder(LatticeStairBlock::new, BasicStair::new);
    }

    public static class Builder extends AppendableBuilder<LatticeStairBlock, Builder> {
        protected Builder(@NotNull TriFunction<RegistryId, DeferredBlock<StairBlock>, Builder, LatticeStairBlock> latticeFactory, @NotNull Function<Builder, StairBlock> blockFactory) {
            super(latticeFactory, blockFactory);
        }
    }

    public static class AppendableBuilder<I extends LatticeBlock<StairBlock>, B extends AppendableBuilder<I, B>> extends LatticeBlock.AppendableBuilder<StairBlock, I, B> {
        public Supplier<Block> defaultBlock = () -> Blocks.STONE;

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<StairBlock>, B, I> latticeFactory, @NotNull Function<B, StairBlock> blockFactory) {
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
