package net.ethyl.lattice_api.modules.base;

import net.neoforged.bus.api.IEventBus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;

public abstract class LatticeRegistry<T extends LatticeObject>  {
    protected final String modId;
    protected final Collection<T> registryContent = new LinkedList<>();

    protected LatticeRegistry(@NotNull String modId) {
        this.modId = modId;
    }

    public abstract void register(@NotNull IEventBus modEventBus);
}