package net.ethyl.lattice_api.modules.common.tags;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
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
    private LatticeBlockTag(@NotNull RegistryId registryId, @NotNull TagKey<Block> tagKey, @NotNull AppendableBuilder<? extends LatticeTag<Block>, ?> builder) {
        super(registryId, tagKey, builder);
    }

    public static AppendableBuilder<? extends LatticeBlockTag, ?> builder() {
        return new AppendableBuilder<>(LatticeBlockTag::new, BlockTags::create);
    }

    public static class AppendableBuilder<I extends LatticeTag<Block>, B extends AppendableBuilder<I, B>> extends LatticeTag.AppendableBuilder<Block, I, B> {
        protected AppendableBuilder(@NotNull TriFunction<RegistryId, TagKey<Block>, B, I> latticeFactory, @NotNull Function<ResourceLocation, TagKey<Block>> tagKeyFactory) {
            super(latticeFactory, tagKeyFactory);
        }

        public B add(@NotNull LatticeBlockTag latticeBlockTag) {
            this.keyTagContent.add(latticeBlockTag::get);

            return this.self();
        }

        public B add(@NotNull LatticeBlock<?> latticeBlock) {
            return this.add(latticeBlock::get);
        }

        public B add(@NotNull Block block) {
            return this.add(() -> block);
        }

        private B add(@NotNull Supplier<Block> blockSupplier) {
            this.tagContent.add(blockSupplier);

            return this.self();
        }
    }
}
