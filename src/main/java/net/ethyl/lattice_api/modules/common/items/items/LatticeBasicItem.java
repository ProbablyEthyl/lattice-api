package net.ethyl.lattice_api.modules.common.items.items;

import net.ethyl.lattice_api.core.content.items.items.BasicItem;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeBasicItem extends LatticeItem<Item> {
    protected LatticeBasicItem(@NotNull RegistryId registryId, @NotNull DeferredItem<Item> deferredItem, @NotNull AppendableBuilder<? extends LatticeItem<Item>, ?> builder) {
        super(registryId, deferredItem, builder);
    }

    public static AppendableBuilder<? extends LatticeBasicItem, ?> builder() {
        return new AppendableBuilder<>(LatticeBasicItem::new, BasicItem::new);
    }

    public static class AppendableBuilder<I extends LatticeBasicItem, B extends AppendableBuilder<I, B>> extends LatticeItem.AppendableBuilder<Item, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredItem<Item>, B, I> latticeFactory, @NotNull Function<B, Item> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }
}
