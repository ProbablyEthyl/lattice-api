package net.ethyl.lattice_api.modules.common.blocks;

import net.ethyl.lattice_api.core.content.blocks.BasicWall;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.base.LatticeObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeWallBlock extends LatticeBlock<WallBlock> {
    protected final Supplier<Block> defaultBlock;

    protected LatticeWallBlock(@NotNull RegistryId registryId, @NotNull DeferredBlock<WallBlock> deferredBlock, @NotNull AppendableBuilder<WallBlock, ? extends LatticeBlock<WallBlock>, ?> builder) {
        super(registryId, deferredBlock, builder);
        this.defaultBlock = builder.defaultBlock;
    }

    protected LatticeWallBlock(@NotNull LatticeWallBlock latticeWallBlock) {
        super(latticeWallBlock);
        this.defaultBlock = latticeWallBlock.getDefaultBlock();
    }

    public Supplier<Block> getDefaultBlock() {
        return this.defaultBlock;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public LatticeObject clone() {
        return new LatticeWallBlock(this);
    }

    public static Builder builder() {
        return new Builder(LatticeWallBlock::new, BasicWall::new);
    }

    public static class Builder extends AppendableBuilder<WallBlock, LatticeWallBlock, Builder> {
        protected Builder(@NotNull TriFunction<RegistryId, DeferredBlock<WallBlock>, Builder, LatticeWallBlock> latticeFactory, @NotNull Function<Builder, WallBlock> blockFactory) {
            super(latticeFactory, blockFactory);
        }
    }

    public static class AppendableBuilder<T extends Block, I extends LatticeBlock<T>, B extends AppendableBuilder<T, I, B>> extends LatticeStairBlock.AppendableBuilder<T, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<T>, B, I> latticeFactory, @NotNull Function<B, T> blockFactory) {
            super(latticeFactory, blockFactory);
        }
    }
}
