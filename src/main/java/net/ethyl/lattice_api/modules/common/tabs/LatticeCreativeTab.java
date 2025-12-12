package net.ethyl.lattice_api.modules.common.tabs;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.base.LatticeObject;
import net.ethyl.lattice_api.modules.common.tags.LatticeBlockTag;
import net.ethyl.lattice_api.modules.common.tags.LatticeItemTag;
import net.minecraft.data.tags.VanillaBlockTagsProvider;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class LatticeCreativeTab extends LatticeObject {
    protected final Supplier<CreativeModeTab> tabSupplier;

    protected LatticeCreativeTab(@NotNull RegistryId registryId, @NotNull Supplier<CreativeModeTab> tabSupplier) {
        super(registryId);
        this.tabSupplier = tabSupplier;
    }

    public CreativeModeTab get() {
        return this.tabSupplier.get();
    }

    public static Builder builder() {
        return new Builder(LatticeCreativeTab::new);
    }

    public static class Builder extends AppendableBuilder<LatticeCreativeTab, Builder> {
        protected Builder(@NotNull BiFunction<RegistryId, Supplier<CreativeModeTab>, LatticeCreativeTab> latticeFactory) {
            super(latticeFactory);
        }
    }

    public static class AppendableBuilder<I extends LatticeCreativeTab, B extends AppendableBuilder<I, B>> {
        private final BiFunction<RegistryId, Supplier<CreativeModeTab>, I> latticeFactory;
        protected Collection<Supplier<ItemLike>> tabContent = new LinkedList<>();
        protected Supplier<ItemStack> icon = () -> new ItemStack(Items.STONE);

        @SuppressWarnings("unchecked")
        protected B self() {
            return (B) this;
        }

        protected AppendableBuilder(@NotNull BiFunction<RegistryId, Supplier<CreativeModeTab>, I> latticeFactory) {
            this.latticeFactory = latticeFactory;

        }

        public Collection<Supplier<ItemLike>> getTabContent() {
            return this.tabContent;
        }

        public B add(@NotNull LatticeItem<?> latticeItem) {
            return this.add(latticeItem::get);
        }

        public B add(@NotNull Item item) {
            return this.add(() -> item);
        }

        public B add(@NotNull LatticeBlock<?> latticeBlock) {
            return this.add(latticeBlock::asItem);
        }

        public B add(@NotNull Block block) {
            return this.add(() -> block);
        }

        public B add(@NotNull LatticeItemTag latticeItemTag) {
            latticeItemTag.getTagContent().forEach(itemSupplier -> this.tabContent.add(itemSupplier::get));

            return this.self();
        }

        public B add(@NotNull LatticeBlockTag latticeBlockTag) {
            latticeBlockTag.getTagContent().forEach(blockSupplier -> this.tabContent.add(blockSupplier::get));

            return this.self();
        }

        private B add(@NotNull Supplier<ItemLike> itemLikeSupplier) {
            this.tabContent.add(itemLikeSupplier);

            return this.self();
        }

        public Supplier<ItemStack> getIcon() {
            return this.icon;
        }

        public B icon(@NotNull LatticeItem<?> latticeItem) {
            return this.icon(() -> new ItemStack(latticeItem::get));
        }

        public B icon(@NotNull Item item) {
            return this.icon(() -> new ItemStack(item));
        }

        public B icon(@NotNull LatticeBlock<?> latticeBlock) {
            return this.icon(() -> new ItemStack(latticeBlock::asItem));
        }

        public B icon(@NotNull Block block) {
            return this.icon(() -> new ItemStack(block));
        }

        private B icon(@NotNull Supplier<ItemStack> itemStackSupplier) {
            this.icon = itemStackSupplier;

            return this.self();
        }

        public I build(@NotNull RegistryId registryId, @NotNull Supplier<CreativeModeTab> tabSupplier) {
            return this.latticeFactory.apply(registryId, tabSupplier);
        }
    }
}
