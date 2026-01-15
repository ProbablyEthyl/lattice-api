package net.ethyl.lattice_api.modules.common.items.equipment.tools;

import net.ethyl.lattice_api.core.content.items.equipment.tools.BasicHoe;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeBasicHoe extends LatticeItem<HoeItem> {
    private final Tier tier;

    protected LatticeBasicHoe(@NotNull RegistryId registryId, @NotNull DeferredItem<HoeItem> deferredItem, @NotNull AppendableBuilder<? extends LatticeItem<HoeItem>, ?> builder) {
        super(registryId, deferredItem, builder);
        this.tier = builder.getTier();
    }

    public Tier getTier() {
        return this.tier;
    }

    public static AppendableBuilder<? extends LatticeBasicHoe, ?> builder() {
        return new AppendableBuilder<>(LatticeBasicHoe::new, BasicHoe::new);
    }

    public static class AppendableBuilder<I extends LatticeItem<HoeItem>, B extends AppendableBuilder<I, B>> extends LatticeItem.AppendableBuilder.Tool<HoeItem, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredItem<HoeItem>, B, I> latticeFactory, @NotNull Function<B, HoeItem> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }
}
