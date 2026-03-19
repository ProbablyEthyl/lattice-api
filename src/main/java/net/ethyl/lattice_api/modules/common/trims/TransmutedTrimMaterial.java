package net.ethyl.lattice_api.modules.common.trims;

import net.ethyl.lattice_api.core.data.LatticeRegistries;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeTrimMaterial;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.armortrim.TrimMaterial;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

public class TransmutedTrimMaterial extends LatticeTrimMaterial {
    protected TransmutedTrimMaterial(@NotNull RegistryId registryId, @NotNull ResourceKey<TrimMaterial> resourceKey, @NotNull AppendableBuilder<?> builder) {
        super(registryId, resourceKey, builder);
    }

    public static void transmute(@NotNull ResourceKey<TrimMaterial> resourceKey) {
        LatticeRegistries.transmute(transmutableBuilder().build(RegistryId.create(resourceKey), resourceKey));
    }

    @SuppressWarnings("unchecked")
    private static <B extends AppendableBuilder<B>> B transmutableBuilder() {
        return (B) new AppendableBuilder<>(TransmutedTrimMaterial::new);
    }

    public static class AppendableBuilder<B extends AppendableBuilder<B>> extends LatticeTrimMaterial.AppendableBuilder<TransmutedTrimMaterial, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, ResourceKey<TrimMaterial>, B, ? extends TransmutedTrimMaterial> latticeFactory) {
            super(latticeFactory);
        }
    }
}
