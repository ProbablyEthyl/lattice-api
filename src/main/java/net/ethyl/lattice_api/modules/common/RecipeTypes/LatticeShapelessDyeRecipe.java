package net.ethyl.lattice_api.modules.common.RecipeTypes;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.base.LatticeRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class LatticeShapelessDyeRecipe extends LatticeShapelessRecipe {
    private final Collection<Integer> dyeable;

    protected LatticeShapelessDyeRecipe(@NotNull RegistryId registryId, @NotNull AppendableBuilder<? extends LatticeRecipe, ?> builder) {
        super(registryId, builder);
        this.dyeable = builder.dyeable;
    }

    public boolean isDyeable(int ingredient) {
        return this.dyeable.contains(ingredient);
    }

    public static AppendableBuilder<? extends LatticeShapelessDyeRecipe, ?> builder() {
        return new AppendableBuilder<>(LatticeShapelessDyeRecipe::new);
    }

    public static class AppendableBuilder<I extends LatticeShapelessDyeRecipe, B extends AppendableBuilder<I, B>> extends LatticeShapelessRecipe.AppendableBuilder<I, B> {
        private final Collection<Integer> dyeable = new ArrayList<>();

        protected AppendableBuilder(@NotNull BiFunction<RegistryId, B, I> latticeFactory) {
            super(latticeFactory);
        }

        @Override
        public B ingredient(@NotNull LatticeBlock<?> latticeBlock) {
            return this.ingredient(latticeBlock, false);
        }

        @Override
        public B ingredient(@NotNull LatticeBlock<?> latticeBlock, int count) {
            return this.ingredient(latticeBlock, count, false);
        }

        public B ingredient(@NotNull LatticeBlock<?> latticeBlock, boolean isDyeable) {
            return this.ingredient(latticeBlock, 1, isDyeable);
        }

        public B ingredient(@NotNull LatticeBlock<?> latticeBlock, int count, boolean isDyeable) {
            return this.ingredient(latticeBlock::asItem, count, isDyeable);
        }

        @Override
        public B ingredient(@NotNull Block block) {
            return this.ingredient(block, false);
        }

        @Override
        public B ingredient(@NotNull Block block, int count) {
            return this.ingredient(block, count, false);
        }

        public B ingredient(@NotNull Block block, boolean isDyeable) {
            return this.ingredient(block, 1, isDyeable);
        }

        public B ingredient(@NotNull Block block, int count, boolean isDyeable) {
            return this.ingredient(block::asItem, count, isDyeable);
        }

        @Override
        public B ingredient(@NotNull LatticeItem<?> latticeItem) {
            return this.ingredient(latticeItem, false);
        }

        @Override
        public B ingredient(@NotNull LatticeItem<?> latticeItem, int count) {
            return this.ingredient(latticeItem, count, false);
        }

        public B ingredient(@NotNull LatticeItem<?> latticeItem, boolean isDyeable) {
            return this.ingredient(latticeItem, 1, isDyeable);
        }

        public B ingredient(@NotNull LatticeItem<?> latticeItem, int count, boolean isDyeable) {
            return this.ingredient(latticeItem::get, count, isDyeable);
        }

        @Override
        public B ingredient(@NotNull Item item) {
            return this.ingredient(item, false);
        }

        @Override
        public B ingredient(@NotNull Item item, int count) {
            return this.ingredient(item, count, false);
        }

        public B ingredient(@NotNull Item item, boolean isDyeable) {
            return this.ingredient(item, 1, isDyeable);
        }

        public B ingredient(@NotNull Item item, int count, boolean isDyeable) {
            return this.ingredient(() -> item, count, isDyeable);
        }

        protected B ingredient(@NotNull Supplier<Item> itemSupplier, int count, boolean isDyeable) {
            return this.ingredient(List.of(itemSupplier), count, isDyeable);
        }

        @Override
        public B ingredient(@NotNull Collection<Supplier<Item>> items) {
            return this.ingredient(items, false);
        }

        public B ingredient(@NotNull Collection<Supplier<Item>> items, boolean isDyeable) {
            return this.ingredient(items, 1, isDyeable);
        }

        public B ingredient(@NotNull Collection<Supplier<Item>> items, int count, boolean isDyeable) {
            if (isDyeable) {
                for (int i = 0; i < count && this.ingredients < 9; i++) {
                    this.dyeable.add(this.ingredients + i + 1);
                }
            }

            return super.ingredient(items, count);
        }
    }
}
