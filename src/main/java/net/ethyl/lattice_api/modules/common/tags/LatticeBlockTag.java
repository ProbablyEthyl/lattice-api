package net.ethyl.lattice_api.modules.common.tags;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeBlockTag extends LatticeTag<Block> {
    private LatticeBlockTag(@NotNull RegistryId registryId, @NotNull TagKey<Block> tagKey, @NotNull Builder builder) {
        super(registryId, tagKey, builder);
    }

    public static Builder builder() {
        return new Builder(LatticeBlockTag::new, BlockTags::create);
    }

    public static class Builder extends LatticeTag.Builder<Block, LatticeBlockTag, Builder> {
        protected Builder(@NotNull TriFunction<RegistryId, TagKey<Block>, Builder, LatticeBlockTag> latticeFactory, @NotNull Function<ResourceLocation, TagKey<Block>> tagKeyFactory) {
            super(latticeFactory, tagKeyFactory);
        }

        public Builder add(@NotNull Block block) {
            return this.add(() -> block);
        }

        private Builder add(@NotNull Supplier<Block> blockSupplier) {
            this.tagContent.add(blockSupplier);

            return this;
        }
    }
}
