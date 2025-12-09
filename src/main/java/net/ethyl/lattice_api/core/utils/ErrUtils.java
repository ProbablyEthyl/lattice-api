package net.ethyl.lattice_api.core.utils;

import net.ethyl.lattice_api.core.instances.RegistryId;
import org.jetbrains.annotations.NotNull;

public class ErrUtils {
    public static void invalidIdErr(@NotNull RegistryId registryId) {
        throw new InstantiationError("The mod '" + registryId.getNamespace() + "' tried to register an object with an invalid id! Key: '" + registryId.getPath() + "'!");
    }

    public static void duplicateObjectErr(@NotNull RegistryId registryId) {
        throw new InstantiationError("The mod '" + registryId.getNamespace() + "' tried to register an already existing object! Key: '" + registryId.getPath() + "'!");
    }

    public static void IncompleteObjectErr(@NotNull RegistryId registryId) {
        throw new InstantiationError("The mod '" + registryId.getNamespace() + "' tried to register an incomplete object! Key: '" + registryId.getPath() + "'!");
    }
}
