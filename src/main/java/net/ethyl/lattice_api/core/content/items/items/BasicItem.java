package net.ethyl.lattice_api.core.content.items.items;

import net.ethyl.lattice_api.core.utils.CoreUtils;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.common.items.items.LatticeBasicItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BasicItem extends Item {
    private final boolean hasDescription;

    public BasicItem(@NotNull LatticeBasicItem.AppendableBuilder<? extends LatticeItem<Item>, ?> builder) {
        super(builder.getItemProperties());
        this.hasDescription = builder.getHasDescription();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        if (this.hasDescription) CoreUtils.setBasicDescription(itemStack, tooltipComponents);

        super.appendHoverText(itemStack, tooltipContext, tooltipComponents, tooltipFlag);
    }
}