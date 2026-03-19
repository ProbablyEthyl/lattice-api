package net.ethyl.lattice_api.modules.common.blocks;

import net.ethyl.lattice_api.core.content.blocks.BasicBlock;
import net.ethyl.lattice_api.core.content.blocks.UseableBlock;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.core.utils.function.HexaConsumer;
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

public class LatticeUseableBlock extends LatticeBasicBlock {
    protected LatticeUseableBlock(@NotNull RegistryId registryId, @NotNull DeferredBlock<BasicBlock> deferredBlock, @NotNull AppendableBuilder<? extends LatticeBasicBlock, ?> builder) {
        super(registryId, deferredBlock, builder);
    }

    public static AppendableBuilder<? extends LatticeUseableBlock, ?> builder() {
        return new AppendableBuilder<>(LatticeUseableBlock::new, UseableBlock::new);
    }

    public static class AppendableBuilder<I extends LatticeUseableBlock, B extends AppendableBuilder<I, B>> extends LatticeBasicBlock.AppendableBuilder<I, B> {
        private HexaConsumer<ItemStack, BlockState, Level, BlockPos, Player, BlockHitResult> interact = null;

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<BasicBlock>, B, I> latticeFactory, @NotNull Function<B, BasicBlock> blockFactory) {
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