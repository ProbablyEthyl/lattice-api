package net.ethyl.lattice_api.core.data;

import net.ethyl.lattice_api.modules.common.other.fx.LatticeFX;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class LatticeFXManager {
    private static final Collection<LatticeFX> ACTIVE_FX = new ArrayList<>();

    public static void generate(@NotNull LatticeFX latticeFX, @NotNull ServerLevel serverLevel, @NotNull Player player) {
        ACTIVE_FX.add(latticeFX.generate(serverLevel, player));
    }

    public static void tick() {
        ACTIVE_FX.removeIf(LatticeFX::tick);
    }
}