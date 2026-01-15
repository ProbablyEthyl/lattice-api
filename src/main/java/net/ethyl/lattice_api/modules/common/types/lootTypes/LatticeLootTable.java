package net.ethyl.lattice_api.modules.common.types.lootTypes;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class LatticeLootTable extends LatticeType {
    protected Supplier<Item> drop = () -> Items.STONE;
    protected float minDrops = 1;
    protected float maxDrops = 3;

    protected LatticeLootTable(@NotNull RegistryId registryId) {
        super(registryId);
    }

    public Supplier<Item> getDrop() {
        return this.drop;
    }

    public LatticeLootTable drop(@NotNull Supplier<Item> itemSupplier) {
        this.drop = itemSupplier;

        return this;
    }

    public float getMinDrops() {
        return this.minDrops;
    }

    public LatticeLootTable minDrops(float minDrops) {
        this.minDrops = minDrops;

        return this;
    }

    public float getMaxDrops() {
        return this.maxDrops;
    }

    public LatticeLootTable maxDrops(float maxDrops) {
        this.maxDrops = maxDrops;

        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends LatticeType.AppendableBuilder<LatticeLootTable, Builder> {
        private Builder() {
            super(LatticeLootTable::new);
        }
    }
}
