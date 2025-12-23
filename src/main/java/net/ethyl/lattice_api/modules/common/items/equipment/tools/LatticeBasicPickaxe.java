package net.ethyl.lattice_api.modules.common.items.equipment.tools;

import net.ethyl.lattice_api.core.content.items.equipment.tools.BasicPickaxe;
import net.ethyl.lattice_api.core.data.LatticeRegistries;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.common.items.equipment.tier.LatticeTier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeBasicPickaxe extends LatticeItem<PickaxeItem> {
    protected LatticeBasicPickaxe(@NotNull RegistryId registryId, @NotNull DeferredItem<PickaxeItem> deferredItem, @NotNull AppendableBuilder<PickaxeItem, ? extends LatticeItem<PickaxeItem>, ?> builder) {
        super(registryId, deferredItem, builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AppendableBuilder<PickaxeItem, LatticeBasicPickaxe, Builder> {
        protected Builder() {
            super(LatticeBasicPickaxe::new, BasicPickaxe::new);
        }
    }

    public static class AppendableBuilder<T extends Item, I extends LatticeItem<T>, B extends AppendableBuilder<T, I, B>> extends LatticeItem.AppendableBuilder<T, I, B> {
        protected Tier tier = Tiers.DIAMOND;

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredItem<T>, B, I> latticeFactory, @NotNull Function<B, T> itemFactory) {
            super(latticeFactory, itemFactory);
            this.modelType(LatticeRegistries.Types.Item.HANDHELD);
        }

        public B attribute(@NotNull ItemAttributeModifiers itemAttributeModifiers) {
            this.itemProperties.attributes(itemAttributeModifiers);

            return this.self();
        }

        public Tier getTier() {
            return this.tier;
        }

        public B tier(@NotNull LatticeTier latticeTier) {
            return this.tier(latticeTier.get());
        }

        public B tier(@NotNull Tier tier) {
            this.tier = tier;

            return this.self();
        }
    }
}
