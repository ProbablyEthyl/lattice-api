package net.ethyl.lattice_api.modules.common.items.equipment.tools;

import net.ethyl.lattice_api.core.content.items.equipment.tools.BasicShovel;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeBasicShovel extends LatticeItem<ShovelItem> {
    private final Tier tier;

    protected LatticeBasicShovel(@NotNull RegistryId registryId, @NotNull DeferredItem<ShovelItem> deferredItem, @NotNull AppendableBuilder<? extends LatticeItem<ShovelItem>, ?> builder) {
        super(registryId, deferredItem, builder);
        this.tier = builder.getTier();
    }

    public Tier getTier() {
        return this.tier;
    }

    public static AppendableBuilder<? extends LatticeBasicShovel, ?> builder() {
        return new AppendableBuilder<>(LatticeBasicShovel::new, BasicShovel::new);
    }


    public static class AppendableBuilder<I extends LatticeItem<ShovelItem>, B extends AppendableBuilder<I, B>> extends LatticeItem.AppendableBuilder.Tool<ShovelItem, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredItem<ShovelItem>, B, I> latticeFactory, @NotNull Function<B, ShovelItem> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }
}
