package net.ethyl.lattice_api.modules.common.types.modelTypes;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeType;
import org.jetbrains.annotations.NotNull;

public class LatticeItemModelType extends LatticeType {
    protected LatticeItemModelType(@NotNull RegistryId registryId) {
        super(registryId);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends LatticeType.AppendableBuilder<LatticeItemModelType, Builder> {
        private Builder() {
            super(LatticeItemModelType::new);
        }
    }
}