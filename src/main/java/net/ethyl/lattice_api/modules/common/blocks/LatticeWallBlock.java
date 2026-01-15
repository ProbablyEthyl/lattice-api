package net.ethyl.lattice_api.modules.common.blocks;

import net.ethyl.lattice_api.core.content.blocks.BasicWall;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.minecraft.world.level.block.WallBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeWallBlock extends LatticeBlock<WallBlock> {
    protected LatticeWallBlock(@NotNull RegistryId registryId, @NotNull DeferredBlock<WallBlock> deferredBlock, @NotNull AppendableBuilder<? extends LatticeBlock<WallBlock>, ?> builder) {
        super(registryId, deferredBlock, builder);
    }

    public static AppendableBuilder<? extends LatticeWallBlock, ?> builder() {
        return new AppendableBuilder<>(LatticeWallBlock::new, BasicWall::new);
    }

    public static class AppendableBuilder<I extends LatticeBlock<WallBlock>, B extends AppendableBuilder<I, B>> extends LatticeBlock.AppendableBuilder<WallBlock, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<WallBlock>, B, I> latticeFactory, @NotNull Function<B, WallBlock> blockFactory) {
            super(latticeFactory, blockFactory);
        }
    }
}
