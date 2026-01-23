package net.ethyl.lattice_api.modules.common.RecipeTypes;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.base.LatticeRecipe;
import net.ethyl.lattice_api.modules.common.tags.LatticeItemTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class LatticeShapelessRecipe extends LatticeRecipe {
    private final Map<Integer, Collection<Supplier<Item>>> recipeIngredients;
    private final Map<Integer, TagKey<Item>> recipeIngredientTags;

    protected LatticeShapelessRecipe(@NotNull RegistryId registryId, @NotNull AppendableBuilder<? extends LatticeRecipe, ?> builder) {
        super(registryId, builder);
        this.recipeIngredients = builder.recipeIngredients;
        this.recipeIngredientTags = builder.recipeIngredientTags;
    }

    public Map<Integer, Collection<Supplier<Item>>> getIngredients() {
        return this.recipeIngredients;
    }

    public Map<Integer, TagKey<Item>> getIngredientTags() {
        return this.recipeIngredientTags;
    }

    public static AppendableBuilder<? extends LatticeShapelessRecipe, ?> builder() {
        return new AppendableBuilder<>(LatticeShapelessRecipe::new);
    }

    public static class AppendableBuilder<I extends LatticeShapelessRecipe, B extends AppendableBuilder<I, B>> extends LatticeRecipe.AppendableBuilder<I, B> {
        private final Map<Integer, Collection<Supplier<Item>>> recipeIngredients = new HashMap<>();
        private final Map<Integer, TagKey<Item>> recipeIngredientTags = new HashMap<>();

        protected int ingredients = 0;

        protected AppendableBuilder(@NotNull BiFunction<RegistryId, B, I> latticeFactory) {
            super(latticeFactory);
        }

        public B ingredient(@NotNull LatticeBlock<?> latticeBlock) {
            return this.ingredient(latticeBlock, 1);
        }

        public B ingredient(@NotNull LatticeBlock<?> latticeBlock, int count) {
            return this.ingredient(latticeBlock::asItem, count);
        }

        public B ingredient(@NotNull Block block) {
            return this.ingredient(block, 1);
        }

        public B ingredient(@NotNull Block block, int count) {
            return this.ingredient(block::asItem, count);
        }

        public B ingredient(@NotNull LatticeItem<?> latticeItem) {
            return this.ingredient(latticeItem, 1);
        }

        public B ingredient(@NotNull LatticeItem<?> latticeItem, int count) {
            return this.ingredient(latticeItem::get, count);
        }

        public B ingredient(@NotNull Item item) {
            return this.ingredient(item, 1);
        }

        public B ingredient(@NotNull Item item, int count) {
            return this.ingredient(() -> item, count);
        }

        private B ingredient(@NotNull Supplier<Item> itemSupplier, int count) {
            return this.ingredient(List.of(itemSupplier), count);
        }

        public B ingredient(@NotNull Collection<Supplier<Item>> items) {
            return this.ingredient(items, 1);
        }

        public B ingredient(@NotNull Collection<Supplier<Item>> items, int count) {
            for (int i = 0; i < count && this.ingredients < 9; i++) {
                this.recipeIngredients.put(++this.ingredients, items);
            }

            return this.self();
        }

        public B ingredient(@NotNull LatticeItemTag latticeItemTag) {
            return this.ingredient(latticeItemTag, 1);
        }

        public B ingredient(@NotNull LatticeItemTag latticeItemTag, int count) {
            return this.ingredient(latticeItemTag.get(), count);
        }

        public B ingredient(@NotNull TagKey<Item> tagKey) {
            return this.ingredient(tagKey, 1);
        }

        public B ingredient(@NotNull TagKey<Item> tagKey, int count) {
            for (int i = 0; i < count && this.ingredients < 9; i++) {
                this.recipeIngredientTags.put(++this.ingredients, tagKey);
            }

            return this.self();
        }
    }
}
