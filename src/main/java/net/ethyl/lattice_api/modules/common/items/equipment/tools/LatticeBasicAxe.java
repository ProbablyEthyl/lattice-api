package net.ethyl.lattice_api.modules.common.items.equipment.tools;

import net.ethyl.lattice_api.core.content.items.equipment.tools.BasicAxe;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Tier;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeBasicAxe extends LatticeItem<AxeItem> {
    private final Tier tier;

    protected LatticeBasicAxe(@NotNull RegistryId registryId, @NotNull Supplier<AxeItem> deferredItem, @NotNull AppendableBuilder<? extends LatticeBasicAxe, ?> builder) {
        super(registryId, deferredItem, builder);
        this.tier = builder.getTier();
    }

    public Tier getTier() {
        return this.tier;
    }

    public static AppendableBuilder<? extends LatticeBasicAxe, ?> builder() {
        return new AppendableBuilder<>(LatticeBasicAxe::new, BasicAxe::new);
    }

    public static class AppendableBuilder<I extends LatticeBasicAxe, B extends AppendableBuilder<I, B>> extends LatticeItem.AppendableBuilder.Tool<AxeItem, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, Supplier<AxeItem>, B, I> latticeFactory, @NotNull Function<B, AxeItem> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }
}
