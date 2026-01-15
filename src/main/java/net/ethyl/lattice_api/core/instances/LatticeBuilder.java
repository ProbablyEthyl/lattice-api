package net.ethyl.lattice_api.core.instances;

import net.ethyl.lattice_api.modules.base.LatticeObject;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class LatticeBuilder<F, A, I extends LatticeObject, B extends LatticeBuilder<F, A, I, B>> {
    private final F latticeFactory;

    @SuppressWarnings("unchecked")
    public B self() {
        return (B) this;
    }

    private LatticeBuilder(@NotNull F latticeFactory) {
        this.latticeFactory = latticeFactory;
    }

    protected F getLatticeFactory() {
        return this.latticeFactory;
    }

    public abstract I build(@NotNull RegistryId registryId, @Nullable A object);

    public static class Simple<I extends LatticeObject, B extends Simple<I, B>> extends LatticeBuilder<Function<RegistryId, I>, Object, I, B> {
        protected Simple(@NotNull Function<RegistryId, I> latticeFactory) {
            super(latticeFactory);
        }

        @Override
        public I build(@NotNull RegistryId registryId, @Nullable Object object) {
            return this.getLatticeFactory().apply(registryId);
        }
    }

    public static class Advanced<I extends LatticeObject, B extends Advanced<I, B>> extends LatticeBuilder<BiFunction<RegistryId, B, I>, Object, I, B> {
        protected Advanced(@NotNull BiFunction<RegistryId, B, I> latticeFactory) {
            super(latticeFactory);
        }

        @Override
        public I build(@NotNull RegistryId registryId, @Nullable Object object) {
            return this.getLatticeFactory().apply(registryId, this.self());
        }
    }

    public static class Complex<I extends LatticeObject, A, B extends Complex<I, A, B>> extends LatticeBuilder<TriFunction<RegistryId, A, B, I>, A, I, B> {
        protected Complex(@NotNull TriFunction<RegistryId, A, B, I> latticeFactory) {
            super(latticeFactory);
        }

        @Override
        public I build(@NotNull RegistryId registryId, @Nullable A object) {
            return this.getLatticeFactory().apply(registryId, object, this.self());
        }
    }
}
