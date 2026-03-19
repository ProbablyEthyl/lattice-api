package net.ethyl.lattice_api.core.content.blocks;

import com.mojang.serialization.MapCodec;
import net.ethyl.lattice_api.core.utils.utility.CoreUtils;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.common.blocks.LatticeDirBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class DirBlock extends HorizontalDirectionalBlock {
    protected static final MapCodec<DirBlock> CODEC = simpleCodec(DirBlock::new);
    private final boolean hasDescription;
    private final float burnChance;
    private final int spreadSpeed;
    protected final Function<BlockPlaceContext, Direction> directionFunction;
    protected final VoxelShape voxelShape;

    public DirBlock(@NotNull LatticeDirBlock.AppendableBuilder<? extends LatticeBlock<?>, ?> builder) {
        super(builder.getBlockProperties());
        this.hasDescription = builder.getHasDescription();
        this.burnChance = builder.getBurnChance();
        this.spreadSpeed = builder.getSpreadSpeed();
        this.directionFunction = builder.getPlacementState();
        this.voxelShape = builder.getVoxelShape();
    }

    public DirBlock(Properties properties) {
        super(properties);
        this.hasDescription = false;
        this.burnChance = 0.0f;
        this.spreadSpeed = 0;
        this.directionFunction = UseOnContext::getHorizontalDirection;
        this.voxelShape = Shapes.block();
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return this.voxelShape;
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, this.directionFunction.apply(blockPlaceContext));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
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
