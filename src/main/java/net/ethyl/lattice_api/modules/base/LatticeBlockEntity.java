package net.ethyl.lattice_api.modules.base;

import com.mojang.blaze3d.vertex.PoseStack;
import net.ethyl.lattice_api.core.instances.objects.LatticeAnimation;
import net.ethyl.lattice_api.core.instances.objects.LatticeBuilder;
import net.ethyl.lattice_api.core.instances.objects.LatticeModel;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.core.utils.function.QuadConsumer;
import net.ethyl.lattice_api.core.utils.function.SeptConsumer;
import net.ethyl.lattice_api.core.utils.utility.CoreUtils;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeBlockEntity<T extends Block, BE extends BlockEntity> extends LatticeBlock<T> {
    private final AppendableBuilder<T, BE, ? extends LatticeBlockEntity<T, BE>, ?, ?> builder;
    private Supplier<BlockEntityType<BE>> blockEntityType;
    private final ModelLayerLocation layerLocation;

    @SuppressWarnings("unchecked")
    protected LatticeBlockEntity(@NotNull RegistryId registryId, @NotNull DeferredBlock<T> deferredBlock, @NotNull AppendableBuilder<T, BE, ? extends LatticeBlockEntity<T, BE>, ?, ?> builder) {
        super(registryId, deferredBlock, (LatticeBlock.AppendableBuilder<? extends T, ? extends LatticeBlock<T>, ?>) builder.getBlockBuilder());
        this.builder = builder;
        this.blockEntityType = builder.blockEntityType;
        this.layerLocation = new ModelLayerLocation(registryId.toResourceLoc(), "main");
    }

    public Supplier<BlockEntityType<BE>> getType() {
        return this.blockEntityType;
    }

    public void setType(Supplier<BlockEntityType<BE>> supplier) {
        this.blockEntityType = supplier;
    }

    public ModelLayerLocation getLayerLocation() {
        return this.layerLocation;
    }

    public Renderer<T, BE> createRendererDefinition(@NotNull BlockEntityRendererProvider.Context context) {
        return this.builder.generateRenderer(this.getRegistryId(), this.layerLocation, context);
    }

    public LayerDefinition createLayerDefinition() {
        return new LatticeModel(this.getRegistryId(), null).bake();
    }

    public static class AppendableBuilder<T extends Block, BE extends BlockEntity, I extends LatticeBlockEntity<T, BE>, BB extends LatticeBlock.AppendableBuilder<? extends Block, ? extends LatticeBlock<?>, ?>, B extends AppendableBuilder<T, BE, I, BB, B>> extends LatticeBuilder.Complex<I, DeferredBlock<T>, B> {
        private final Function<B, T> blockFactory;
        private final BB blockBuilder;
        private final TriFunction<BlockEntityType<BE>, BlockPos, BlockState, BE> blockEntityFactory;
        private Supplier<BlockEntityType<BE>> blockEntityType;
        private RenderShape renderShape = RenderShape.MODEL;
        private QuadConsumer<Level, BlockPos, BlockState, BlockEntity> onClientTick;
        private SeptConsumer<BlockEntity, Float, PoseStack, MultiBufferSource, Integer, Integer, Renderer<T, BE>> render;
        private Function<BE, AABB> renderBoundingBox;

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<T>, B, I> latticeFactory, @NotNull Function<B, T> blockFactory, @NotNull BB blockBuilder, @NotNull TriFunction<BlockEntityType<BE>, BlockPos, BlockState, BE> blockEntityFactory) {
            super(latticeFactory);
            this.blockFactory = blockFactory;
            this.blockBuilder = blockBuilder;
            this.blockEntityFactory = blockEntityFactory;
        }

        public BE buildBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
            return this.blockEntityFactory.apply(this.getType().get(), blockPos, blockState);
        }

        public Renderer<T, BE> generateRenderer(@NotNull RegistryId registryId, @NotNull ModelLayerLocation layerLocation, @NotNull BlockEntityRendererProvider.Context context) {
            return FMLEnvironment.dist == Dist.CLIENT ? new Renderer<>(this.self(), registryId, layerLocation, context) : null;
        }

        public B editBlock(Consumer<BB> consumer) {
            consumer.accept(this.blockBuilder);

            return this.self();
        }

        public BB getBlockBuilder() {
            return this.blockBuilder;
        }

        public Supplier<BlockEntityType<BE>> getType() {
            return this.blockEntityType;
        }

        public B type(@NotNull Supplier<BlockEntityType<BE>> supplier) {
            this.blockEntityType = supplier;

            return this.self();
        }

        public RenderShape getRenderShape() {
            return this.renderShape;
        }

        public B renderShape(@NotNull RenderShape renderShape) {
            this.renderShape = renderShape;

            return this.self();
        }

        public QuadConsumer<Level, BlockPos, BlockState, BlockEntity> getOnClientTick() {
            return this.onClientTick;
        }

        public B onClientTick(@NotNull QuadConsumer<Level, BlockPos, BlockState, BlockEntity> onClientTick) {
            this.onClientTick = onClientTick;

            return this.self();
        }

        public B render(@NotNull SeptConsumer<BlockEntity, Float, PoseStack, MultiBufferSource, Integer, Integer, Renderer<T, BE>> render) {
            this.render = render;

            return this.self();
        }

        public B renderBoundingBox(@NotNull Function<BE, AABB> renderBoundingBox) {
            this.renderBoundingBox = renderBoundingBox;

            return this.self();
        }

        public T generate() {
            return this.blockFactory.apply(this.self());
        }
    }

    public static class Renderer<T extends Block, BE extends BlockEntity> implements BlockEntityRenderer<BE> {
        private final RegistryId registryId;
        private final LatticeModel latticeModel;
        private final Map<String, LatticeAnimation> animations = new HashMap<>();
        private final SeptConsumer<BlockEntity, Float, PoseStack, MultiBufferSource, Integer, Integer, Renderer<T, BE>> render;
        private final Function<BE, AABB> renderBoundingBox;
        private String activeAnimation = null;

        public Renderer(@NotNull AppendableBuilder<T, BE, ? extends LatticeBlockEntity<T, BE>, ?, ?> builder, @NotNull RegistryId registryId, @NotNull ModelLayerLocation layerLocation, @NotNull BlockEntityRendererProvider.Context context) {
            this.registryId = registryId;
            this.latticeModel = new LatticeModel(registryId, context);
            this.render = builder.render;
            this.renderBoundingBox = builder.renderBoundingBox;
        }

        public LatticeModel getModel() {
            return this.latticeModel;
        }

        public void loadAnimations() {
            if (this.animations.isEmpty()) {
                this.animations.putAll(LatticeAnimation.fromRegistryId(this.registryId, "animations", "block_entity"));
            }
        }

        private LatticeAnimation getAnimation(@NotNull String animationId) {
            return this.animations.get(animationId);
        }

        public void setAnimation(@NotNull String animationId) {
            LatticeAnimation animation = this.getAnimation(animationId);

            if (animation == null) {
                this.activeAnimation = null;
                return;
            }

            this.activeAnimation = animationId;
        }

        public void playAnimation(float progress) {
            if (this.activeAnimation != null) {
                LatticeModel latticeModel = this.getModel();
                LatticeAnimation latticeAnimation = this.getAnimation(this.activeAnimation);
                float currentTime = progress * latticeAnimation.getLength();

                for (LatticeAnimation.Track track : latticeAnimation.getTracks().values().stream().toList()) {
                    String boneName = track.getTrackName();
                    float[] rotation = this.getBoneRotation(track, currentTime);
                    ModelPart bone = latticeModel.getModelPart(boneName);

                    bone.xRot = degToRad(rotation[0]);
                    bone.yRot = degToRad(rotation[1]);
                    bone.zRot = degToRad(rotation[2]);
                }
            }
        }

        private float[] getBoneRotation(@NotNull LatticeAnimation.Track track, float animationTime) {
            if (track.getKeyframes().isEmpty()) {
                return toFloatArray(0.0f, 0.0f, 0.0f);
            }

            List<Float> timestamps = new ArrayList<>(track.getKeyframes().keySet());
            timestamps.sort(Float::compareTo);
            float firstTime = timestamps.getFirst();
            float lastTime = timestamps.getLast();

            if (animationTime <= firstTime) {
                LatticeAnimation.Keyframe keyframe = track.getKeyframe(firstTime);

                return toFloatArray(keyframe.rotX, keyframe.rotY, keyframe.rotZ);
            }

            if (animationTime >= lastTime) {
                LatticeAnimation.Keyframe keyframe = track.getKeyframe(lastTime);

                return toFloatArray(keyframe.rotX, keyframe.rotY, keyframe.rotZ);
            }

            for (int i = 0; i < timestamps.size() - 1; i++) {
                float start = timestamps.get(i);
                float end = timestamps.get(i + 1);

                if (animationTime >= start && animationTime <= end) {
                    LatticeAnimation.Keyframe prevKeyframe = track.getKeyframe(start);
                    LatticeAnimation.Keyframe nextKeyframe = track.getKeyframe(end);
                    float factor = (animationTime - start) / (end - start);

                    return toFloatArray(prevKeyframe.rotX + factor * (nextKeyframe.rotX - prevKeyframe.rotX), prevKeyframe.rotY + factor * (nextKeyframe.rotY - prevKeyframe.rotY), prevKeyframe.rotZ + factor * (nextKeyframe.rotZ - prevKeyframe.rotZ));
                }
            }

            LatticeAnimation.Keyframe lastKeyframe = track.getKeyframe(lastTime);

            return toFloatArray(lastKeyframe.rotX, lastKeyframe.rotY, lastKeyframe.rotZ);
        }

        @Override
        public void render(@NotNull BE blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
            if (this.render != null) {
                this.render.accept(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay, this);
            }
        }

        @Override
        public @NotNull AABB getRenderBoundingBox(@NotNull BE blockEntity) {
            if (this.renderBoundingBox != null) {
                return this.renderBoundingBox.apply(blockEntity);
            }

            return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity);
        }

        private static float[] toFloatArray(float... floats) {
            return CoreUtils.toFloatArray(floats);
        }

        private static float degToRad(float degrees) {
            return CoreUtils.degToRad(degrees);
        }
    }
}