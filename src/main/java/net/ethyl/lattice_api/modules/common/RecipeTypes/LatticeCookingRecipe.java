package net.ethyl.lattice_api.modules.common.RecipeTypes;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.base.LatticeRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class LatticeCookingRecipe extends LatticeRecipe {
    private final Map<Integer, Map<Collection<Supplier<Item>>, Float>> recipeIngredients;
    private final Map<RecipeSerializer<?>, Integer> recipeSerializers;

    protected LatticeCookingRecipe(@NotNull RegistryId registryId, @NotNull AppendableBuilder<? extends LatticeCookingRecipe, ?> builder) {
        super(registryId, builder);
        this.recipeIngredients = builder.recipeIngredients;
        this.recipeSerializers = builder.recipeSerializers;
    }

    public Map<Integer, Map<Collection<Supplier<Item>>, Float>> getIngredients() {
        return this.recipeIngredients;
    }

    public Map<RecipeSerializer<?>, Integer> getSerializers() {
        return this.recipeSerializers;
    }

    public static AppendableBuilder<? extends LatticeCookingRecipe, ?> builder() {
        return new AppendableBuilder<>(LatticeCookingRecipe::new);
    }

    public static class AppendableBuilder<I extends LatticeCookingRecipe, B extends AppendableBuilder<I, B>> extends LatticeRecipe.AppendableBuilder<I, B> {
        private final Map<Integer, Map<Collection<Supplier<Item>>, Float>> recipeIngredients = new HashMap<>();
        private final Map<RecipeSerializer<?>, Integer> recipeSerializers = new HashMap<>();

        private int ingredients = 0;

        protected AppendableBuilder(@NotNull BiFunction<RegistryId, B, I> latticeFactory) {
            super(latticeFactory);
        }

        public B ingredient(@NotNull LatticeBlock<?> latticeBlock) {
            return this.ingredient(latticeBlock, 0.0f);
        }

        public B ingredient(@NotNull LatticeBlock<?> latticeBlock, float experience) {
            return this.ingredient(latticeBlock::asItem, experience);
        }

        public B ingredient(@NotNull Block block) {
            return this.ingredient(block, 0.0f);
        }

        public B ingredient(@NotNull Block block, float experience) {
            return this.ingredient(block::asItem, experience);
        }

        public B ingredient(@NotNull LatticeItem<?> latticeItem) {
            return this.ingredient(latticeItem, 0.0f);
        }

        public B ingredient(@NotNull LatticeItem<?> latticeItem, float experience) {
            return this.ingredient(latticeItem::get, experience);
        }

        public B ingredient(@NotNull Item item) {
            return this.ingredient(item, 0.0f);
        }

        public B ingredient(@NotNull Item item, float experience) {
            return this.ingredient(() -> item, experience);
        }

        private B ingredient(@NotNull Supplier<Item> item) {
            return this.ingredient(item, 0.0f);
        }

        public B ingredient(@NotNull Supplier<Item> item, float experience) {
            return this.ingredient(List.of(item), experience);
        }

        public B ingredient(@NotNull Collection<Supplier<Item>> items) {
            return this.ingredient(items, 0.0f);
        }

        public B ingredient(@NotNull Collection<Supplier<Item>> items, float experience) {
            this.recipeIngredients.put(this.ingredients++, Map.of(items, experience));

            return this.self();
        }

        public B smelting(int cookingTime) {
            return this.serialize(RecipeSerializer.SMELTING_RECIPE, cookingTime);
        }

        public B blasting(int cookingTime) {
            return this.serialize(RecipeSerializer.BLASTING_RECIPE, cookingTime);
        }

        public B smoking(int cookingTime) {
            return this.serialize(RecipeSerializer.SMOKING_RECIPE, cookingTime);
        }

        private B serialize(@NotNull RecipeSerializer<?> recipeSerializer, int cookingTime) {
            this.recipeSerializers.put(recipeSerializer, cookingTime);

            return this.self();
        }
    }
}
