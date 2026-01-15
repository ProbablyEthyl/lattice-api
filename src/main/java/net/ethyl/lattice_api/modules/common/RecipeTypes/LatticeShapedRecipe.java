package net.ethyl.lattice_api.modules.common.RecipeTypes;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.base.LatticeRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class LatticeShapedRecipe extends LatticeRecipe {
    private final Map<Integer, String> recipePattern;
    private final Map<Character, Supplier<Item>> recipeDefiner;
    private final String unlockId;
    private final Supplier<Item> unlockItem;

    protected LatticeShapedRecipe(@NotNull RegistryId registryId, @NotNull AppendableBuilder<? extends LatticeRecipe, ?> builder) {
        super(registryId, builder);
        this.recipePattern = builder.recipePattern;
        this.recipeDefiner = builder.recipeDefiner;
        this.unlockId = builder.unlockId;
        this.unlockItem = builder.unlockItem;
    }

    public Map<Integer, String> getPattern() {
        return this.recipePattern;
    }

    public Map<Character, Supplier<Item>> getDefined() {
        return this.recipeDefiner;
    }

    public String getUnlockId() {
        return this.unlockId;
    }

    public Supplier<Item> getUnlockItem() {
        return this.unlockItem;
    }

    public static AppendableBuilder<? extends LatticeShapedRecipe, ?> builder() {
        return new AppendableBuilder<>(LatticeShapedRecipe::new);
    }

    public static class AppendableBuilder<I extends LatticeShapedRecipe, B extends AppendableBuilder<I, B>> extends LatticeRecipe.AppendableBuilder<I, B> {
        private final Map<Integer, String> recipePattern = new HashMap<>();
        private final Map<Character, Supplier<Item>> recipeDefiner = new HashMap<>();

        private int currentRow = 0;
        private String unlockId = "unlock_by";
        private Supplier<Item> unlockItem = () -> Items.STONE;

        protected AppendableBuilder(@NotNull BiFunction<RegistryId, B, I> latticeFactory) {
            super(latticeFactory);
        }

        public B pattern(@NotNull String pattern) {
            StringBuilder input = new StringBuilder();
            int inputLength = Math.min(3, pattern.length());
            int remaining = inputLength;

            if (this.currentRow > 0) {
                for (Map.Entry<Integer, String> entry : this.recipePattern.entrySet()) {
                    String rowPattern = entry.getValue();
                    int rowLength = rowPattern.length();

                    if (rowLength > inputLength) {
                        remaining = rowLength;
                    }

                    if (rowLength < 3 && rowLength != inputLength) {
                        this.recipePattern.put(entry.getKey(), rowPattern + "?".repeat(Math.max(0, inputLength - rowLength)));
                    }
                }
            }

            if (!pattern.isEmpty()) {
                input.append(pattern.length() > 3 ? pattern.substring(0, 3) : pattern);
                remaining -= inputLength;
            }

            input.append("?".repeat(remaining));
            this.recipePattern.put(this.currentRow++, input.toString());

            return this.self();
        }

        public B define(char character, @NotNull LatticeBlock<?> latticeBlock) {
            return this.define(character, latticeBlock::asItem);
        }

        public B define(char character, @NotNull Block block) {
            return this.define(character, block::asItem);
        }

        public B define(char character, @NotNull LatticeItem<?> latticeItem) {
            return this.define(character, latticeItem::get);
        }

        public B define(char character, @NotNull Item item) {
            return this.define(character, () -> item);
        }

        private B define(char character, @NotNull Supplier<Item> itemSupplier) {
            this.recipeDefiner.put(character, itemSupplier);

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
    }
}
