package net.ethyl.lattice_api.modules.common.items.equipment.tools;

import net.ethyl.lattice_api.core.content.items.equipment.tools.BasicAxe;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeBasicAxe extends LatticeItem<AxeItem> {
    protected LatticeBasicAxe(@NotNull RegistryId registryId, @NotNull DeferredItem<AxeItem> deferredItem, @NotNull AppendableBuilder<AxeItem, ? extends LatticeItem<AxeItem>, ?> builder) {
        super(registryId, deferredItem, builder);
    }

    public static Builder builder() {
        return new Builder(LatticeBasicAxe::new, BasicAxe::new);
    }

    public static class Builder extends AppendableBuilder<AxeItem, LatticeBasicAxe, Builder> {
        protected Builder(@NotNull TriFunction<RegistryId, DeferredItem<AxeItem>, Builder, LatticeBasicAxe> latticeFactory, @NotNull Function<Builder, AxeItem> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }

    public static class AppendableBuilder<T extends Item, I extends LatticeItem<T>, B extends AppendableBuilder<T, I, B>> extends LatticeBasicPickaxe.AppendableBuilder<T, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredItem<T>, B, I> latticeFactory, @NotNull Function<B, T> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }
}
