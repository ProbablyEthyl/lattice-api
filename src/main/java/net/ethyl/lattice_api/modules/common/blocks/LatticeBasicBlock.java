package net.ethyl.lattice_api.modules.common.blocks;

import net.ethyl.lattice_api.core.content.blocks.BasicBlock;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeBasicBlock extends LatticeBlock<Block> {
    protected LatticeBasicBlock(@NotNull RegistryId registryId, @NotNull DeferredBlock<Block> deferredBlock, @NotNull AppendableBuilder<? extends LatticeBlock<Block>, ?> builder) {
        super(registryId, deferredBlock, builder);
    }

    public static AppendableBuilder<? extends LatticeBasicBlock, ?> builder() {
        return new AppendableBuilder<>(LatticeBasicBlock::new, BasicBlock::new);
    }

    public static class AppendableBuilder<I extends LatticeBlock<Block>, B extends AppendableBuilder<I, B>> extends LatticeBlock.AppendableBuilder<Block, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<Block>, B, I> latticeFactory, @NotNull Function<B, Block> blockFactory) {
            super(latticeFactory, blockFactory);
        }

        public B defaultBlockSelf() {
            this.defaultBlock = null;

            return this.self();
        }
    }
}