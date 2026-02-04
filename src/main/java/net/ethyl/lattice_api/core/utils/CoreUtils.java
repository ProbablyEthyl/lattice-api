package net.ethyl.lattice_api.core.utils;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CoreUtils {
    public static boolean isInvalidId(@NotNull RegistryId registryId) {
        return !registryId.toString().matches("[a-z_:]+");
    }

    public static void setBasicDescription(@NotNull ItemStack itemStack, @NotNull List<Component> tooltipComponents) {
        String[] description = Component.translatable(itemStack.getDescriptionId() + ".lore").getString().split("\n");

        for (String descriptionEntry : description) tooltipComponents.add(Component.literal(descriptionEntry));
    }

    public static String camelToSnakeCase(@NotNull String input) {
        StringBuilder result = new StringBuilder();

        for (char c : input.toCharArray()) {
            result.append(Character.isUpperCase(c) ? "_" + Character.toLowerCase(c) : c);
        }

        return result.toString();
    }
}
