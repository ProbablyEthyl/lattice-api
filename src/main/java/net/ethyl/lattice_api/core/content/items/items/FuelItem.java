package net.ethyl.lattice_api.core.content.items.items;

import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.common.items.items.LatticeFuelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FuelItem extends Item {
    private final int burnTicks;

    public FuelItem(@NotNull LatticeFuelItem.AppendableBuilder<Item, ? extends LatticeItem<Item>, ?> builder) {
        super(builder.getItemProperties());
        this.burnTicks = builder.getBurnTicks();
    }

    @Override
    public int getBurnTime(@NotNull ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return this.burnTicks;
    }
}
