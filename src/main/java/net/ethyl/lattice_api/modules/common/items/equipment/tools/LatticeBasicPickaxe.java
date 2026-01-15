package net.ethyl.lattice_api.modules.common.items.equipment.tools;

import net.ethyl.lattice_api.core.content.items.equipment.tools.BasicPickaxe;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LatticeBasicPickaxe extends LatticeItem<PickaxeItem> {
    private final Tier tier;

    protected LatticeBasicPickaxe(@NotNull RegistryId registryId, @NotNull DeferredItem<PickaxeItem> deferredItem, @NotNull AppendableBuilder<? extends LatticeItem<PickaxeItem>, ?> builder) {
        super(registryId, deferredItem, builder);
        this.tier = builder.getTier();
    }

    public Tier getTier() {
        return this.tier;
    }

    public static AppendableBuilder<? extends LatticeBasicPickaxe, ?> builder() {
        return new AppendableBuilder<>(LatticeBasicPickaxe::new, BasicPickaxe::new);
    }

    public static class AppendableBuilder<I extends LatticeItem<PickaxeItem>, B extends AppendableBuilder<I, B>> extends LatticeItem.AppendableBuilder.Tool<PickaxeItem, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredItem<PickaxeItem>, B, I> latticeFactory, @NotNull Function<B, PickaxeItem> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }
}
