package net.ethyl.lattice_api.modules.common.items.items;

import net.ethyl.lattice_api.core.content.items.items.UseableItem;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.function.TriConsumer;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeUseableItem extends LatticeBasicItem {
    protected final TriConsumer<Level, Player, InteractionHand> use;
    protected final Consumer<UseOnContext> useOn;

    protected LatticeUseableItem(@NotNull RegistryId registryId, @NotNull Supplier<Item> deferredItem, @NotNull AppendableBuilder<? extends LatticeUseableItem, ?> builder) {
        super(registryId, deferredItem, builder);
        this.use = builder.getUse();
        this.useOn = builder.getUseOn();
    }

    public TriConsumer<Level, Player, InteractionHand> getUse() {
        return this.use;
    }

    public Consumer<UseOnContext> getUseOn() {
        return this.useOn;
    }

    public static AppendableBuilder<? extends LatticeUseableItem, ?> builder() {
        return new AppendableBuilder<>(LatticeUseableItem::new, UseableItem::new);
    }

    public static class AppendableBuilder<I extends LatticeUseableItem, B extends AppendableBuilder<I, B>> extends LatticeBasicItem.AppendableBuilder<I, B> {
        private TriConsumer<Level, Player, InteractionHand> use = null;
        private Consumer<UseOnContext> useOn = null;

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, Supplier<Item>, B, I> latticeFactory, @NotNull Function<B, Item> itemFactory) {
            super(latticeFactory, itemFactory);
        }

        public B use(@NotNull TriConsumer<Level, Player, InteractionHand> use) {
            this.use = use;

            return this.self();
        }

        public TriConsumer<Level, Player, InteractionHand> getUse() {
            return this.use;
        }

        public B useOn(@NotNull Consumer<UseOnContext> useOn) {
            this.useOn = useOn;

            return this.self();
        }

        public Consumer<UseOnContext> getUseOn() {
            return this.useOn;
        }
    }
}
