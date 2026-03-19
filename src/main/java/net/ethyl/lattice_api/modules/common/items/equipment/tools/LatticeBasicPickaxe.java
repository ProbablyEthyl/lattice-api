package net.ethyl.lattice_api.modules.common.items.equipment.tools;

import net.ethyl.lattice_api.core.content.items.equipment.tools.BasicPickaxe;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeBasicPickaxe extends LatticeItem<PickaxeItem> {
    private final Tier tier;

    protected LatticeBasicPickaxe(@NotNull RegistryId registryId, @NotNull Supplier<PickaxeItem> deferredItem, @NotNull AppendableBuilder<? extends LatticeBasicPickaxe, ?> builder) {
        super(registryId, deferredItem, builder);
        this.tier = builder.getTier();
    }

    public Tier getTier() {
        return this.tier;
    }

    public static AppendableBuilder<? extends LatticeBasicPickaxe, ?> builder() {
        return new AppendableBuilder<>(LatticeBasicPickaxe::new, BasicPickaxe::new);
    }

    public static class AppendableBuilder<I extends LatticeBasicPickaxe, B extends AppendableBuilder<I, B>> extends LatticeItem.AppendableBuilder.Tool<PickaxeItem, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, Supplier<PickaxeItem>, B, I> latticeFactory, @NotNull Function<B, PickaxeItem> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }
}
