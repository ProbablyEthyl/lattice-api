package net.ethyl.lattice_api.modules.base;

import net.neoforged.bus.api.IEventBus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;

public abstract class LatticeRegistry<I extends LatticeObject>  {
    public final String modId;
    public final Collection<I> registryContent = new LinkedList<>();

    protected LatticeRegistry(@NotNull String modId) {
        this.modId = modId;
    }

    public abstract void register(@NotNull IEventBus modEventBus);
}