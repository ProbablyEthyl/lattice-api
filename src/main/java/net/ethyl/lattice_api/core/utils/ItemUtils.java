package net.ethyl.lattice_api.core.utils;

import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.common.items.LatticeBasicItem;
import net.ethyl.lattice_api.modules.common.items.LatticeUseableItem;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;


public class ItemUtils {
    public static Item.Properties getItemProperties(@NotNull LatticeBasicItem.AppendableBuilder<? extends LatticeItem<Item>, ?> builder) {
        return getBaseItemProperties(builder);
    }

    public static Item.Properties getItemProperties(@NotNull LatticeUseableItem.AppendableBuilder<? extends LatticeItem<Item>, ?> builder) {
        return getBaseItemProperties(builder);
    }

    private static <T extends Item> Item.Properties getBaseItemProperties(@NotNull LatticeItem.AppendableBuilder<T, ? extends LatticeItem<T>, ?> builder) {
        Item.Properties itemProperties = new Item.Properties();
        itemProperties.stacksTo(builder.stackSize);

        if (builder.fireResistant) itemProperties.fireResistant();

        return itemProperties;
    }
}
