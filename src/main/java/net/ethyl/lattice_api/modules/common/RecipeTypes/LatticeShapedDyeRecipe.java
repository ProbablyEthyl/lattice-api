package net.ethyl.lattice_api.modules.common.RecipeTypes;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class LatticeShapedDyeRecipe extends LatticeShapedRecipe {
    private final Collection<Character> dyeables;

    protected LatticeShapedDyeRecipe(@NotNull RegistryId registryId, @NotNull AppendableBuilder<? extends LatticeShapedDyeRecipe, ?> builder) {
        super(registryId, builder);
        this.dyeables = builder.dyeables;
    }

    public boolean isDyeable(char character) {
        return this.dyeables.contains(character);
    }

    public static AppendableBuilder<? extends LatticeShapedDyeRecipe, ?> builder() {
        return new AppendableBuilder<>(LatticeShapedDyeRecipe::new);
    }

    public static class AppendableBuilder<I extends LatticeShapedDyeRecipe, B extends AppendableBuilder<I, B>> extends LatticeShapedRecipe.AppendableBuilder<I, B> {
        private final Collection<Character> dyeables = new ArrayList<>();

        protected AppendableBuilder(@NotNull BiFunction<RegistryId, B, I> latticeFactory) {
            super(latticeFactory);
        }

        @Override
        public B define(char character, @NotNull LatticeBlock<?> latticeBlock) {
            return this.define(character, latticeBlock, false);
        }

        public B define(char character, @NotNull LatticeBlock<?> latticeBlock, boolean isDyeable) {
            return this.define(character, latticeBlock::asItem, isDyeable);
        }

        @Override
        public B define(char character, @NotNull Block block) {
            return this.define(character, block, false);
        }

        public B define(char character, @NotNull Block block, boolean isDyeable) {
            return this.define(character, block::asItem, isDyeable);
        }

        public B define(char character, @NotNull LatticeItem<?> latticeItem) {
            return this.define(character, latticeItem, false);
        }

        public B define(char character, @NotNull LatticeItem<?> latticeItem, boolean isDyeable) {
            return this.define(character, latticeItem::get, isDyeable);
        }

        @Override
        public B define(char character, @NotNull Item item) {
            return this.define(character, item, false);
        }

        public B define(char character, @NotNull Item item, boolean isDyeable) {
            return this.define(character, () -> item, isDyeable);
        }

        protected B define(char character, @NotNull Supplier<Item> itemSupplier, boolean isDyeable) {
            return this.define(character, List.of(itemSupplier), isDyeable);
        }

        @Override
        public B define(char character, @NotNull Collection<Supplier<Item>> items) {
            return this.define(character, items, false);
        }

        public B define(char character, @NotNull Collection<Supplier<Item>> items, boolean isDyeable) {
            if (isDyeable) {
                this.dyeables.add(character);
            } else {
                this.dyeables.remove(character);
            }

            return super.define(character, items);
        }
    }
}
