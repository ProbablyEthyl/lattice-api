package net.ethyl.lattice_api.core.instances;

import net.ethyl.lattice_api.core.utils.FXUtils;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class FXLayer<T extends ParticleOptions> {
    private final T particle;
    private final int count;
    private final Vec3 offset;
    private final float speed;

    public FXLayer(@NotNull T particle, int count, @NotNull Vec3 offset, float speed) {
        this.particle = particle;
        this.count = count;
        this.offset = offset;
        this.speed = speed;
    }

    public FXLayer(@NotNull T particle, int count, float speed) {
        this.particle = particle;
        this.count = count;
        this.offset = new Vec3(0, 0, 0);
        this.speed = speed;
    }

    public FXLayer(@NotNull T particle, int count, @NotNull Vec3 offset) {
        this.particle = particle;
        this.count = count;
        this.offset = offset;
        this.speed = 0;
    }

    public FXLayer(@NotNull T particle, int count) {
        this.particle = particle;
        this.count = count;
        this.offset = new Vec3(0, 0, 0);
        this.speed = 0;
    }

    public void send(@NotNull ServerLevel serverLevel, @NotNull LivingEntity source, @NotNull Vec3 pos) {
        if (source instanceof Player player) {
            FXUtils.sendParticle(serverLevel, player, particle, pos.x, pos.y, pos.z, this.count, this.offset.x, this.offset.y, this.offset.z, this.speed);
        }
    }
}