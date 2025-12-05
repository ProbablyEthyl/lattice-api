package net.ethyl.lattice_api.modules.base;

import net.ethyl.lattice_api.core.instances.RegistryId;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class LatticeType extends LatticeObject {
    protected LatticeType(@NotNull RegistryId registryId, @NotNull AppendableBuilder<? extends LatticeType, ?> builder) {
        super(registryId);
    }

    public static class AppendableBuilder<I extends LatticeType, B extends AppendableBuilder<I, B>> {
        private final BiFunction<RegistryId, B, I> latticeFactory;

        @SuppressWarnings("unchecked")
        protected B self() {
            return (B) this;
        }

        protected AppendableBuilder(@NotNull BiFunction<RegistryId, B, I> latticeFactory) {
            this.latticeFactory = latticeFactory;
        }

        public I build(@NotNull RegistryId registryId) {
            return this.latticeFactory.apply(registryId, this.self());
        }
    }
}