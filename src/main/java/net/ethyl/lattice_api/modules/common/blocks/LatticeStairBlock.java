package net.ethyl.lattice_api.modules.common.blocks;

import net.ethyl.lattice_api.core.content.blocks.BasicStair;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeStairBlock extends LatticeBlock<BasicStair> {
    protected LatticeStairBlock(@NotNull RegistryId registryId, @NotNull DeferredBlock<BasicStair> deferredBlock, @NotNull AppendableBuilder<? extends LatticeStairBlock, ?> builder) {
        super(registryId, deferredBlock, builder);
    }

    public static AppendableBuilder<? extends LatticeStairBlock, ?> builder() {
        return new AppendableBuilder<>(LatticeStairBlock::new, BasicStair::new);
    }

    public static class AppendableBuilder<I extends LatticeStairBlock, B extends AppendableBuilder<I, B>> extends LatticeBlock.AppendableBuilder<BasicStair, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<BasicStair>, B, I> latticeFactory, @NotNull Function<B, BasicStair> blockFactory) {
            super(latticeFactory, blockFactory);
        }
    }
}
