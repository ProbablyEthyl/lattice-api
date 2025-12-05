package net.ethyl.lattice_api.modules.base;

import net.ethyl.lattice_api.core.instances.RegistryId;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeModelType extends LatticeObject {
    protected LatticeModelType(@NotNull RegistryId registryId) {
        super(registryId);
    }

    public static class Builder<I extends LatticeModelType> {
        private final Function<RegistryId, I> latticeFactory;

        protected Builder(@NotNull Function<RegistryId, I> latticeFactory) {
            this.latticeFactory = latticeFactory;
        }

        public I build(@NotNull RegistryId registryId) {
            return this.latticeFactory.apply(registryId);
        }
    }
}