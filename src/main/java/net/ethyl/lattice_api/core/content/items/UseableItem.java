package net.ethyl.lattice_api.core.content.items;

import net.ethyl.lattice_api.core.utils.CoreUtils;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.common.items.LatticeUseableItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.function.TriConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class UseableItem extends Item {
    private final boolean hasDescription;
    private final TriConsumer<Level, Player, InteractionHand> use;
    private final Consumer<UseOnContext> useOn;

    public UseableItem(@NotNull LatticeUseableItem.AppendableBuilder<? extends LatticeItem<Item>, ?> builder) {
        super(builder.itemProperties);
        this.hasDescription = builder.hasDescription;
        this.use = builder.use;
        this.useOn = builder.useOn;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        if (this.hasDescription) CoreUtils.setBasicDescription(itemStack, tooltipComponents);

        super.appendHoverText(itemStack, tooltipContext, tooltipComponents, tooltipFlag);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        this.use.accept(level, player, interactionHand);

        return super.use(level, player, interactionHand);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext useOnContext) {
        this.useOn.accept(useOnContext);

        return super.useOn(useOnContext);
    }
}
