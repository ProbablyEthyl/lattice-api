package net.ethyl.lattice_api.modules.common.blocks;

import net.ethyl.lattice_api.core.content.blocks.BasicStair;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.base.LatticeObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeStairBlock extends LatticeBlock<StairBlock> {
    protected final Supplier<Block> defaultBlock;

    protected LatticeStairBlock(@NotNull RegistryId registryId, @NotNull DeferredBlock<StairBlock> deferredBlock, @NotNull AppendableBuilder<StairBlock, ? extends LatticeBlock<StairBlock>, ?> builder) {
        super(registryId, deferredBlock, builder);
        this.defaultBlock = builder.defaultBlock;
    }

    private LatticeStairBlock(@NotNull LatticeStairBlock latticeStairBlock) {
        super(latticeStairBlock);
        this.defaultBlock = this.getDefaultBlock();
    }

    public Supplier<Block> getDefaultBlock() {
        return this.defaultBlock;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public LatticeObject clone() {
        return new LatticeStairBlock(this);
    }

    public static Builder builder() {
        return new Builder(LatticeStairBlock::new, BasicStair::new);
    }

    public static class Builder extends AppendableBuilder<StairBlock, LatticeStairBlock, Builder> {
        protected Builder(@NotNull TriFunction<RegistryId, DeferredBlock<StairBlock>, Builder, LatticeStairBlock> latticeFactory, @NotNull Function<Builder, StairBlock> blockFactory) {
            super(latticeFactory, blockFactory);
        }
    }

    public static class AppendableBuilder<T extends Block, I extends LatticeBlock<T>, B extends AppendableBuilder<T, I, B>> extends LatticeBlock.AppendableBuilder<T, I, B> {
        protected Supplier<Block> defaultBlock = () -> Blocks.STONE;

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<T>, B, I> latticeFactory, @NotNull Function<B, T> blockFactory) {
            super(latticeFactory, blockFactory);
        }

        public Supplier<Block> getDefaultBlock() {
            return this.defaultBlock;
        }

        public B defaultBlock(@NotNull LatticeBlock<?> latticeBlock) {
            this.defaultBlock = latticeBlock::get;

            return this.self();
        }

        public B defaultBlock(@NotNull Block block) {
            this.defaultBlock = () -> block;

            return this.self();
        }
    }
}
