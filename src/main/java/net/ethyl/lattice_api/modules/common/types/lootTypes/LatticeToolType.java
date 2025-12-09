package net.ethyl.lattice_api.modules.common.types.lootTypes;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeToolType extends LatticeType {
    protected LatticeToolType(@NotNull RegistryId registryId) {
        super(registryId);
    }

    public static Builder builder() {
        return new Builder(LatticeToolType::new);
    }

    public static class Builder extends LatticeType.AppendableBuilder<LatticeToolType, Builder> {
        protected Builder(@NotNull Function<RegistryId, LatticeToolType> latticeFactory) {
            super(latticeFactory);
        }
    }
}
