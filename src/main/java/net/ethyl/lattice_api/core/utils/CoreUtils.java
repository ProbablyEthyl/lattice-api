package net.ethyl.lattice_api.core.utils;

import net.ethyl.lattice_api.core.instances.RegistryId;
import org.jetbrains.annotations.NotNull;

public class CoreUtils {
    public static boolean isInvalidId(@NotNull RegistryId registryId) {
        return !registryId.toString().matches("[a-z_:]+");
    }
}
