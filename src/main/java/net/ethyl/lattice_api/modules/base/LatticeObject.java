package net.ethyl.lattice_api.modules.base;

import net.ethyl.lattice_api.core.instances.RegistryId;
import org.jetbrains.annotations.NotNull;

public abstract class LatticeObject {
    private final RegistryId registryId;

    protected LatticeObject(@NotNull RegistryId registryId) {
        this.registryId = registryId;
    }

    public RegistryId getRegistryId() {
        return this.registryId;
    }

    public String getModId() {
        return this.getRegistryId().getNamespace();
    }

    public String getId() {
        return this.getRegistryId().getPath();
    }
}
