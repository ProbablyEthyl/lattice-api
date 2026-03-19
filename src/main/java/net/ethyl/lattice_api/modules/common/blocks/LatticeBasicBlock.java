package net.ethyl.lattice_api.modules.common.blocks;

import net.ethyl.lattice_api.core.content.blocks.BasicBlock;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeBasicBlock extends LatticeBlock<BasicBlock> {
    protected LatticeBasicBlock(@NotNull RegistryId registryId, @NotNull DeferredBlock<BasicBlock> deferredBlock, @NotNull AppendableBuilder<? extends LatticeBasicBlock, ?> builder) {
        super(registryId, deferredBlock, builder);
    }

    public static AppendableBuilder<? extends LatticeBasicBlock, ?> builder() {
        return new AppendableBuilder<>(LatticeBasicBlock::new, BasicBlock::new);
    }

    public static class AppendableBuilder<I extends LatticeBasicBlock, B extends AppendableBuilder<I, B>> extends LatticeBlock.AppendableBuilder<BasicBlock, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<BasicBlock>, B, I> latticeFactory, @NotNull Function<B, BasicBlock> blockFactory) {
            super(latticeFactory, blockFactory);
        }

        public B defaultBlockSelf() {
            this.defaultBlock = null;

            return this.self();
        }
    }
}