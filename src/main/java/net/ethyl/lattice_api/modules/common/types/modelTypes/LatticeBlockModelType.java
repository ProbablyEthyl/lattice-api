package net.ethyl.lattice_api.modules.common.types.modelTypes;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeBlockModelType extends LatticeType {
    protected LatticeBlockModelType(@NotNull RegistryId registryId) {
        super(registryId);
    }

    public static Builder builder() {
        return new Builder(LatticeBlockModelType::new);
    }

    public static class Builder extends AppendableBuilder<LatticeBlockModelType, Builder> {
        private Builder(@NotNull Function<RegistryId, LatticeBlockModelType> latticeFactory) {
            super(latticeFactory);
        }
    }
}