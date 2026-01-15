package net.ethyl.lattice_api.core.content.items.equipment.tools;

import net.ethyl.lattice_api.core.utils.CoreUtils;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.common.items.equipment.tools.LatticeBasicPickaxe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BasicPickaxe extends PickaxeItem {
    private final boolean hasDescription;

    public BasicPickaxe(@NotNull LatticeBasicPickaxe.AppendableBuilder<? extends LatticeItem<PickaxeItem>, ?> builder) {
        super(builder.getTier(), builder.getItemProperties());
        this.hasDescription = builder.getHasDescription();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        if (this.hasDescription) CoreUtils.setBasicDescription(itemStack, tooltipComponents);

        super.appendHoverText(itemStack, tooltipContext, tooltipComponents, tooltipFlag);
    }
}