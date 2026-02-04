package net.ethyl.lattice_api;

import com.mojang.logging.LogUtils;
import net.ethyl.lattice_api.core.data.LatticeRegistries;
import net.ethyl.lattice_api.core.instances.Events;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(LatticeApi.MOD_ID)
public class LatticeApi {
    public static final String MOD_ID = "lattice_api";
    public static final Logger LOGGER = LogUtils.getLogger();

    public LatticeApi(IEventBus modEventBus, ModContainer ignored) {
        LatticeRegistries.register(modEventBus);
        Events.addListener();
    }
}