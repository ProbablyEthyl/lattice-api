package net.ethyl.lattice_api.core.instances.objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.ethyl.lattice_api.core.utils.utility.CoreUtils;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class LatticeAnimation {
    private final float length;
    private final Map<String, Track> tracks = new HashMap<>();

    private LatticeAnimation(float length) {
        this.length = length;
    }

    public float getLength() {
        return this.length;
    }

    public void addTrack(@NotNull String id, @NotNull Track track) {
        this.tracks.put(id, track);
    }

    public Map<String, Track> getTracks() {
        return this.tracks;
    }

    public static class Track {
        private final String trackName;
        private final Map<Float, Keyframe> keyframes = new HashMap<>();

        private Track(@NotNull String trackName) {
            this.trackName = trackName;
        }

        public String getTrackName() {
            return this.trackName;
        }

        public Map<Float, Keyframe> getKeyframes() {
            return this.keyframes;
        }

        public Keyframe getKeyframe(float time) {
            return this.keyframes.get(time);
        }

        public boolean hasOnTime(float time) {
            return this.keyframes.containsKey(time);
        }

        public void addKeyframe(@NotNull Keyframe keyframe) {
            this.keyframes.put(keyframe.getTime(), keyframe);
        }
    }

    public static class Keyframe {
        private final float time;
        public float rotX = 0.0f, rotY = 0.0f, rotZ = 0.0f;

        private Keyframe(float time) {
            this.time = time;
        }

        public float getTime() {
            return this.time;
        }

        public void setRot(float x, float y, float z) {
            this.rotX = x;
            this.rotY = y;
            this.rotZ = z;
        }
    }

    public static Map<String, LatticeAnimation> fromRegistryId(@NotNull RegistryId registryId, @NotNull String... path) {
        ResourceLocation resourceLocation = CoreUtils.toAssetLoc(".json", registryId, path);
        JsonObject jsonData = CoreUtils.getJson(resourceLocation, "Failed to load animation");

        try {
            if (!jsonData.has("animations")) {
                throw new IllegalArgumentException("Json is not an animation in " + resourceLocation);
            }

            Map<String, LatticeAnimation> animations = new HashMap<>();

            for (String animationId : jsonData.getAsJsonObject("animations").keySet()) {
                animations.put(animationId, fromRegistryId(animationId, registryId, path));
            }

            return animations;
        } catch (Exception error) {
            throw new RuntimeException("Failed to load animation: " + resourceLocation, error);
        }
    }

    public static LatticeAnimation fromRegistryId(@NotNull String animationId, @NotNull RegistryId registryId, @NotNull String... path) {
        ResourceLocation resourceLocation = CoreUtils.toAssetLoc(".json", registryId, path);
        JsonObject jsonData = CoreUtils.getJson(resourceLocation, "Failed to load animation");

        try {
            if (!jsonData.has("animations")) {
                throw new IllegalArgumentException("Animation '" + animationId + "' has no animations in " + resourceLocation);
            }

            JsonObject animations = jsonData.getAsJsonObject("animations");

            if (!animations.has(animationId)) {
                throw new IllegalArgumentException("Animation '" + animationId + "' not found in " + resourceLocation);
            }

            JsonObject animObject = animations.getAsJsonObject(animationId);

            if (!animObject.has("animation_length")) {
                throw new IllegalArgumentException("Animation '" + animationId + "' has no animation length set in " + resourceLocation);
            }

            float length = animObject.get("animation_length").getAsFloat();
            LatticeAnimation latticeAnimation = new LatticeAnimation(length);

            if (!animObject.has("bones")) {
                throw new IllegalArgumentException("Animation '" + animationId + "' has no bones set in " + resourceLocation);
            }

            for (Map.Entry<String, JsonElement> boneData : animObject.getAsJsonObject("bones").entrySet()) {
                String name = boneData.getKey();
                JsonObject data = boneData.getValue().getAsJsonObject();
                Track track = new Track(name);

                if (data.has("rotation")) {
                    JsonObject rotation = data.getAsJsonObject("rotation");

                    for (Map.Entry<String, JsonElement> keyframeData : rotation.entrySet()) {
                        float time = Float.parseFloat(keyframeData.getKey());
                        Keyframe keyframe = track.hasOnTime(time) ? track.getKeyframe(time) : new Keyframe(time);
                        JsonElement element = keyframeData.getValue();

                        if (element.isJsonArray()) {
                            JsonArray vector = element.getAsJsonArray();
                            keyframe.setRot(vector.get(0).getAsFloat(), vector.get(1).getAsFloat(), vector.get(2).getAsFloat());
                        }

                        track.addKeyframe(keyframe);
                    }
                }

                latticeAnimation.addTrack(name, track);
            }

            return latticeAnimation;
        } catch (Exception error) {
            throw new RuntimeException("Failed to load animation: " + resourceLocation, error);
        }
    }
}
