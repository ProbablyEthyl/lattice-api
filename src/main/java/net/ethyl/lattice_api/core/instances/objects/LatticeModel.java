package net.ethyl.lattice_api.core.instances.objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.ethyl.lattice_api.core.utils.utility.CoreUtils;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class LatticeModel {
    private static final float[] EMPTY_FLOATS = toFloatArray(0.0f, 0.0f, 0.0f);
    protected final RegistryId registryId;
    protected final ModelLayerLocation layerLocation;
    protected final Map<String, ModelPart> modelParts = new HashMap<>();

    public LatticeModel(@NotNull RegistryId registryId, @Nullable BlockEntityRendererProvider.Context context) {
        this(registryId, "main", context);
    }

    public LatticeModel(@NotNull RegistryId registryId, @NotNull String layer, @Nullable BlockEntityRendererProvider.Context context) {
        this.registryId = registryId;
        this.layerLocation = new ModelLayerLocation(registryId.toResourceLoc(), layer);

        if (context != null) {
            this.modelParts.put("root", context.bakeLayer(this.layerLocation));
            this.loadModelParts();
        }
    }

    public ModelPart getRoot() {
        return this.getModelPart("root");
    }

    public ModelPart getModelPart(@NotNull String modelPartId) {
        return this.modelParts.get(modelPartId);
    }

    private void loadModelParts() {
        ResourceLocation resourceLocation = getBlock(this.registryId);
        JsonObject jsonData = CoreUtils.getJson(resourceLocation, "Failed to load model");

        try {
            if (jsonData.has("groups")) {
                getJsonArray(jsonData, "groups").forEach(groupElement -> {
                    if (groupElement.isJsonObject()) {
                        this.insertModelPart(resourceLocation, groupElement.getAsJsonObject(), this.getRoot());
                    }
                });
            }
        } catch (Exception error) {
            throw new RuntimeException("Failed to load model: " + resourceLocation, error);
        }
    }

    private void insertModelPart(@NotNull ResourceLocation resourceLocation, @NotNull JsonObject groupData, @NotNull ModelPart parent) {
        try {
            String groupName = getJsonString(groupData, "name");
            ModelPart modelPart = parent.getChild(groupName);
            this.modelParts.put(groupName, modelPart);
            JsonArray children = getJsonArray(groupData, "children");

            children.forEach(groupElement -> {
                if (groupElement.isJsonObject()) {
                    this.insertModelPart(resourceLocation, groupElement.getAsJsonObject(), modelPart);
                }
            });
        } catch (Exception error) {
            throw new RuntimeException("Failed to load model: " + resourceLocation, error);
        }
    }

    public LayerDefinition bake() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition rootDefinition = meshDefinition.getRoot();
        ResourceLocation resourceLocation = getBlock(this.registryId);
        JsonObject jsonData = CoreUtils.getJson(resourceLocation, "Failed to load model");

        try {
            float[] textureSize = has(jsonData, "texture_size") ? toFloatArray(jsonData, "texture_size") : toFloatArray(16.0f, 16.0f);

            if (has(jsonData, "groups")) {
                getJsonArray(jsonData, "groups").forEach(jsonElement -> this.bakeGroup(jsonData, textureSize, jsonElement.getAsJsonObject(), rootDefinition, EMPTY_FLOATS));
            } else {
                this.bakeGroup(jsonData, textureSize, null, rootDefinition, EMPTY_FLOATS);
            }

            return LayerDefinition.create(meshDefinition, (int) textureSize[0], (int) textureSize[1]);
        } catch (Exception error) {
            throw new RuntimeException("Failed to load model: " + resourceLocation, error);
        }
    }

    private void bakeGroup(@NotNull JsonObject jsonData, float[] textureSize, @Nullable JsonObject groupData, @NotNull PartDefinition parent, float[] parentOffset) {
        JsonArray elements = getJsonArray(jsonData, "elements");
        String groupName = groupData != null ? getJsonString(groupData, "name") : "main";
        float[] groupPivot = toFloatArray(0.0f, 0.0f, 0.0f);

        if (groupData != null) {
            if (has(groupData, "children")) {
                for (JsonElement jsonElement : getJsonArray(groupData, "children")) {
                    if (jsonElement.isJsonPrimitive()) {
                        JsonObject firstCube = elements.get(this.getFirstElement(groupData)).getAsJsonObject();
                        groupPivot = this.getOrigin(firstCube);
                        break;
                    }
                }
            }
        }

        PartDefinition groupPart = parent.addOrReplaceChild(
                groupName,
                CubeListBuilder.create(),
                PartPose.offset(
                        groupPivot[0] - parentOffset[0],
                        groupPivot[1] - parentOffset[1],
                        groupPivot[2] - parentOffset[2]
                )
        );

        JsonArray cubes = groupData != null ? getJsonArray(groupData, "children") : elements;
        int cubeIndex = 0;

        for (JsonElement jsonElement : cubes) {
            if (jsonElement.isJsonObject()) {
                this.bakeGroup(jsonData, textureSize, jsonElement.getAsJsonObject(), groupPart, groupPivot);
            } else {
                JsonObject cubeData = elements.get(jsonElement.getAsInt()).getAsJsonObject();
                this.bakeElement(cubeData, has(cubeData, "name") ? getJsonString(cubeData, "name") : "cube_" + cubeIndex++, groupPart, textureSize, groupPivot);
            }
        }
    }

    private void bakeElement(@NotNull JsonObject cubeData, @NotNull String cubeName, @NotNull PartDefinition group, float[] textureSize, float[] groupPivot) {
        CubeListBuilder cubeBuilder = CubeListBuilder.create();
        float[] uv = this.getUV(cubeData, textureSize);
        cubeBuilder.texOffs((int) uv[0], (int) uv[1]);
        float[] dimensions = this.getDimensions(cubeData);
        cubeBuilder.addBox(
                -dimensions[0] / 2 + groupPivot[0],
                -dimensions[1] / 2 + groupPivot[1],
                -dimensions[2] / 2 + groupPivot[2],
                dimensions[0],
                dimensions[1],
                dimensions[2]
        );
        float[] center = this.getCenter(cubeData);
        float[] rot = this.getRotation(cubeData);
        float[] rOffs = getRotationCompensation(center, this.getOrigin(cubeData), rot);
        group.addOrReplaceChild(
                cubeName,
                cubeBuilder,
                PartPose.offsetAndRotation(
                        center[0] + rOffs[0],
                        center[1] + rOffs[1],
                        center[2] + rOffs[2] - groupPivot[2] * 2,
                        0.0f - rot[0], 0.0f - rot[1], 0.0f + rot[2] + (float) Math.PI
                )
        );
    }

    private float[] getRotation(@NotNull JsonObject cubeData) {
        float rotX = 0.0f, rotY = 0.0f, rotZ = 0.0f;

        if (has(cubeData, "rotation")) {
            JsonObject rotationData = getJsonObject(cubeData, "rotation");

            if (has(rotationData, "angle") && has(rotationData, "axis")) {
                float radRot = CoreUtils.degToRad(rotationData.get("angle").getAsFloat());
                String axis = getJsonString(rotationData, "axis");

                switch (axis) {
                    case "x" -> rotX += radRot;
                    case "y" -> rotY += radRot;
                    case "z" -> rotZ += radRot;
                    default -> throw new IllegalArgumentException("Unexpected axis: " + axis);
                }
            }
        }

        return toFloatArray(rotX, rotY, rotZ);
    }

    private float[] getRotationCompensation(float[] center, float[] origin, float[] rot) {
        float x = center[0] - origin[0];
        float y = center[1] - origin[1];
        float z = center[2] - origin[2];
        float cosX = (float) Math.cos(rot[0]);
        float sinX = (float) Math.sin(rot[0]);
        float y1 = y * cosX - z * sinX;
        float z1 = y * sinX + z * cosX;
        float cosY = (float) Math.cos(rot[1]);
        float sinY = (float) Math.sin(rot[1]);
        float x2 = x * cosY + z1 * sinY;
        float cosZ = (float) Math.cos(rot[2]);
        float sinZ = (float) Math.sin(rot[2]);

        return toFloatArray((origin[0] + (x2 * cosZ - y1 * sinZ)) - center[0], (origin[1] + (x2 * sinZ + y1 * cosZ)) - center[1], (origin[2] + - x * sinY + z1 * cosY) - center[2]);
    }

    private int getFirstElement(@NotNull JsonObject groupData) {
        try {
            for (JsonElement jsonElement : getJsonArray(groupData, "children")) {
                if (jsonElement.isJsonPrimitive()) {
                    return jsonElement.getAsInt();
                }
            }

            throw new IllegalArgumentException("Group does not contain any elements");
        } catch (Exception error) {
            throw new RuntimeException("Failed to retrieve element", error);
        }
    }

    private float[] getDimensions(@NotNull JsonObject cubeData) {
        float[] from = toFloatArray(cubeData, "from");
        float[] to = toFloatArray(cubeData, "to");

        return toFloatArray(to[0] - from[0], to[1] - from[1], to[2] - from[2]);
    }

    private float[] getUV(@NotNull JsonObject cubeData, float[] textureSize) {
        float u = -1.0f, v = -1.0f;

        try {
            for (JsonElement jsonElement : getJsonObject(cubeData, "faces").entrySet().stream().map(Map.Entry::getValue).toList()) {
                float[] uv = toFloatArray(jsonElement.getAsJsonObject(), "uv");
                if (u == -1) {
                    u = uv[0];
                }

                if (v == -1) {
                    v = uv[1];
                }

                u = Math.min(uv[0], u);
                v = Math.min(uv[1], v);
            }

            return toFloatArray(u * ((textureSize[0] - (textureSize[0] % 16)) / 16), v * ((textureSize[1] - (textureSize[1] % 16)) / 16));
        } catch (Exception error) {
            throw new RuntimeException("Failed to get UV", error);
        }
    }

    private float[] getCenter(@NotNull JsonObject cubeData) {
        float[] from = toFloatArray(cubeData, "from");
        float[] to = toFloatArray(cubeData, "to");

        return toFloatArray((to[0] + from[0]) / 2, (to[1] + from[1]) / 2, (to[2] + from[2]) / 2);
    }

    private float[] getOrigin(@NotNull JsonObject cubeData) {
        try {
            if (has(cubeData, "rotation")) {
                JsonObject rotationData = getJsonObject(cubeData, "rotation");

                if (has(rotationData, "origin")) {
                    return toFloatArray(rotationData, "origin");
                } else {
                    throw new IllegalArgumentException("Element does not contain an origin");
                }
            } else {
                return toFloatArray(cubeData, "from");
            }
        } catch (Exception error) {
            throw new RuntimeException("Failed to get origin", error);
        }
    }

    private static boolean has(@NotNull JsonObject jsonObject, @NotNull String identifier) {
        return jsonObject.has(identifier);
    }

    private static JsonObject getJsonObject(@NotNull JsonObject jsonObject, @NotNull String identifier) {
        try {
            if (has(jsonObject, identifier)) {
                JsonElement jsonElement = jsonObject.get(identifier);

                if (jsonElement.isJsonObject()) {
                    return jsonElement.getAsJsonObject();
                } else {
                    throw new IllegalArgumentException("JsonElement '" + identifier + "' is not a JsonObject");
                }
            } else {
                throw new IllegalArgumentException("JsonObject does not contain '" + identifier + "'");
            }
        } catch (Exception error) {
            throw new RuntimeException("Failed to retrieve JsonObject", error);
        }
    }

    private static JsonArray getJsonArray(@NotNull JsonObject jsonObject, @NotNull String identifier) {
        try {
            if (has(jsonObject, identifier)) {
                JsonElement jsonElement = jsonObject.get(identifier);

                if (jsonElement.isJsonArray()) {
                    return jsonElement.getAsJsonArray();
                } else {
                    throw new IllegalArgumentException("JsonElement '" + identifier + "' is not a JsonArray");
                }
            } else {
                throw new IllegalArgumentException("JsonObject does not contain '" + identifier + "'");
            }
        } catch (Exception error) {
            throw new RuntimeException("Failed to retrieve JsonArray", error);
        }
    }

    private static float[] toFloatArray(@NotNull JsonObject jsonObject, @NotNull String identifier) {
        return toFloatArray(getJsonArray(jsonObject, identifier));
    }

    private static float[] toFloatArray(@NotNull JsonArray jsonArray) {
        return CoreUtils.toFloatArray(jsonArray);
    }

    private static float[] toFloatArray(float... floats) {
        return CoreUtils.toFloatArray(floats);
    }

    private static String getJsonString(@NotNull JsonObject jsonObject, @NotNull String identifier) {
        try {
            if (has(jsonObject, identifier)) {
                JsonElement jsonElement = jsonObject.get(identifier);

                if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString()) {
                    return jsonElement.getAsString();
                } else {
                    throw new IllegalArgumentException("JsonElement '" + identifier + "' is not a String");
                }
            } else {
                throw new IllegalArgumentException("JsonObject does not contain '" + identifier + "'");
            }
        } catch (Exception error) {
            throw new RuntimeException("Failed to retrieve String", error);
        }
    }

    private static ResourceLocation getBlock(@NotNull RegistryId registryId) {
        return CoreUtils.toAssetLoc(".json", registryId, "models", "block");
    }

    public void renderRoot(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        this.render(this.getRoot(), poseStack, multiBufferSource, packedLight, packedOverlay);
    }

    public void render(@NotNull ModelPart modelPart, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        this.render(modelPart, poseStack, multiBufferSource.getBuffer(RenderType.entitySolid(this.getResourceLocation())), packedLight, packedOverlay);
    }

    public void renderCutout(@NotNull ModelPart modelPart, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        this.render(modelPart, poseStack, multiBufferSource.getBuffer(RenderType.entityCutout(this.getResourceLocation())), packedLight, packedOverlay);
    }

    public void renderTranslucent(@NotNull ModelPart modelPart, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        this.render(modelPart, poseStack, multiBufferSource.getBuffer(RenderType.entityTranslucent(this.getResourceLocation())), packedLight, packedOverlay);
    }

    private void render(@NotNull ModelPart modelPart, @NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
        modelPart.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }

    private ResourceLocation getResourceLocation() {
        ResourceLocation resourceLocation = this.layerLocation.getModel();

        return ResourceLocation.fromNamespaceAndPath(resourceLocation.getNamespace(), "textures/block/" + resourceLocation.getPath() + ".png");
    }
}