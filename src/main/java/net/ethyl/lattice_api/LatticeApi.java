package net.ethyl.lattice_api;

import com.mojang.logging.LogUtils;
import net.ethyl.lattice_api.core.data.LatticeRegistries;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.core.utils.ErrUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(LatticeApi.MOD_ID)
public class LatticeApi {
    public static final String MOD_ID = "lattice_api";
    public static final Logger LOGGER = LogUtils.getLogger();

    public LatticeApi(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        LatticeRegistries.register(modEventBus);
    }

    public static void invalidIdErr(@NotNull RegistryId registryId) {
        ErrUtils.invalidIdErr(registryId);
    }

    public static void duplicateObjectErr(@NotNull RegistryId registryId) {
        ErrUtils.duplicateObjectErr(registryId);
    }
}
