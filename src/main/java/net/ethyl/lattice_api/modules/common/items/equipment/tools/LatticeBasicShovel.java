package net.ethyl.lattice_api.modules.common.items.equipment.tools;

import net.ethyl.lattice_api.core.content.items.equipment.tools.BasicShovel;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeBasicShovel extends LatticeItem<ShovelItem> {
    protected LatticeBasicShovel(@NotNull RegistryId registryId, @NotNull DeferredItem<ShovelItem> deferredItem, @NotNull AppendableBuilder<ShovelItem, ? extends LatticeItem<ShovelItem>, ?> builder) {
        super(registryId, deferredItem, builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AppendableBuilder<ShovelItem, LatticeBasicShovel, Builder> {
        protected Builder() {
            super(LatticeBasicShovel::new, BasicShovel::new);
        }
    }

    public static class AppendableBuilder<T extends Item, I extends LatticeItem<T>, B extends AppendableBuilder<T, I, B>> extends LatticeBasicPickaxe.AppendableBuilder<T, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredItem<T>, B, I> latticeFactory, @NotNull Function<B, T> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }
}
