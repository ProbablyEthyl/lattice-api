package net.ethyl.lattice_api.modules.common.items.equipment.armor;

import net.ethyl.lattice_api.core.content.items.equipment.armor.BasicLeggings;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeBasicLeggings extends LatticeItem<BasicLeggings> {
    protected LatticeBasicLeggings(@NotNull RegistryId registryId, @NotNull Supplier<BasicLeggings> deferredItem, @NotNull AppendableBuilder<? extends LatticeBasicLeggings, ?> builder) {
        super(registryId, deferredItem, builder);
    }

    public static AppendableBuilder<? extends LatticeBasicLeggings, ?> builder() {
        return new AppendableBuilder<>(LatticeBasicLeggings::new, BasicLeggings::new);
    }

    public static class AppendableBuilder<I extends LatticeBasicLeggings, B extends AppendableBuilder<I, B>> extends LatticeItem.AppendableBuilder.Armor<BasicLeggings, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, Supplier<BasicLeggings>, B, I> latticeFactory, @NotNull Function<B, BasicLeggings> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }
}
