package net.ethyl.lattice_api.core.content.items.equipment.armor;

import net.ethyl.lattice_api.core.utils.utility.CoreUtils;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.common.items.equipment.armor.LatticeBasicHelmet;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BasicHelmet extends ArmorItem {
    private final boolean hasDescription;

    public BasicHelmet(@NotNull LatticeBasicHelmet.AppendableBuilder<? extends LatticeItem<BasicHelmet>, ?> builder) {
        super(builder.getArmorMaterial(), Type.HELMET, builder.getItemProperties());
        this.hasDescription = builder.getHasDescription();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        if (this.hasDescription) CoreUtils.setBasicDescription(itemStack, tooltipComponents);

        super.appendHoverText(itemStack, tooltipContext, tooltipComponents, tooltipFlag);
    }
}
