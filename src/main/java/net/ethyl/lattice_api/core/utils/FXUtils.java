package net.ethyl.lattice_api.core.utils;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class FXUtils {
    public static <T extends ParticleOptions> void sendParticle(ServerLevel serverLevel, Player player, T type, double posX, double posY, double posZ, int particleCount, double xOffset, double yOffset, double zOffset, double speed) {
        if (serverLevel.isClientSide) return;

        ClientboundLevelParticlesPacket particlesPacket = new ClientboundLevelParticlesPacket(type, true, posX, posY, posZ, (float) xOffset, (float) yOffset, (float) zOffset, (float) speed, particleCount);

        for (Player target : serverLevel.players()) {
            if (target.distanceTo(player) <= 250.0f) {
                if (target instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(particlesPacket);
                }
            }
        }
    }

    public static EntityHitResult rayCastEntity(@NotNull ServerLevel serverLevel, @NotNull LivingEntity livingEntity, @NotNull Vec3 pos, @NotNull Vec3 direction, double step, double inflation, @NotNull Predicate<Entity> filter) {
        direction = direction.scale(step);

        List<Entity> hits = serverLevel.getEntities(livingEntity, new AABB(pos, pos.add(direction)).inflate(inflation), entity -> entity != livingEntity);

        return ProjectileUtil.getEntityHitResult(serverLevel, livingEntity, pos, pos.add(direction), new AABB(pos, pos.add(direction)).inflate(inflation), filter);
    }

    public static List<Entity> rayCastEntities(@NotNull ServerLevel serverLevel, @NotNull LivingEntity livingEntity, @NotNull Vec3 pos, @NotNull Vec3 direction, double step, double inflation, @NotNull Predicate<Entity> filter) {
        return serverLevel.getEntities(livingEntity, new AABB(pos, pos.add(direction.scale(step))).inflate(inflation), filter);
    }

    public static BlockHitResult rayCastBlock(@NotNull ServerLevel serverLevel, @NotNull LivingEntity livingEntity, @NotNull Vec3 pos, @NotNull Vec3 direction, double step, @NotNull ClipContext.Block blockCollider, @NotNull ClipContext.Fluid fluidCollider) {
        return serverLevel.clip(new ClipContext(pos, pos.add(direction.scale(step)), blockCollider, fluidCollider, livingEntity));
    }
}
