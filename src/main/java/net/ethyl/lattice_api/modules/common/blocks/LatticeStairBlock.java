package net.ethyl.lattice_api.modules.common.blocks;

import net.ethyl.lattice_api.core.content.blocks.BasicStair;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.minecraft.world.level.block.StairBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeStairBlock extends LatticeBlock<StairBlock> {
    protected LatticeStairBlock(@NotNull RegistryId registryId, @NotNull DeferredBlock<StairBlock> deferredBlock, @NotNull AppendableBuilder<? extends LatticeBlock<StairBlock>, ?> builder) {
        super(registryId, deferredBlock, builder);
    }

    public static AppendableBuilder<? extends LatticeStairBlock, ?> builder() {
        return new AppendableBuilder<>(LatticeStairBlock::new, BasicStair::new);
    }

    public static class AppendableBuilder<I extends LatticeBlock<StairBlock>, B extends AppendableBuilder<I, B>> extends LatticeBlock.AppendableBuilder<StairBlock, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<StairBlock>, B, I> latticeFactory, @NotNull Function<B, StairBlock> blockFactory) {
            super(latticeFactory, blockFactory);
        }
    }
}
