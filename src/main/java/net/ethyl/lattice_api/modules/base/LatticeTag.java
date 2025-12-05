package net.ethyl.lattice_api.modules.base;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeTag<T> extends LatticeObject {
    private final TagKey<T> tagKey;
    private final Collection<Supplier<T>> tagContent;

    protected LatticeTag(@NotNull RegistryId registryId, @NotNull TagKey<T> tagKey, @NotNull Builder<T, ? extends LatticeTag<T>, ?> builder) {
        super(registryId);
        this.tagKey = tagKey;
        this.tagContent = builder.tagContent;
    }

    public TagKey<T> get() {
        return this.tagKey;
    }

    public Collection<Supplier<T>> getTagContent() {
        return this.tagContent;
    }

    public boolean contains(@NotNull T instance) {
        return this.getTagContent().stream().anyMatch(element -> element.get().equals(instance));
    }

    public static abstract class Builder<T, I extends LatticeTag<T>, B extends Builder<T, I, B>> {
        public final Collection<Supplier<T>> tagContent = new LinkedList<>();
        private final TriFunction<RegistryId, TagKey<T>, B, I> latticeFactory;
        private final Function<ResourceLocation, TagKey<T>> tagKeyFactory;

        @SuppressWarnings("unchecked")
        protected B self() {
            return (B) this;
        }

        protected Builder(@NotNull TriFunction<RegistryId, TagKey<T>, B, I> latticeFactory, @NotNull Function<ResourceLocation, TagKey<T>> tagKeyFactory) {
            this.latticeFactory = latticeFactory;
            this.tagKeyFactory = tagKeyFactory;
        }

        public I build(@NotNull RegistryId registryId) {
            return latticeFactory.apply(registryId, this.tagKeyFactory.apply(registryId.toResourceLoc()), this.self());
        }
    }
}
