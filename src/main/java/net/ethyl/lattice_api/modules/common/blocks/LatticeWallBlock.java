package net.ethyl.lattice_api.modules.common.blocks;

import net.ethyl.lattice_api.core.content.blocks.BasicWall;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeWallBlock extends LatticeBlock<BasicWall> {
    protected LatticeWallBlock(@NotNull RegistryId registryId, @NotNull DeferredBlock<BasicWall> deferredBlock, @NotNull AppendableBuilder<? extends LatticeWallBlock, ?> builder) {
        super(registryId, deferredBlock, builder);
    }

    public static AppendableBuilder<? extends LatticeWallBlock, ?> builder() {
        return new AppendableBuilder<>(LatticeWallBlock::new, BasicWall::new);
    }

    public static class AppendableBuilder<I extends LatticeWallBlock, B extends AppendableBuilder<I, B>> extends LatticeBlock.AppendableBuilder<BasicWall, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<BasicWall>, B, I> latticeFactory, @NotNull Function<B, BasicWall> blockFactory) {
            super(latticeFactory, blockFactory);
        }
    }
}
