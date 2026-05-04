package net.ethyl.lattice_api;

import com.mojang.logging.LogUtils;
import net.ethyl.lattice_api.core.data.LatticeDataGen;
import net.ethyl.lattice_api.core.instances.other.Events;
import net.ethyl.lattice_api.mod.registries.LatticeTypes;
import net.ethyl.lattice_api.mod.registries.TransmutedItems;
import net.ethyl.lattice_api.mod.registries.TransmutedTrimMaterials;
import net.ethyl.lattice_api.mod.registries.TransmutedTrimPatterns;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Set;

@Mod(LatticeApi.MOD_ID)
public class LatticeApi {
    public static final String MOD_ID = "lattice_api";
    public static final Logger LOGGER = LogUtils.getLogger();

    public LatticeApi(IEventBus modEventBus, ModContainer modContainer) {
        LatticeTypes.register(modEventBus);
        Events.addListeners(modEventBus);
        TransmutedItems.transmute();
        TransmutedTrimMaterials.transmute();
        TransmutedTrimPatterns.transmute();
        LatticeDataGen.addListeners(modEventBus, modContainer, Set.of());
        LatticeApiClient.addListeners(modEventBus);
    }

    public static void info(@NotNull String message) {
        LOGGER.info(message);
    }
}