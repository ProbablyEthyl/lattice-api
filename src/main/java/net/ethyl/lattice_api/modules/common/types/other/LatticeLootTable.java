package net.ethyl.lattice_api.modules.common.types.other;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeType;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class LatticeLootTable extends LatticeType {
    public Supplier<Item> drop;
    public float minDrops;
    public float maxDrops;

    protected LatticeLootTable(@NotNull RegistryId registryId, @NotNull Builder builder) {
        super(registryId, builder);
    }

    public static Builder builder() {
        return new Builder(LatticeLootTable::new);
    }

    public static class Builder extends AppendableBuilder<LatticeLootTable, Builder> {
        private Builder(@NotNull BiFunction<RegistryId, Builder, LatticeLootTable> latticeFactory) {
            super(latticeFactory);
        }
    }
}
