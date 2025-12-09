package net.ethyl.lattice_api.core.content.items.equipment.tools;

import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.common.items.equipment.tools.LatticeBasicHoe;
import net.minecraft.world.item.HoeItem;
import org.jetbrains.annotations.NotNull;

public class BasicHoe extends HoeItem {
    public BasicHoe(@NotNull LatticeBasicHoe.AppendableBuilder<HoeItem, ? extends LatticeItem<HoeItem>, ?> builder) {
        super(builder.getTier(), builder.getItemProperties());
    }
}