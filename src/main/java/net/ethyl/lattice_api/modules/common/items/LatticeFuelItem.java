package net.ethyl.lattice_api.modules.common.items;

import net.ethyl.lattice_api.core.content.items.FuelItem;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeFuelItem extends LatticeItem<Item> {
    protected LatticeFuelItem(@NotNull RegistryId registryId, @NotNull DeferredItem<Item> deferredItem, @NotNull AppendableBuilder<Item, ? extends LatticeItem<Item>, ?> builder) {
        super(registryId, deferredItem, builder);
    }

    public static Builder builder() {
        return new Builder(LatticeFuelItem::new, FuelItem::new);
    }

    public static class Builder extends AppendableBuilder<Item, LatticeFuelItem, Builder> {
        protected Builder(@NotNull TriFunction<RegistryId, DeferredItem<Item>, Builder, LatticeFuelItem> latticeFactory, @NotNull Function<Builder, Item> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }

    public static class AppendableBuilder<T extends Item, I extends LatticeItem<T>, B extends AppendableBuilder<T, I, B>> extends LatticeItem.AppendableBuilder<T, I, B> {
        public int burnTicks = 20;

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredItem<T>, B, I> latticeFactory, @NotNull Function<B, T> itemFactory) {
            super(latticeFactory, itemFactory);
        }

        public B burnTicks(int burnTicks) {
            this.burnTicks = burnTicks;

            return this.self();
        }
    }
}
