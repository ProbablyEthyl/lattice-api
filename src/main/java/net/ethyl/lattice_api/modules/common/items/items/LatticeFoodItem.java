package net.ethyl.lattice_api.modules.common.items.items;

import net.ethyl.lattice_api.core.content.items.items.FoodItem;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeFoodItem extends LatticeItem<Item> {
    protected final FoodProperties.Builder foodProperties;

    protected LatticeFoodItem(@NotNull RegistryId registryId, @NotNull DeferredItem<Item> deferredItem, @NotNull AppendableBuilder<? extends LatticeItem<Item>, ?> builder) {
        super(registryId, deferredItem, builder);
        this.foodProperties = builder.foodProperties;
    }

    public FoodProperties.Builder getFoodProperties() {
        return this.foodProperties;
    }

    public static AppendableBuilder<? extends LatticeFoodItem, ?> builder() {
        return new AppendableBuilder<>(LatticeFoodItem::new, FoodItem::new);
    }

    public static class AppendableBuilder<I extends LatticeFoodItem, B extends AppendableBuilder<I, B>> extends LatticeItem.AppendableBuilder<Item, I, B> {
        protected FoodProperties.Builder foodProperties = new FoodProperties.Builder().nutrition(5).saturationModifier(5f);

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredItem<Item>, B, I> latticeFactory, @NotNull Function<B, Item> itemFactory) {
            super(latticeFactory, itemFactory);
        }

        @Override
        public B from(@NotNull I latticeItem) {
            this.foodProperties(latticeItem.getFoodProperties());

            return super.from(latticeItem);
        }

        public B foodProperties(@NotNull FoodProperties.Builder foodProperties) {
            this.foodProperties = foodProperties;

            return this.self();
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

        public B resultItem(@NotNull LatticeBlock<?> latticeBlock) {
            return this.resultItem(latticeBlock::asItem);
        }

        public B resultItem(@NotNull Block block) {
            return this.resultItem(block::asItem);
        }

        public B resultItem(@NotNull LatticeItem<?> latticeItem) {
            return this.resultItem(latticeItem::get);
        }

        public B resultItem(@NotNull Item item) {
            return this.resultItem(() -> item);
        }

        private B resultItem(@NotNull Supplier<Item> itemSupplier) {
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
