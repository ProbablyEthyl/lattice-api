package net.ethyl.lattice_api.modules.common.trims;

import net.ethyl.lattice_api.core.data.LatticeRegistries;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeTrimPattern;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimPattern;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

public class TransmutedTrimPattern extends LatticeTrimPattern {
    protected TransmutedTrimPattern(@NotNull RegistryId registryId, @NotNull ResourceKey<TrimPattern> resourceKey, @NotNull AppendableBuilder<?> builder) {
        super(registryId, resourceKey, builder);
    }

    public static void transmute(@NotNull ResourceKey<TrimPattern> resourceKey, @NotNull Item trimItem) {
        LatticeRegistries.transmute(transmutableBuilder().setTrimItem(Holder.direct(trimItem)).build(RegistryId.create(resourceKey), resourceKey));
    }

    @SuppressWarnings("unchecked")
    public static <B extends AppendableBuilder<B>> B transmutableBuilder() {
        return (B) new AppendableBuilder<>(TransmutedTrimPattern::new);
    }

    public static class AppendableBuilder<B extends AppendableBuilder<B>> extends LatticeTrimPattern.AppendableBuilder<TransmutedTrimPattern, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, ResourceKey<TrimPattern>, B, ? extends TransmutedTrimPattern> latticeFactory) {
            super(latticeFactory);
        }
    }
}
