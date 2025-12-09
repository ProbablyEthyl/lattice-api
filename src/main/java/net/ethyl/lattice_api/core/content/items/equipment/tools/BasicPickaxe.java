package net.ethyl.lattice_api.core.content.items.equipment.tools;

import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.common.items.equipment.tools.LatticeBasicPickaxe;
import net.minecraft.world.item.PickaxeItem;
import org.jetbrains.annotations.NotNull;

public class BasicPickaxe extends PickaxeItem {
    public BasicPickaxe(@NotNull LatticeBasicPickaxe.AppendableBuilder<PickaxeItem, ? extends LatticeItem<PickaxeItem>, ?> builder) {
        super(builder.getTier(), builder.getItemProperties());
    }
}