package net.ethyl.lattice_api.core.content.items.equipment.weapons;

import net.ethyl.lattice_api.core.utils.CoreUtils;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.common.items.equipment.weapons.LatticeBasicSword;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BasicSword extends SwordItem {
    public final boolean hasDescription;

    public BasicSword(@NotNull LatticeBasicSword.AppendableBuilder<? extends LatticeItem<SwordItem>, ?> builder) {
        super(builder.getTier(), builder.getItemProperties());
        this.hasDescription = builder.getHasDescription();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        if (this.hasDescription) CoreUtils.setBasicDescription(itemStack, tooltipComponents);

        super.appendHoverText(itemStack, tooltipContext, tooltipComponents, tooltipFlag);
    }
}
