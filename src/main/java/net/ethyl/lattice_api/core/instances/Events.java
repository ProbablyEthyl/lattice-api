package net.ethyl.lattice_api.core.instances;

import net.ethyl.lattice_api.core.data.LatticeFXManager;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class Events {
    public static void onServerTickEvent(ServerTickEvent.Post event) {
        LatticeFXManager.tick();
    }

    public static void addListener() {
        NeoForge.EVENT_BUS.addListener(Events::onServerTickEvent);
    }
}
