package net.ethyl.lattice_api.modules.common.items.equipment.armor;

import net.ethyl.lattice_api.core.content.items.equipment.armor.BasicChestplate;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeBasicChestplate extends LatticeItem<BasicChestplate> {
    protected LatticeBasicChestplate(@NotNull RegistryId registryId, @NotNull Supplier<BasicChestplate> deferredItem, @NotNull AppendableBuilder<? extends LatticeBasicChestplate, ?> builder) {
        super(registryId, deferredItem, builder);
    }

    public static AppendableBuilder<? extends LatticeBasicChestplate, ?> builder() {
        return new AppendableBuilder<>(LatticeBasicChestplate::new, BasicChestplate::new);
    }

    public static class AppendableBuilder<I extends LatticeBasicChestplate, B extends AppendableBuilder<I, B>> extends LatticeItem.AppendableBuilder.Armor<BasicChestplate, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, Supplier<BasicChestplate>, B, I> latticeFactory, @NotNull Function<B, BasicChestplate> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }
}
