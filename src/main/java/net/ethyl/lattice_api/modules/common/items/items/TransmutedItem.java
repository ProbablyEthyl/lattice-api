package net.ethyl.lattice_api.modules.common.items.items;

import net.ethyl.lattice_api.core.data.LatticeRegistries;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.minecraft.world.item.Item;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class TransmutedItem extends LatticeItem<Item> {
    protected TransmutedItem(@NotNull RegistryId registryId, @NotNull Supplier<Item> supplierItem, @NotNull AppendableBuilder<?> builder) {
        super(registryId, supplierItem, builder);
    }

    public static void transmute(@NotNull Item item) {
        TransmutedItem transmutedItem = (TransmutedItem) transmutableBuilder(item).build(RegistryId.create(item), () -> item);
        LatticeRegistries.transmute(transmutedItem);

    }

    @SuppressWarnings("unchecked")
    private static <B extends AppendableBuilder<B>> B transmutableBuilder(@NotNull Item item) {
        return (B) new AppendableBuilder<>(TransmutedItem::new, b -> item);
    }

    public static class AppendableBuilder<B extends AppendableBuilder<B>> extends LatticeItem.AppendableBuilder<Item, LatticeItem<Item>, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, Supplier<Item>, B, LatticeItem<Item>> latticeFactory, @NotNull Function<B, Item> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }
}
