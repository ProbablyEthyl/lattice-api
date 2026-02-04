package net.ethyl.lattice_api.core.instances;

import net.ethyl.lattice_api.core.utils.CoreUtils;
import net.ethyl.lattice_api.core.utils.ErrUtils;
import net.minecraft.resources.ResourceLocation;
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
}
