package net.ethyl.lattice_api.modules.common.modelTypes;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeModelType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeBlockModelType extends LatticeModelType {
    protected LatticeBlockModelType(@NotNull RegistryId registryId) {
        super(registryId);
    }

    public static Builder builder() {
        return new Builder(LatticeBlockModelType::new);
    }

    public static class Builder extends AppendableBuilder<LatticeBlockModelType> {
        protected Builder(@NotNull Function<RegistryId, LatticeBlockModelType> latticeFactory) {
            super(latticeFactory);
        }
    }

    public static class AppendableBuilder<I extends LatticeModelType> extends LatticeModelType.Builder<I> {
        protected AppendableBuilder(@NotNull Function<RegistryId, I> latticeFactory) {
            super(latticeFactory);
        }
    }
}