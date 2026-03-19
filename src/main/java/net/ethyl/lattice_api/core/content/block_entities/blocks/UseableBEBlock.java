package net.ethyl.lattice_api.core.content.block_entities.blocks;

import net.ethyl.lattice_api.core.content.block_entities.entities.UseableBlockEntity;
import net.ethyl.lattice_api.core.content.blocks.UseableBlock;
import net.ethyl.lattice_api.core.utils.function.QuadConsumer;
import net.ethyl.lattice_api.modules.common.block_entities.LatticeUseableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class UseableBEBlock extends UseableBlock implements EntityBlock {
    private final Supplier<BlockEntityType<UseableBlockEntity>> blockEntityType;
    private final RenderShape renderShape;
    private final QuadConsumer<Level, BlockPos, BlockState, BlockEntity> onClientTick;

    public UseableBEBlock(@NotNull LatticeUseableBlockEntity.AppendableBuilder<? extends LatticeUseableBlockEntity, ?, ?> builder) {
        super(builder.getBlockBuilder());
        this.blockEntityType = builder.getType();
        this.renderShape = builder.getRenderShape();
        this.onClientTick = builder.getOnClientTick();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new UseableBlockEntity(this.blockEntityType.get(), blockPos, blockState);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
        return this.renderShape;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        if (level.isClientSide() && this.onClientTick != null) {
            return this.onClientTick::accept;
        }

        return null;
    }
}
