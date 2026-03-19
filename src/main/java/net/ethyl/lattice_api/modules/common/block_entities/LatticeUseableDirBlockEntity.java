package net.ethyl.lattice_api.modules.common.block_entities;

import net.ethyl.lattice_api.core.content.block_entities.blocks.UseableDirBEBlock;
import net.ethyl.lattice_api.core.content.block_entities.entities.UseableDirBlockEntity;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.base.LatticeBlockEntity;
import net.ethyl.lattice_api.modules.common.blocks.LatticeUseableDirBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeUseableDirBlockEntity extends LatticeBlockEntity<UseableDirBEBlock, UseableDirBlockEntity> {
    protected LatticeUseableDirBlockEntity(@NotNull RegistryId registryId, @NotNull DeferredBlock<UseableDirBEBlock> deferredBlock, @NotNull AppendableBuilder<? extends LatticeUseableDirBlockEntity, ?, ?> builder) {
        super(registryId, deferredBlock, builder);
    }

    public static AppendableBuilder<? extends LatticeUseableDirBlockEntity, ?, ?> builder() {
        return new AppendableBuilder<>(LatticeUseableDirBlockEntity::new, UseableDirBEBlock::new, LatticeUseableDirBlock.builder(), UseableDirBlockEntity::new);
    }

    public static class AppendableBuilder<I extends LatticeUseableDirBlockEntity, BB extends LatticeUseableDirBlock.AppendableBuilder<? extends LatticeBlock<?>, ?>, B extends AppendableBuilder<I, BB, B>> extends LatticeBlockEntity.AppendableBuilder<UseableDirBEBlock, UseableDirBlockEntity, I, BB, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<UseableDirBEBlock>, B, I> latticeFactory, @NotNull Function<B, UseableDirBEBlock> blockFactory, @NotNull BB blockBuilder, @NotNull TriFunction<BlockEntityType<UseableDirBlockEntity>, BlockPos, BlockState, UseableDirBlockEntity> blockEntityFactory) {
            super(latticeFactory, blockFactory, blockBuilder, blockEntityFactory);
        }
    }
}
