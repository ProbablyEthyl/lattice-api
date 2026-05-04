package net.ethyl.lattice_api.core.utils.utility;

import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import org.jetbrains.annotations.NotNull;

public class ErrUtils {
    public static void invalidIdErr(@NotNull RegistryId registryId) {
        throw new InstantiationError("The mod '" + registryId.getNamespace() + "' tried to register an object with an invalid id! Key: '" + registryId.getPath() + "'!");
    }

    public static void duplicateObjectErr(@NotNull RegistryId registryId) {
        throw new InstantiationError("The mod '" + registryId.getNamespace() + "' tried to register an already existing object! Key: '" + registryId.getPath() + "'!");
    }

    public static void incompleteObjectErr(@NotNull RegistryId registryId) {
        throw new InstantiationError("The mod '" + registryId.getNamespace() + "' tried to register an incomplete object! Key: '" + registryId.getPath() + "'!");
    }

    public static void illegalAccessErr(@NotNull String fieldName) {
        throw new RuntimeException("Illegal access at the field '" + fieldName + "'!");
    }

    public static void createFrozenErr(@NotNull String modId) {
        throw new InstantiationError("The mod '" + modId + "' tried to create a registry while registries are frozen!");
    }

    public static void isFrozenErr(@NotNull String modId) {
        throw new InstantiationError("The mod '" + modId + "' tried to register (to) a frozen registry!");
    }
}
