package net.ethyl.lattice_api.modules.common.other.fx;

import net.ethyl.lattice_api.core.instances.FXLayer;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.core.utils.FXUtils;
import net.ethyl.lattice_api.modules.base.LatticeObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class LatticeFX extends LatticeObject {
    private final Collection<Properties> fxProperties;
    private final HashMap<Integer, Collection<FXLayer<?>>> fxLayers;
    private int tick = 0;
    private ServerLevel serverLevel = null;
    private Player player = null;

    protected LatticeFX(@NotNull RegistryId registryId, @NotNull AppendableBuilder<? extends LatticeFX, ?> builder) {
        super(registryId);
        this.fxProperties = builder.fxProperties;
        this.fxLayers = builder.fxLayers;
    }

    private LatticeFX(@NotNull RegistryId registryId, @NotNull Collection<Properties> fxProperties, @NotNull HashMap<Integer, Collection<FXLayer<?>>> fxLayers) {
        super(registryId);
        this.fxProperties = new LinkedList<>();

        for (Properties fxProperty : fxProperties) {
            this.fxProperties.add(fxProperty.generate());
        }

        this.fxLayers = fxLayers;
    }

    public LatticeFX cloneFX() {
        return new LatticeFX(this.getRegistryId(), this.fxProperties, this.fxLayers);
    }

    public LatticeFX generate(@NotNull ServerLevel serverLevel, @NotNull Player player) {
        return this.cloneFX().prepare(serverLevel, player);
    }

    private LatticeFX prepare(@NotNull ServerLevel serverLevel, @NotNull Player player) {
        this.serverLevel = serverLevel;
        this.player = player;

        return this;
    }

    public boolean tick() {
        this.tick++;

        for (Properties properties : this.fxProperties) {
            if (this.tick < properties.startingTick || properties.isFinished() || (serverLevel == null || player == null)) continue;

            properties.tick(this.serverLevel, this.player, this.fxLayers);

            if (this.tick >= properties.totalTicks) properties.finished = true;
        }

        return this.fxProperties.stream().allMatch(Properties::isFinished);
    }

    public static Properties properties() {
        return new Properties();
    }

    @Override
    public LatticeObject clone() {
        return this.cloneFX();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Properties {
        public ServerLevel serverLevel;
        public LivingEntity source = null;
        private boolean finished = false;
        public int startingTick = 0;
        public int totalTicks = 20;
        public double range = 20.0f;
        public Vec3 currentPos = null;
        public Vec3 direction = null;
        public double distanceTraveled = 0.0f;
        private Consumer<Properties> onTick = null;
        private HashMap<Integer, Collection<FXLayer<?>>> fxLayers;

        protected Properties() {}

        public void tick(@NotNull ServerLevel serverLevel, @NotNull LivingEntity livingEntity, @NotNull HashMap<Integer, Collection<FXLayer<?>>> fxLayers) {
            this.serverLevel = serverLevel;
            this.source = livingEntity;
            this.fxLayers = fxLayers;

            if (this.onTick != null) this.onTick.accept(this);
        }

        public Properties generate() {
            return new Properties().startingTick(this.startingTick).totalTicks(this.totalTicks).onTick(this.onTick).particles(this.fxLayers).range(this.range);
        }

        public EntityHitResult rayCastEntity(@NotNull Vec3 pos, @NotNull Vec3 direction, double step, double inflation, @NotNull Predicate<Entity> filter) {
            return FXUtils.rayCastEntity(this.serverLevel, this.source, pos, direction, step, inflation, filter);
        }

        public List<Entity> rayCastEntities(@NotNull Vec3 pos, @NotNull Vec3 direction, double step, double inflation, @NotNull Predicate<Entity> filter) {
            return FXUtils.rayCastEntities(this.serverLevel, this.source, pos, direction, step, inflation, filter);
        }

        public BlockHitResult rayCastBlock(@NotNull Vec3 pos, @NotNull Vec3 direction, double step, @NotNull ClipContext.Block blockCollider, ClipContext.Fluid fluidCollider) {
            return FXUtils.rayCastBlock(this.serverLevel, this.source, pos, direction, step, blockCollider, fluidCollider);
        }

        public void sendParticles(int layer, @NotNull BlockPos pos) {
            this.sendParticles(layer, new Vec3(pos.getX(), pos.getY(), pos.getZ()));
        }

        public void sendParticles(int layer, @NotNull Vec3 pos) {
            this.fxLayers.get(layer).forEach(fxLayer -> fxLayer.send(this.serverLevel, this.source, pos));
        }

        public boolean isFinished() {
            return this.finished;
        }

        public void finish() {
            this.finished = true;
        }

        public Properties startingTick(int startingTick) {
            this.startingTick = startingTick;

            return this;
        }

        public Properties totalTicks(int totalTicks) {
            this.totalTicks = totalTicks;

            return this;
        }

        public int getAbsoluteTicks() {
            return this.totalTicks - this.startingTick;
        }

        public Properties range(double range) {
            this.range = range;

            return this;
        }

        public Properties onTick(@NotNull Consumer<Properties> onTick) {
            this.onTick = onTick;

            return this;
        }

        private Properties particles(@NotNull HashMap<Integer, Collection<FXLayer<?>>> fxLayers) {
            this.fxLayers = fxLayers;

            return this;
        }
    }

    public static class Builder extends AppendableBuilder<LatticeFX, Builder> {
        protected Builder() {
            super(LatticeFX::new);
        }
    }

    public static class AppendableBuilder<I extends LatticeFX, B extends AppendableBuilder<I, B>> {
        private final BiFunction<RegistryId, B, I> latticeFactory;
        private final Collection<Properties> fxProperties = new LinkedList<>();
        private final HashMap<Integer, Collection<FXLayer<?>>> fxLayers = new HashMap<>();
        private int layer = 0;

        @SuppressWarnings("unchecked")
        protected B self() {
            return (B) this;
        }

        protected AppendableBuilder(@NotNull BiFunction<RegistryId, B, I> latticeFactory) {
            this.latticeFactory = latticeFactory;
        }

        public B fx(@NotNull Properties fxProperties, int startingTick, int totalTicks) {
            return this.fx(fxProperties, startingTick, totalTicks, fxProperties.range);
        }

        public B fx(@NotNull Properties fxProperties, ObjectUtils.Null startingTick, int totalTicks) {
            return this.fx(fxProperties, startingTick == null ? fxProperties.startingTick : 0, totalTicks, fxProperties.range);
        }

        public B fx(@NotNull Properties fxProperties, int startingTick, ObjectUtils.Null totalTicks) {
            return this.fx(fxProperties, startingTick, totalTicks == null ? fxProperties.totalTicks : 20, fxProperties.range);
        }

        public B fx(@NotNull Properties fxProperties, double range) {
            return this.fx(fxProperties, fxProperties.startingTick, fxProperties.totalTicks, range);
        }

        public B fx(@NotNull Properties fxProperties, int startingTick, int totalTicks, double range) {
            this.fxProperties.add(fxProperties.generate().startingTick(startingTick).totalTicks(totalTicks).range(range));

            return this.self();
        }

        public B particles(@NotNull FXLayer<?>... fxLayers) {
            this.fxLayers.put(this.layer++, Arrays.asList(fxLayers));

            return this.self();
        }

        public I build(@NotNull RegistryId registryId) {
            return this.latticeFactory.apply(registryId, this.self());
        }
    }
}