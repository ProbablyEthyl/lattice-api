package net.ethyl.lattice_api.core.content.items.equipment.tools;

import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.common.items.equipment.tools.LatticeBasicAxe;
import net.minecraft.world.item.AxeItem;
import org.jetbrains.annotations.NotNull;

public class BasicAxe extends AxeItem {
    public BasicAxe(@NotNull LatticeBasicAxe.AppendableBuilder<AxeItem, ? extends LatticeItem<AxeItem>, ?> builder) {
        super(builder.getTier(), builder.getItemProperties());
    }
}