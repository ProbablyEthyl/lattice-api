package net.ethyl.lattice_api.modules.base;

import net.ethyl.lattice_api.core.instances.LatticeBuilder;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class LatticeRecipe extends LatticeObject {
    private final RecipeCategory recipeCategory;
    private final Collection<Supplier<Item>> result;
    private final int resultCount;
    private final String unlockId;
    private final Supplier<Item> unlockItem;
    private final String groupId;

    protected LatticeRecipe(@NotNull RegistryId registryId, @NotNull LatticeRecipe.AppendableBuilder<? extends LatticeRecipe, ?> builder) {
        super(registryId);
        this.recipeCategory = builder.recipeCategory;
        this.result = builder.result;
        this.resultCount = builder.resultCount;
        this.unlockId = builder.unlockId;
        this.unlockItem = builder.unlockItem;
        this.groupId = builder.groupId;
    }

    public RecipeCategory getCategory() {
        return this.recipeCategory;
    }

    public Collection<Supplier<Item>> getResult() {
        return this.result;
    }

    public int getResultCount() {
        return this.resultCount;
    }

    public String getUnlockId() {
        return this.unlockId;
    }

    public Supplier<Item> getUnlockItem() {
        return this.unlockItem;
    }

    public String getGroup() {
        return this.groupId;
    }

    public static class AppendableBuilder<I extends LatticeRecipe, B extends AppendableBuilder<I, B>> extends LatticeBuilder.Advanced<I, B> {
        private RecipeCategory recipeCategory = RecipeCategory.MISC;
        private Collection<Supplier<Item>> result = List.of();
        private int resultCount = 1;
        private String unlockId = "unlock_by";
        private Supplier<Item> unlockItem = () -> Items.STONE;
        private String groupId = "none";

        protected AppendableBuilder(@NotNull BiFunction<RegistryId, B, I> latticeFactory) {
            super(latticeFactory);
        }

        public B recipeCategory(@NotNull RecipeCategory recipeCategory) {
            this.recipeCategory = recipeCategory;

            return this.self();
        }

        public B result(@NotNull LatticeBlock<?> latticeBlock) {
            return this.result(latticeBlock::asItem);
        }

        public B result(@NotNull Block block) {
            return this.result(block::asItem);
        }

        public B result(@NotNull LatticeItem<?> latticeItem) {
            return this.result(latticeItem::get);
        }

        public B result(@NotNull Item item) {
            return this.result(() -> item);
        }

        private B result(@NotNull Supplier<Item> itemSupplier) {
            return this.result(List.of(itemSupplier));
        }

        public B result(@NotNull Collection<Supplier<Item>> items) {
            this.result = items;

            return this.self();
        }

        public B resultCount(int count) {
            this.resultCount = count;

            return this.self();
        }

        public B unlockedBy(@NotNull String id, @NotNull LatticeBlock<?> latticeBlock) {
            return this.has(id, latticeBlock::asItem);
        }

        public B unlockedBy(@NotNull String id, @NotNull Block block) {
            return this.has(id, block::asItem);
        }

        public B unlockedBy(@NotNull String id, @NotNull LatticeItem<?> latticeItem) {
            return this.has(id, latticeItem::get);
        }

        public B unlockedBy(@NotNull String id, @NotNull Item item) {
            return this.has(id, () -> item);
        }

        private B has(@NotNull String id, @NotNull Supplier<Item> itemSupplier) {
            this.unlockId = id;
            this.unlockItem = itemSupplier;

            return this.self();
        }

        public B group(@NotNull String id) {
            this.groupId = id;

            return this.self();
        }
    }
}