package net.ethyl.lattice_api.core.content.items.items;

import net.ethyl.lattice_api.core.utils.CoreUtils;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.common.items.items.LatticeFuelItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FuelItem extends Item {
    private final boolean hasDescription;
    private final int burnTicks;

    public FuelItem(@NotNull LatticeFuelItem.AppendableBuilder<? extends LatticeItem<Item>, ?> builder) {
        super(builder.getItemProperties());
        this.hasDescription = builder.getHasDescription();
        this.burnTicks = builder.getBurnTicks();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        if (this.hasDescription) CoreUtils.setBasicDescription(itemStack, tooltipComponents);

        super.appendHoverText(itemStack, tooltipContext, tooltipComponents, tooltipFlag);
    }

    @Override
    public int getBurnTime(@NotNull ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return this.burnTicks;
    }
}
