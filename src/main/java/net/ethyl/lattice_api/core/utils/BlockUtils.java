package net.ethyl.lattice_api.core.utils;

import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.common.blocks.LatticeBasicBlock;
import net.ethyl.lattice_api.modules.common.blocks.LatticeSlabBlock;
import net.ethyl.lattice_api.modules.common.blocks.LatticeStairBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

public class BlockUtils {
    public static BlockBehaviour.Properties getBlockProperties(@NotNull LatticeBasicBlock.AppendableBuilder<? extends LatticeBlock<Block>, ?> builder) {
        return getBaseBlockProperties(builder);
    }

    public static BlockBehaviour.Properties getBlockProperties(@NotNull LatticeStairBlock.AppendableBuilder<? extends LatticeBlock<StairBlock>, ?> builder) {
        return getBaseBlockProperties(builder);
    }

    public static BlockBehaviour.Properties getBlockProperties(@NotNull LatticeSlabBlock.AppendableBuilder<? extends LatticeBlock<SlabBlock>, ?> builder) {
        return getBaseBlockProperties(builder);
    }

    private static <T extends Block> BlockBehaviour.Properties getBaseBlockProperties(@NotNull LatticeBlock.AppendableBuilder<T, ? extends LatticeBlock<T>, ?> builder) {
        BlockBehaviour.Properties blockProperties = BlockBehaviour.Properties.of().strength(builder.strength);

        if (builder.instaBreak) blockProperties.instabreak();

        return blockProperties;
    }

    public static <T extends Block> Item.Properties getBlockItemProperties(@NotNull LatticeBlock.AppendableBuilder<T, ? extends LatticeBlock<T>, ?> builder) {
        Item.Properties blockItemProperties = new Item.Properties();

        blockItemProperties.stacksTo(builder.stackSize);

        return blockItemProperties;
    }
}
