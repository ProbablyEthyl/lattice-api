package net.ethyl.lattice_api.core.utils.utility;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.TrimMaterial;
import org.jetbrains.annotations.NotNull;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CoreUtils {
    public static boolean isInvalidId(@NotNull RegistryId registryId) {
        return !registryId.toString().matches("[a-z_:]+");
    }

    public static void setBasicDescription(@NotNull ItemStack itemStack, @NotNull List<Component> tooltipComponents) {
        String[] description = Component.translatable(itemStack.getDescriptionId() + ".lore").getString().split("\n");

        for (String descriptionEntry : description) tooltipComponents.add(Component.literal(descriptionEntry));
    }

    public static ResourceKey<TrimMaterial> toResourceKey(@NotNull TrimMaterial trimMaterial) {
        return Holder.direct(trimMaterial).unwrapKey().orElseThrow();
    }

    public static String camelToSnakeCase(@NotNull String input) {
        StringBuilder result = new StringBuilder();

        for (char c : input.toCharArray()) {
            result.append(Character.isUpperCase(c) ? "_" + Character.toLowerCase(c) : c);
        }

        return result.toString();
    }

    public static JsonObject getJson(@NotNull ResourceLocation resourceLocation, String errorMessage) {
        try (Reader reader = new InputStreamReader(Minecraft.getInstance().getResourceManager().getResource(resourceLocation).orElseThrow().open())) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (Exception error) {
            throw new RuntimeException(errorMessage + ": " + resourceLocation, error);
        }
    }

    public static float degToRad(float degrees) {
        return (float) (degrees * Math.PI / 180.0f);
    }

    public static float[] toFloatArray(@NotNull JsonArray jsonArray) {
        float[] floatArray = new float[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            floatArray[i] = jsonArray.get(i).getAsFloat();
        }

        return floatArray;
    }

    public static float[] toFloatArray(float... floats) {
        return floats;
    }

    public static ResourceLocation toAssetLoc(@NotNull String extension, @NotNull RegistryId registryId, @NotNull String... path) {
        return ResourceLocation.fromNamespaceAndPath(registryId.getNamespace(), Arrays.stream(path).collect(Collectors.joining("/", "", "/")) + registryId.getPath() + extension);
    }
}
