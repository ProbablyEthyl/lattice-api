package net.ethyl.lattice_api.modules.common.items.items;

import net.ethyl.lattice_api.core.content.items.items.FuelItem;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.minecraft.world.item.Item;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeFuelItem extends LatticeItem<Item> {
    protected final int burnTicks;

    protected LatticeFuelItem(@NotNull RegistryId registryId, @NotNull Supplier<Item> deferredItem, @NotNull AppendableBuilder<? extends LatticeFuelItem, ?> builder) {
        super(registryId, deferredItem, builder);
        this.burnTicks = builder.burnTicks;
    }

    public int getBurnTicks() {
        return this.burnTicks;
    }

    public static AppendableBuilder<? extends LatticeFuelItem, ?> builder() {
        return new AppendableBuilder<>(LatticeFuelItem::new, FuelItem::new);
    }

    public static class AppendableBuilder<I extends LatticeFuelItem, B extends AppendableBuilder<I, B>> extends LatticeItem.AppendableBuilder<Item, I, B> {
        protected int burnTicks = 20;

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, Supplier<Item>, B, I> latticeFactory, @NotNull Function<B, Item> itemFactory) {
            super(latticeFactory, itemFactory);
        }

        public int getBurnTicks() {
            return this.burnTicks;
        }

        public B burnTicks(int burnTicks) {
            this.burnTicks = burnTicks;

            return this.self();
        }
    }
}
