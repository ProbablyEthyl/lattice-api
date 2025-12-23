package net.ethyl.lattice_api.modules.base;

import net.ethyl.lattice_api.core.instances.RegistryId;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeType extends LatticeObject {
    protected LatticeType(@NotNull RegistryId registryId) {
        super(registryId);
    }

    @Override
    public LatticeObject clone() {
        return new LatticeType(this.getRegistryId());
    }

    public static class AppendableBuilder<I extends LatticeType, B extends AppendableBuilder<I, B>> {
        private final Function<RegistryId, I> latticeFactory;

        @SuppressWarnings("unchecked")
        protected B self() {
            return (B) this;
        }

        protected AppendableBuilder(@NotNull Function<RegistryId, I> latticeFactory) {
            this.latticeFactory = latticeFactory;
        }

        public I build(@NotNull RegistryId registryId) {
            return this.latticeFactory.apply(registryId);
        }
    }
}