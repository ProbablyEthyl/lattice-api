package net.ethyl.lattice_api.modules.common.blocks;

import net.ethyl.lattice_api.core.content.blocks.DirBlock;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeDirBlock extends LatticeBlock<DirBlock> {
    protected LatticeDirBlock(@NotNull RegistryId registryId, @NotNull DeferredBlock<DirBlock> deferredBlock, @NotNull AppendableBuilder<? extends LatticeDirBlock, ?> builder) {
        super(registryId, deferredBlock, builder);
    }

    public static AppendableBuilder<? extends LatticeDirBlock, ?> builder() {
        return new AppendableBuilder<>(LatticeDirBlock::new, DirBlock::new);
    }

    public static class AppendableBuilder<I extends LatticeBlock<DirBlock>, B extends AppendableBuilder<I, B>> extends LatticeBlock.AppendableBuilder<DirBlock, I, B> {
        private Function<BlockPlaceContext, Direction> placementState = UseOnContext::getHorizontalDirection;

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<DirBlock>, B, I> latticeFactory, @NotNull Function<B, DirBlock> blockFactory) {
            super(latticeFactory, blockFactory);
        }

        public B placementState(@NotNull Function<BlockPlaceContext, Direction> placementState) {
            this.placementState = placementState;

            return this.self();
        }

        public Function<BlockPlaceContext, Direction> getPlacementState() {
            return this.placementState;
        }
    }
}
