package net.ethyl.lattice_api.modules.common.items.equipment.weapons;

import net.ethyl.lattice_api.core.content.items.equipment.weapons.BasicSword;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.common.items.equipment.tools.LatticeBasicPickaxe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeBasicSword extends LatticeItem<SwordItem> {
    protected LatticeBasicSword(@NotNull RegistryId registryId, @NotNull DeferredItem<SwordItem> deferredItem, @NotNull AppendableBuilder<SwordItem, ? extends LatticeItem<SwordItem>, ?> builder) {
        super(registryId, deferredItem, builder);
    }

    public static Builder builder() {
        return new Builder(LatticeBasicSword::new, BasicSword::new);
    }

    public static class Builder extends AppendableBuilder<SwordItem, LatticeBasicSword, Builder> {
        protected Builder(@NotNull TriFunction<RegistryId, DeferredItem<SwordItem>, Builder, LatticeBasicSword> latticeFactory, @NotNull Function<Builder, SwordItem> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }

    public static class AppendableBuilder<T extends Item, I extends LatticeItem<T>, B extends AppendableBuilder<T, I, B>> extends LatticeBasicPickaxe.AppendableBuilder<T, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredItem<T>, B, I> latticeFactory, @NotNull Function<B, T> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }
}
