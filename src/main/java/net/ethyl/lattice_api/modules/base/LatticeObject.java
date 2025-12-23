package net.ethyl.lattice_api.modules.base;

import net.ethyl.lattice_api.core.instances.RegistryId;
import org.jetbrains.annotations.NotNull;

public abstract class LatticeObject {
    protected final RegistryId registryId;

    protected LatticeObject(@NotNull RegistryId registryId) {
        this.registryId = registryId;
    }

    public String getModId() {
        return this.registryId.getNamespace();
    }

    public String getId() {
        return this.registryId.getPath();
    }

    public RegistryId getRegistryId() {
        return this.registryId;
    }

    public abstract LatticeObject clone();
}
