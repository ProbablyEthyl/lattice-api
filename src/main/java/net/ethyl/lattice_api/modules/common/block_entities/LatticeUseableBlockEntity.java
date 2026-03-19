package net.ethyl.lattice_api.modules.common.block_entities;

import net.ethyl.lattice_api.core.content.block_entities.blocks.UseableBEBlock;
import net.ethyl.lattice_api.core.content.block_entities.entities.UseableBlockEntity;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.base.LatticeBlockEntity;
import net.ethyl.lattice_api.modules.common.blocks.LatticeUseableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeUseableBlockEntity extends LatticeBlockEntity<UseableBEBlock, UseableBlockEntity> {
    protected LatticeUseableBlockEntity(@NotNull RegistryId registryId, @NotNull DeferredBlock<UseableBEBlock> deferredBlock, @NotNull AppendableBuilder<? extends LatticeUseableBlockEntity, ?, ?> builder) {
        super(registryId, deferredBlock, builder);
    }

    public static AppendableBuilder<? extends LatticeUseableBlockEntity, ?, ?> builder() {
        return new AppendableBuilder<>(LatticeUseableBlockEntity::new, UseableBEBlock::new, LatticeUseableBlock.builder(), UseableBlockEntity::new);
    }

    public static class AppendableBuilder<I extends LatticeUseableBlockEntity, BB extends LatticeUseableBlock.AppendableBuilder<? extends LatticeBlock<?>, ?>, B extends AppendableBuilder<I, BB, B>> extends LatticeBlockEntity.AppendableBuilder<UseableBEBlock, UseableBlockEntity, I, BB, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<UseableBEBlock>, B, I> latticeFactory, @NotNull Function<B, UseableBEBlock> blockFactory, @NotNull BB blockBuilder, @NotNull TriFunction<BlockEntityType<UseableBlockEntity>, BlockPos, BlockState, UseableBlockEntity> blockEntityFactory) {
            super(latticeFactory, blockFactory, blockBuilder, blockEntityFactory);
        }
    }
}
