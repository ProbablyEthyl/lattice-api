package net.ethyl.lattice_api.core.content.blocks;

import net.ethyl.lattice_api.core.utils.CoreUtils;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.common.blocks.LatticeStairBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.StairBlock;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BasicStair extends StairBlock {
    private final boolean hasDescription;

    public BasicStair(@NotNull LatticeStairBlock.AppendableBuilder<StairBlock, ? extends LatticeBlock<StairBlock>, ?> builder) {
        super(builder.defaultBlock.get().defaultBlockState(), builder.blockProperties);
        this.hasDescription = builder.hasDescription;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull Item.TooltipContext tooltipContext, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        if (this.hasDescription) CoreUtils.setBasicDescription(itemStack, tooltipComponents);

        super.appendHoverText(itemStack, tooltipContext, tooltipComponents, tooltipFlag);
    }
}
