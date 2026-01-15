package net.ethyl.lattice_api.modules.common.blocks;

import net.ethyl.lattice_api.core.content.blocks.BasicSlab;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeSlabBlock extends LatticeBlock<SlabBlock> {
    protected LatticeSlabBlock(@NotNull RegistryId registryId, @NotNull DeferredBlock<SlabBlock> deferredBlock, @NotNull AppendableBuilder<? extends LatticeBlock<SlabBlock>, ?> builder) {
        super(registryId, deferredBlock, builder);
    }

    public static AppendableBuilder<? extends LatticeSlabBlock, ?> builder() {
        return new AppendableBuilder<>(LatticeSlabBlock::new, BasicSlab::new);
    }

    public static class AppendableBuilder<I extends LatticeBlock<SlabBlock>, B extends AppendableBuilder<I, B>> extends LatticeBlock.AppendableBuilder<SlabBlock, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<SlabBlock>, B, I> latticeFactory, @NotNull Function<B, SlabBlock> blockFactory) {
            super(latticeFactory, blockFactory);
        }
    }
}
