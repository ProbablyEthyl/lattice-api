package net.ethyl.lattice_api.modules.common.types.modelTypes;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeItemModelType extends LatticeType {
    protected LatticeItemModelType(@NotNull RegistryId registryId) {
        super(registryId);
    }

    public static Builder builder() {
        return new Builder(LatticeItemModelType::new);
    }

    public static class Builder extends AppendableBuilder<LatticeItemModelType, Builder> {
        private Builder(@NotNull Function<RegistryId, LatticeItemModelType> latticeFactory) {
            super(latticeFactory);
        }
    }
}