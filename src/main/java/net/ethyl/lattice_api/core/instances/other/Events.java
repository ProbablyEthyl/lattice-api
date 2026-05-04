package net.ethyl.lattice_api.core.instances.other;

import net.ethyl.lattice_api.LatticeApi;
import net.ethyl.lattice_api.core.data.LatticeFXManager;
import net.ethyl.lattice_api.core.data.LatticeRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jetbrains.annotations.NotNull;

public class Events {
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            LatticeRegistries.freeze();
            LatticeApi.info("All registries for '" + LatticeApi.MOD_ID + "' are now frozen.");
        });
    }

    public static void onServerTickEvent(ServerTickEvent.Post event) {
        LatticeFXManager.tick();
    }

    public static void addListeners(@NotNull IEventBus modEventBus) {
        NeoForge.EVENT_BUS.addListener(Events::onServerTickEvent);
        modEventBus.addListener(Events::onCommonSetupEvent);
    }
}
