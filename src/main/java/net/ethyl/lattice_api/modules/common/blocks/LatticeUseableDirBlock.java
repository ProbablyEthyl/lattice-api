package net.ethyl.lattice_api.modules.common.blocks;

import net.ethyl.lattice_api.core.content.blocks.DirBlock;
import net.ethyl.lattice_api.core.content.blocks.UseableDirBlock;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.core.utils.function.HexaConsumer;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeUseableDirBlock extends LatticeDirBlock {
    protected LatticeUseableDirBlock(@NotNull RegistryId registryId, @NotNull DeferredBlock<DirBlock> deferredBlock, @NotNull AppendableBuilder<? extends LatticeDirBlock, ?> builder) {
        super(registryId, deferredBlock, builder);
    }

    public static AppendableBuilder<? extends LatticeUseableDirBlock, ?> builder() {
        return new AppendableBuilder<>(LatticeUseableDirBlock::new, UseableDirBlock::new);
    }

    public static class AppendableBuilder<I extends LatticeBlock<DirBlock>, B extends AppendableBuilder<I, B>> extends LatticeDirBlock.AppendableBuilder<I, B> {
        private HexaConsumer<ItemStack, BlockState, Level, BlockPos, Player, BlockHitResult> interact = null;

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<DirBlock>, B, I> latticeFactory, @NotNull Function<B, DirBlock> blockFactory) {
            super(latticeFactory, blockFactory);
        }

        public HexaConsumer<ItemStack, BlockState, Level, BlockPos, Player, BlockHitResult> getInteract() {
            return this.interact;
        }

        public B interact(@NotNull HexaConsumer<ItemStack, BlockState, Level, BlockPos, Player, BlockHitResult> interact) {
            this.interact = interact;

            return this.self();
        }
    }
}
