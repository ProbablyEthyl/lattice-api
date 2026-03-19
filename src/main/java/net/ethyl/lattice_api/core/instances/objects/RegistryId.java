package net.ethyl.lattice_api.core.instances.objects;

import net.ethyl.lattice_api.core.utils.utility.CoreUtils;
import net.ethyl.lattice_api.core.utils.utility.ErrUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class RegistryId {
    private final String namespace;
    private final String path;
    private final ResourceLocation resourceLocation;

    private RegistryId(@NotNull String namespace, @NotNull String path) {
        this.namespace = namespace;
        this.path = path;

        if (CoreUtils.isInvalidId(this)) ErrUtils.invalidIdErr(this);

        this.resourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String getPath() {
        return this.path;
    }

    public String toString() {
        return this.getNamespace() + ":" + this.getPath();
    }

    public ResourceLocation toResourceLoc() {
        return this.resourceLocation;
    }

    public static RegistryId create(@NotNull String namespace, @NotNull String path) {
        return new RegistryId(namespace, path);
    }

    public static RegistryId create(@NotNull Item item) {
        return create(BuiltInRegistries.ITEM.getKey(item));
    }

    public static RegistryId create(@NotNull ResourceKey<?> resourceKey) {
        return create(resourceKey.location());
    }

    public static RegistryId create(@NotNull ResourceLocation resourceLocation) {
        return new RegistryId(resourceLocation.getNamespace(), resourceLocation.getPath());
    }
}
