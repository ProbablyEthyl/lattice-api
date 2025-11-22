package net.ethyl.lattice_api.core.utils;

import net.ethyl.lattice_api.LatticeApi;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeObject;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class RegistryUtils {
    public static void checkDuplicate(@NotNull Collection<? extends LatticeObject> collection, @NotNull RegistryId registryId) {
        if (collection.stream().anyMatch(latticeObject -> latticeObject.getRegistryId().toString().equals(registryId.toString()))) {
            LatticeApi.duplicateObjectErr(registryId);
        }
    }
}
