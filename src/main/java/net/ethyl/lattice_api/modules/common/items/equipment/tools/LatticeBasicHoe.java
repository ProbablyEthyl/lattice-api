package net.ethyl.lattice_api.modules.common.items.equipment.tools;

import net.ethyl.lattice_api.core.content.items.equipment.tools.BasicHoe;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeBasicHoe extends LatticeItem<HoeItem> {
    protected LatticeBasicHoe(@NotNull RegistryId registryId, @NotNull DeferredItem<HoeItem> deferredItem, @NotNull AppendableBuilder<HoeItem, ? extends LatticeItem<HoeItem>, ?> builder) {
        super(registryId, deferredItem, builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AppendableBuilder<HoeItem, LatticeBasicHoe, Builder> {
        protected Builder() {
            super(LatticeBasicHoe::new, BasicHoe::new);
        }
    }

    public static class AppendableBuilder<T extends Item, I extends LatticeItem<T>, B extends AppendableBuilder<T, I, B>> extends LatticeBasicPickaxe.AppendableBuilder<T, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredItem<T>, B, I> latticeFactory, @NotNull Function<B, T> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }
}
