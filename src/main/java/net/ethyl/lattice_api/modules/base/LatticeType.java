package net.ethyl.lattice_api.modules.base;

import net.ethyl.lattice_api.core.instances.LatticeBuilder;
import net.ethyl.lattice_api.core.instances.RegistryId;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeType extends LatticeObject {
    protected LatticeType(@NotNull RegistryId registryId) {
        super(registryId);
    }

    public static class AppendableBuilder<I extends LatticeType, B extends AppendableBuilder<I, B>> extends LatticeBuilder.Simple<I, B> {
        protected AppendableBuilder(@NotNull Function<RegistryId, I> latticeFactory) {
            super(latticeFactory);
        }
    }
}