package net.ethyl.lattice_api.modules.common.items.equipment.weapons;

import net.ethyl.lattice_api.core.content.items.equipment.weapons.BasicSword;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeBasicSword extends LatticeItem<SwordItem> {
    private final Tier tier;

    protected LatticeBasicSword(@NotNull RegistryId registryId, @NotNull Supplier<SwordItem> deferredItem, @NotNull AppendableBuilder<? extends LatticeBasicSword, ?> builder) {
        super(registryId, deferredItem, builder);
        this.tier = builder.getTier();
    }

    public Tier getTier() {
        return this.tier;
    }

    public static AppendableBuilder<? extends LatticeBasicSword, ?> builder() {
        return new AppendableBuilder<>(LatticeBasicSword::new, BasicSword::new);
    }


    public static class AppendableBuilder<I extends LatticeBasicSword, B extends AppendableBuilder<I, B>> extends LatticeItem.AppendableBuilder.Tool<SwordItem, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, Supplier<SwordItem>, B, I> latticeFactory, @NotNull Function<B, SwordItem> itemFactory) {
            super(latticeFactory, itemFactory);
        }
    }
}
