package net.ethyl.lattice_api.core.content.blocks;

import net.ethyl.lattice_api.core.utils.utility.CoreUtils;
import net.ethyl.lattice_api.modules.common.blocks.LatticeWallBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BasicWall extends WallBlock {
    private final boolean hasDescription;
    private final float burnChance;
    private final int spreadSpeed;
    private final VoxelShape voxelShape;

    public BasicWall(@NotNull LatticeWallBlock.AppendableBuilder<? extends LatticeWallBlock, ?> builder) {
        super(builder.getBlockProperties());
        this.hasDescription = builder.getHasDescription();
        this.burnChance = builder.getBurnChance();
        this.spreadSpeed = builder.getSpreadSpeed();
        this.voxelShape = builder.getVoxelShape();
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return this.voxelShape;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull Item.TooltipContext tooltipContext, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        if (this.hasDescription) CoreUtils.setBasicDescription(itemStack, tooltipComponents);

        super.appendHoverText(itemStack, tooltipContext, tooltipComponents, tooltipFlag);
    }

    @Override
    public boolean isFlammable(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull Direction direction) {
        return blockState.ignitedByLava();
    }

    @Override
    public int getFlammability(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull Direction direction) {
        return (int) (3 * this.burnChance);
    }

    @Override
    public int getFireSpreadSpeed(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull Direction direction) {
        return this.spreadSpeed;
    }
}
