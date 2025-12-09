package net.ethyl.lattice_api.modules.common.items.items;

import net.ethyl.lattice_api.core.content.items.items.FoodItem;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeFoodItem extends LatticeItem<Item> {
    protected LatticeFoodItem(@NotNull RegistryId registryId, @NotNull DeferredItem<Item> deferredItem, @NotNull AppendableBuilder<Item, ? extends LatticeItem<Item>, ?> builder) {
        super(registryId, deferredItem, builder);
    }

    public static Builder builder() {
        return new Builder(LatticeFoodItem::new, FoodItem::new);
    }

    public static class Builder extends AppendableBuilder<Item, LatticeFoodItem, Builder> {
        protected Builder(@NotNull TriFunction<RegistryId, DeferredItem<Item>, Builder, LatticeFoodItem> latticeFactory, @NotNull Function<Builder, Item> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }

    public static class AppendableBuilder<T extends Item, I extends LatticeItem<T>, B extends AppendableBuilder<T, I, B>> extends LatticeItem.AppendableBuilder<T, I, B> {
        protected final FoodProperties.Builder foodProperties = new FoodProperties.Builder().nutrition(5).saturationModifier(5f);

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredItem<T>, B, I> latticeFactory, @NotNull Function<B, T> itemFactory) {
            super(latticeFactory, itemFactory);
        }

        public FoodProperties.Builder getFoodProperties() {
            return this.foodProperties;
        }

        public B nutrition(int nutrition) {
            this.foodProperties.nutrition(nutrition);

            return this.self();
        }

        public B saturation(float saturation) {
            this.foodProperties.saturationModifier(saturation);

            return this.self();
        }

        public B resultItem(@NotNull LatticeItem<?> latticeItem) {
            return this.resultItem(latticeItem::get);
        }

        public B resultItem(@NotNull LatticeBlock<?> latticeBlock) {
            return this.resultItem(latticeBlock::asItem);
        }

        public B resultItem(@NotNull Item item) {
            return this.resultItem(() -> item);
        }

        public B resultItem(@NotNull Supplier<Item> itemSupplier) {
            this.foodProperties.usingConvertsTo(itemSupplier.get());

            return this.self();
        }

        public B fast() {
            this.foodProperties.fast();

            return this.self();
        }

        public B alwaysEdible() {
            this.foodProperties.alwaysEdible();

            return this.self();
        }
    }
}
