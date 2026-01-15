package net.ethyl.lattice_api.modules.base;

import net.ethyl.lattice_api.core.instances.LatticeBuilder;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class LatticeRecipe extends LatticeObject {
    private final RecipeCategory recipeCategory;
    private final Supplier<Item> result;
    private final int resultCount;
    private final String groupId;

    protected LatticeRecipe(@NotNull RegistryId registryId, @NotNull LatticeRecipe.AppendableBuilder<? extends LatticeRecipe, ?> builder) {
        super(registryId);
        this.recipeCategory = builder.recipeCategory;
        this.result = builder.result;
        this.resultCount = builder.resultCount;
        this.groupId = builder.groupId;
    }

    public RecipeCategory getCategory() {
        return this.recipeCategory;
    }

    public Supplier<Item> getResult() {
        return this.result;
    }

    public int getResultCount() {
        return this.resultCount;
    }

    public String getGroup() {
        return this.groupId;
    }

    public static class AppendableBuilder<I extends LatticeRecipe, B extends AppendableBuilder<I, B>> extends LatticeBuilder.Advanced<I, B> {
        private RecipeCategory recipeCategory = RecipeCategory.MISC;
        private Supplier<Item> result = () -> Items.STONE;
        private int resultCount = 1;
        private String groupId = "none";

        protected AppendableBuilder(@NotNull BiFunction<RegistryId, B, I> latticeFactory) {
            super(latticeFactory);
        }

        public B recipeCategory(@NotNull RecipeCategory recipeCategory) {
            this.recipeCategory = recipeCategory;

            return this.self();
        }

        public B result(@NotNull LatticeBlock<?> latticeBlock, int count) {
            return this.result(latticeBlock::asItem, count);
        }

        public B result(@NotNull Block block, int count) {
            return this.result(block::asItem, count);
        }

        public B result(@NotNull LatticeItem<?> latticeItem, int count) {
            return this.result(latticeItem::get, count);
        }

        public B result(@NotNull Item item, int count) {
            return this.result(() -> item, count);
        }

        private B result(@NotNull Supplier<Item> itemSupplier, int count) {
            this.result = itemSupplier;
            this.resultCount = count;

            return this.self();
        }

        public B group(@NotNull String id) {
            this.groupId = id;

            return this.self();
        }
    }
}
