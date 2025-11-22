package net.ethyl.lattice_api.modules.common.tags;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeItemTag extends LatticeTag<Item> {
    private LatticeItemTag(@NotNull RegistryId registryId, @NotNull TagKey<Item> tagKey, @NotNull Builder builder) {
        super(registryId, tagKey, builder);
    }

    public static Builder builder() {
        return new Builder(LatticeItemTag::new, ItemTags::create);
    }

    public static class Builder extends LatticeTag.Builder<Item, LatticeItemTag, Builder> {
        protected Builder(@NotNull TriFunction<RegistryId, TagKey<Item>, Builder, LatticeItemTag> latticeFactory, @NotNull Function<ResourceLocation, TagKey<Item>> tagKeyFactory) {
            super(latticeFactory, tagKeyFactory);
        }

        public Builder add(@NotNull Item item) {
            return this.add(() -> item);
        }

        private Builder add(@NotNull Supplier<Item> item) {
            this.tagContent.add(item);

            return this;
        }
    }
}
