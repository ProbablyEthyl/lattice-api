package net.ethyl.lattice_api.modules.common.items;

import net.ethyl.lattice_api.core.content.items.BasicItem;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeBasicItem extends LatticeItem<Item> {
    protected LatticeBasicItem(@NotNull RegistryId registryId, @NotNull DeferredItem<Item> deferredItem, @NotNull Builder builder) {
        super(registryId, deferredItem, builder);
    }

    public static Builder builder() {
        return new Builder(LatticeBasicItem::new, BasicItem::new);
    }

    public static class Builder extends LatticeItem.Builder<Item, LatticeBasicItem, Builder> {
        protected Builder(@NotNull TriFunction<RegistryId, DeferredItem<Item>, Builder, LatticeBasicItem> latticeFactory, @NotNull Function<Builder, Item> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }
}
