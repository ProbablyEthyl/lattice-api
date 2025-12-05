package net.ethyl.lattice_api.modules.common.modelTypes;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeModelType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeItemModelType extends LatticeModelType {
    protected LatticeItemModelType(@NotNull RegistryId registryId) {
        super(registryId);
    }

    public static Builder builder() {
        return new Builder(LatticeItemModelType::new);
    }

    public static class Builder extends AppendableBuilder<LatticeItemModelType> {
        protected Builder(@NotNull Function<RegistryId, LatticeItemModelType> latticeFactory) {
            super(latticeFactory);
        }
    }

    public static class AppendableBuilder<I extends LatticeModelType> extends LatticeModelType.Builder<I> {
        protected AppendableBuilder(@NotNull Function<RegistryId, I> latticeFactory) {
            super(latticeFactory);
        }
    }
}