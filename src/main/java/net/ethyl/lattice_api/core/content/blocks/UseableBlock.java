package net.ethyl.lattice_api.core.content.blocks;

import net.ethyl.lattice_api.core.utils.function.HexaConsumer;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.common.blocks.LatticeUseableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class UseableBlock extends BasicBlock {
    private final HexaConsumer<ItemStack, BlockState, Level, BlockPos, Player, BlockHitResult> interact;

    public UseableBlock(@NotNull LatticeUseableBlock.AppendableBuilder<? extends LatticeBlock<?>, ?> builder) {
        super(builder);
        this.interact = builder.getInteract();
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack itemStack, @NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
        if (this.interact != null) {
            this.interact.accept(itemStack, blockState, level, blockPos, player, blockHitResult);
            player.swing(interactionHand, true);
        }

        return ItemInteractionResult.SUCCESS;
    }
}
