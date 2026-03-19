package net.ethyl.lattice_api.modules.common.items.equipment.armor;

import net.ethyl.lattice_api.core.content.items.equipment.armor.BasicHelmet;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeBasicHelmet extends LatticeItem<BasicHelmet> {
    protected LatticeBasicHelmet(@NotNull RegistryId registryId, @NotNull Supplier<BasicHelmet> deferredItem, @NotNull AppendableBuilder<? extends LatticeBasicHelmet, ?> builder) {
        super(registryId, deferredItem, builder);
    }

    public static AppendableBuilder<? extends LatticeBasicHelmet, ?> builder() {
        return new AppendableBuilder<>(LatticeBasicHelmet::new, BasicHelmet::new);
    }

    public static class AppendableBuilder<I extends LatticeBasicHelmet, B extends AppendableBuilder<I, B>> extends LatticeItem.AppendableBuilder.Armor<BasicHelmet, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, Supplier<BasicHelmet>, B, I> latticeFactory, @NotNull Function<B, BasicHelmet> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }
}
