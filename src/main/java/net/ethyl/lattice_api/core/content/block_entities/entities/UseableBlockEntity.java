package net.ethyl.lattice_api.core.content.block_entities.entities;

import net.ethyl.lattice_api.core.instances.objects.LatticeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class UseableBlockEntity extends LatticeBlockEntity {
    public UseableBlockEntity(@NotNull BlockEntityType<? extends UseableBlockEntity> type, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        super(type, blockPos, blockState);
    }
}
