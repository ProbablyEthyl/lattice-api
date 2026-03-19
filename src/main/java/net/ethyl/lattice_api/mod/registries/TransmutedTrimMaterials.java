package net.ethyl.lattice_api.mod.registries;

import net.ethyl.lattice_api.modules.common.trims.TransmutedTrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;

public class TransmutedTrimMaterials {
    public static void transmute() {
        TransmutedTrimMaterial.transmute(TrimMaterials.QUARTZ);
        TransmutedTrimMaterial.transmute(TrimMaterials.IRON);
        TransmutedTrimMaterial.transmute(TrimMaterials.NETHERITE);
        TransmutedTrimMaterial.transmute(TrimMaterials.REDSTONE);
        TransmutedTrimMaterial.transmute(TrimMaterials.COPPER);
        TransmutedTrimMaterial.transmute(TrimMaterials.GOLD);
        TransmutedTrimMaterial.transmute(TrimMaterials.EMERALD);
        TransmutedTrimMaterial.transmute(TrimMaterials.DIAMOND);
        TransmutedTrimMaterial.transmute(TrimMaterials.LAPIS);
        TransmutedTrimMaterial.transmute(TrimMaterials.AMETHYST);
    }
}
