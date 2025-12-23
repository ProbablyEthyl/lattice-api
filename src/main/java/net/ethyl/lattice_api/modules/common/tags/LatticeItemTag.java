package net.ethyl.lattice_api.modules.common.tags;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeItem;
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
    private LatticeItemTag(@NotNull RegistryId registryId, @NotNull TagKey<Item> tagKey, @NotNull AppendableBuilder<? extends LatticeTag<Item>, ?> builder) {
        super(registryId, tagKey, builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AppendableBuilder<LatticeItemTag, Builder> {
        protected Builder() {
            super(LatticeItemTag::new, ItemTags::create);
        }
    }

    public static class AppendableBuilder<I extends LatticeTag<Item>, B extends AppendableBuilder<I, B>> extends LatticeTag.AppendableBuilder<Item, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, TagKey<Item>, B, I> latticeFactory, @NotNull Function<ResourceLocation, TagKey<Item>> tagKeyFactory) {
            super(latticeFactory, tagKeyFactory);
        }

        public B add(@NotNull LatticeItemTag latticeItemTag) {
            this.keyTagContent.add(latticeItemTag::get);

            return this.self();
        }

        public B add(@NotNull LatticeItem<?> latticeItem) {
            return this.add(latticeItem::get);
        }

        public B add(@NotNull Item item) {
            return this.add(() -> item);
        }

        private B add(@NotNull Supplier<Item> itemSupplier) {
            this.tagContent.add(itemSupplier);

            return this.self();
        }
    }
}
