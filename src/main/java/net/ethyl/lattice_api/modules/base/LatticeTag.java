package net.ethyl.lattice_api.modules.base;

import net.ethyl.lattice_api.core.instances.LatticeBuilder;
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
    protected final TagKey<T> tagKey;
    protected final Collection<Supplier<T>> tagContent;
    protected final Collection<Supplier<TagKey<T>>> keyTagContent;

    protected LatticeTag(@NotNull RegistryId registryId, @NotNull TagKey<T> tagKey, @NotNull AppendableBuilder<T, ? extends LatticeTag<T>, ?> builder) {
        super(registryId);
        this.tagKey = tagKey;
        this.tagContent = builder.tagContent;
        this.keyTagContent = builder.keyTagContent;
    }

    public TagKey<T> get() {
        return this.tagKey;
    }

    public Collection<Supplier<T>> getTagContent() {
        return this.tagContent;
    }

    public Collection<Supplier<TagKey<T>>> getKeyTagContent() {
        return this.keyTagContent;
    }

    public boolean contains(@NotNull T instance) {
        return this.getTagContent().stream().anyMatch(element -> element.get().equals(instance));
    }

    public static abstract class AppendableBuilder<T, I extends LatticeTag<T>, B extends AppendableBuilder<T, I, B>> extends LatticeBuilder.Complex<I, TagKey<T>, B> {
        private final Function<ResourceLocation, TagKey<T>> tagKeyFactory;
        protected Collection<Supplier<T>> tagContent = new LinkedList<>();
        protected Collection<Supplier<TagKey<T>>> keyTagContent = new LinkedList<>();

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, TagKey<T>, B, I> latticeFactory, @NotNull Function<ResourceLocation, TagKey<T>> tagKeyFactory) {
            super(latticeFactory);
            this.tagKeyFactory = tagKeyFactory;
        }

        public TagKey<T> generate(@NotNull RegistryId registryId) {
            return this.tagKeyFactory.apply(registryId.toResourceLoc());
        }

        public B add(@NotNull TagKey<T> tagKey) {
            return this.add(() -> tagKey);
        }

        private B add(@NotNull Supplier<TagKey<T>> tagKeySupplier) {
            this.keyTagContent.add(tagKeySupplier);

            return this.self();
        }
    }
}
