package net.ethyl.lattice_api.core.content.items;

import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.common.items.LatticeFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import org.jetbrains.annotations.NotNull;

public class FoodItem extends Item {
    public FoodItem(@NotNull LatticeFoodItem.AppendableBuilder<? extends LatticeItem<Item>, ?> builder) {
        super(builder.itemProperties.food(builder.foodProperties.build()));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack) {
        return UseAnim.EAT;
    }
}
