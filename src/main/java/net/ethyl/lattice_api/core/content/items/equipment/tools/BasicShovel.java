package net.ethyl.lattice_api.core.content.items.equipment.tools;

import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.common.items.equipment.tools.LatticeBasicShovel;
import net.minecraft.world.item.ShovelItem;
import org.jetbrains.annotations.NotNull;

public class BasicShovel extends ShovelItem {
    public BasicShovel(@NotNull LatticeBasicShovel.AppendableBuilder<ShovelItem, ? extends LatticeItem<ShovelItem>, ?> builder) {
        super(builder.getTier(), builder.getItemProperties());
    }
}