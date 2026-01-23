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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class LatticeShapedRecipe extends LatticeRecipe {
    private final Map<Integer, String> recipePattern;
    private final Map<Character, Collection<Supplier<Item>>> recipeDefiner;
    private final Map<Character, TagKey<Item>> recipeDefinerTags;

    protected LatticeShapedRecipe(@NotNull RegistryId registryId, @NotNull AppendableBuilder<? extends LatticeRecipe, ?> builder) {
        super(registryId, builder);
        this.recipePattern = builder.recipePattern;
        this.recipeDefiner = builder.recipeDefiner;
        this.recipeDefinerTags = builder.recipeDefinerTags;
    }

    public Map<Integer, String> getPattern() {
        return this.recipePattern;
    }

    public Map<Character, Collection<Supplier<Item>>> getDefined() {
        return this.recipeDefiner;
    }

    public Map<Character, TagKey<Item>> getDefinedTags() {
        return this.recipeDefinerTags;
    }

    public static AppendableBuilder<? extends LatticeShapedRecipe, ?> builder() {
        return new AppendableBuilder<>(LatticeShapedRecipe::new);
    }

    public static class AppendableBuilder<I extends LatticeShapedRecipe, B extends AppendableBuilder<I, B>> extends LatticeRecipe.AppendableBuilder<I, B> {
        private final Map<Integer, String> recipePattern = new HashMap<>();
        private final Map<Character, Collection<Supplier<Item>>> recipeDefiner = new HashMap<>();
        private final Map<Character, TagKey<Item>> recipeDefinerTags = new HashMap<>();

        protected int currentRow = 0;

        protected AppendableBuilder(@NotNull BiFunction<RegistryId, B, I> latticeFactory) {
            super(latticeFactory);
        }

        public B pattern(@NotNull String pattern) {
            StringBuilder input = new StringBuilder();
            int inputLength = Math.min(3, pattern.length());
            int remaining = inputLength;

            if (this.currentRow >= 3) {
                return this.self();
            }

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
            this.recipeDefiner.put(character, List.of(itemSupplier));

            return this.self();
        }

        public B define(char character, @NotNull Collection<Supplier<Item>> items) {
            this.recipeDefiner.put(character, items);

            return this.self();
        }

        public B define(char character, @NotNull LatticeItemTag latticeItemTag) {
            return this.define(character, latticeItemTag.get());
        }

        public B define(char character, @NotNull TagKey<Item> tagKey) {
            this.recipeDefinerTags.put(character, tagKey);

            return this.self();
        }
    }
}
