package net.ethyl.lattice_api.modules.common.items.equipment.armor;

import net.ethyl.lattice_api.core.content.items.equipment.armor.BasicBoots;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeBasicBoots extends LatticeItem<BasicBoots> {
    protected LatticeBasicBoots(@NotNull RegistryId registryId, @NotNull Supplier<BasicBoots> deferredItem, @NotNull AppendableBuilder<? extends LatticeBasicBoots, ?> builder) {
        super(registryId, deferredItem, builder);
    }

    public static AppendableBuilder<? extends LatticeBasicBoots, ?> builder() {
        return new AppendableBuilder<>(LatticeBasicBoots::new, BasicBoots::new);
    }

    public static class AppendableBuilder<I extends LatticeBasicBoots, B extends AppendableBuilder<I, B>> extends LatticeItem.AppendableBuilder.Armor<BasicBoots, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, Supplier<BasicBoots>, B, I> latticeFactory, @NotNull Function<B, BasicBoots> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }
}
