package net.ethyl.lattice_api.core.content.block_entities.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.ethyl.lattice_api.core.instances.objects.LatticeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class UseableDirBlockEntity extends LatticeBlockEntity {
    public UseableDirBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public void adjustForFacing(@NotNull PoseStack poseStack) {
        poseStack.translate(0.5f, 0.5f, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-this.getBlockState().getValue(HorizontalDirectionalBlock.FACING).toYRot()));
        poseStack.translate(-0.5f, -0.5f, -0.5f);
    }
}
