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
    protected LatticeBasicBlock(@NotNull RegistryId registryId, @NotNull DeferredBlock<Block> deferredBlock, @NotNull AppendableBuilder<Block, ? extends LatticeBlock<Block>, ?> builder) {
        super(registryId, deferredBlock, builder);
    }

    public static Builder builder() {
        return new Builder(LatticeBasicBlock::new, BasicBlock::new);
    }

    public static class Builder extends AppendableBuilder<Block, LatticeBasicBlock, Builder> {
        protected Builder(@NotNull TriFunction<RegistryId, DeferredBlock<Block>, Builder, LatticeBasicBlock> latticeFactory, @NotNull Function<Builder, Block> blockFactory) {
            super(latticeFactory, blockFactory);
        }
    }

    public static class AppendableBuilder<T extends Block, I extends LatticeBlock<T>, B extends AppendableBuilder<T, I, B>> extends LatticeBlock.AppendableBuilder<T, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<T>, B, I> latticeFactory, @NotNull Function<B, T> blockFactory) {
            super(latticeFactory, blockFactory);
        }
    }
}