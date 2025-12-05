package net.ethyl.lattice_api.modules.common.items;

import net.ethyl.lattice_api.core.content.items.UseableItem;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.function.TriConsumer;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public class LatticeUseableItem extends LatticeItem<Item> {
    protected LatticeUseableItem(@NotNull RegistryId registryId, @NotNull DeferredItem<Item> deferredItem, @NotNull AppendableBuilder<? extends LatticeItem<Item>, ?> builder) {
        super(registryId, deferredItem, builder);
    }

    public static Builder builder() {
        return new Builder(LatticeUseableItem::new, UseableItem::new);
    }

    public static class Builder extends AppendableBuilder<LatticeUseableItem, Builder> {
        protected Builder(@NotNull TriFunction<RegistryId, DeferredItem<Item>, Builder, LatticeUseableItem> latticeFactory, @NotNull Function<Builder, Item> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }

    public static class AppendableBuilder<I extends LatticeItem<Item>, B extends AppendableBuilder<I, B>> extends LatticeItem.AppendableBuilder<Item, I, B> {
        public TriConsumer<Level, Player, InteractionHand> use = (level, player, interactionHand) -> {};
        public Consumer<UseOnContext> useOn = (useOnContext) -> {};

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredItem<Item>, B, I> latticeFactory, @NotNull Function<B, Item> itemFactory) {
            super(latticeFactory, itemFactory);
        }

        public B use(@NotNull TriConsumer<Level, Player, InteractionHand> use) {
            this.use = use;

            return this.self();
        }

        public B useOn(@NotNull Consumer<UseOnContext> useOn) {
            this.useOn = useOn;

            return this.self();
        }
    }
}
