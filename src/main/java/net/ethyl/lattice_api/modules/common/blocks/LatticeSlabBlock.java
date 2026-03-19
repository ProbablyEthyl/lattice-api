package net.ethyl.lattice_api.modules.common.blocks;

import net.ethyl.lattice_api.core.content.blocks.BasicSlab;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeSlabBlock extends LatticeBlock<BasicSlab> {
    protected LatticeSlabBlock(@NotNull RegistryId registryId, @NotNull DeferredBlock<BasicSlab> deferredBlock, @NotNull AppendableBuilder<? extends LatticeSlabBlock, ?> builder) {
        super(registryId, deferredBlock, builder);
    }

    public static AppendableBuilder<? extends LatticeSlabBlock, ?> builder() {
        return new AppendableBuilder<>(LatticeSlabBlock::new, BasicSlab::new);
    }

    public static class AppendableBuilder<I extends LatticeSlabBlock, B extends AppendableBuilder<I, B>> extends LatticeBlock.AppendableBuilder<BasicSlab, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<BasicSlab>, B, I> latticeFactory, @NotNull Function<B, BasicSlab> blockFactory) {
            super(latticeFactory, blockFactory);
        }
    }
}
