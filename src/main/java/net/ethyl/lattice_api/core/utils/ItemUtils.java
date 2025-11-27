package net.ethyl.lattice_api.core.utils;

import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.common.items.LatticeBasicItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemUtils {
    public static void setBasicDescription(@NotNull ItemStack itemStack, @NotNull List<Component> tooltipComponents) {
        String[] description = Component.translatable(itemStack.getDescriptionId() + ".lore").getString().split("\n");

        for (String descriptionEntry : description) tooltipComponents.add(Component.literal(descriptionEntry));
    }

    public static Item.Properties getItemProperties(@NotNull LatticeBasicItem.Builder builder) {
        return getBaseItemProperties(builder);
    }

    private static <T extends Item> Item.Properties getBaseItemProperties(@NotNull LatticeItem.Builder<T, ? extends LatticeItem<T>, ?> builder) {
        Item.Properties itemProperties = new Item.Properties();
        itemProperties.stacksTo(builder.stackSize);

        if (builder.fireResistant) itemProperties.fireResistant();

        return itemProperties;
    }
}
