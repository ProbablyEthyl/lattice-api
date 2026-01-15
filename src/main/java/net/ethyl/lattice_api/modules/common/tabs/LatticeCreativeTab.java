package net.ethyl.lattice_api.modules.common.tabs;

import net.ethyl.lattice_api.core.instances.LatticeBuilder;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.base.LatticeObject;
import net.ethyl.lattice_api.modules.common.tags.LatticeBlockTag;
import net.ethyl.lattice_api.modules.common.tags.LatticeItemTag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Supplier;

public class LatticeCreativeTab extends LatticeObject {
    protected final Supplier<CreativeModeTab> tabSupplier;
    protected final Collection<Supplier<ItemLike>> tabContent;
    protected final Supplier<ItemStack> icon;

    protected LatticeCreativeTab(@NotNull RegistryId registryId, @NotNull Supplier<CreativeModeTab> tabSupplier, @NotNull AppendableBuilder<? extends LatticeCreativeTab, ?> builder) {
        super(registryId);
        this.tabSupplier = tabSupplier;
        this.tabContent = builder.tabContent;
        this.icon = builder.icon;
    }

    public CreativeModeTab get() {
        return this.tabSupplier.get();
    }

    public Collection<Supplier<ItemLike>> getTabContent() {
        return this.tabContent;
    }

    public Supplier<ItemStack> getIcon() {
        return this.icon;
    }

    public static AppendableBuilder<? extends LatticeCreativeTab, ?> builder() {
        return new AppendableBuilder<>(LatticeCreativeTab::new);
    }

    public static class AppendableBuilder<I extends LatticeCreativeTab, B extends AppendableBuilder<I, B>> extends LatticeBuilder.Complex<I, Supplier<CreativeModeTab>, B> {
        protected Collection<Supplier<ItemLike>> tabContent = new LinkedList<>();
        protected Supplier<ItemStack> icon = () -> new ItemStack(Items.STONE);

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, Supplier<CreativeModeTab>, B, I> latticeFactory) {
            super(latticeFactory);
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

        public B add(@NotNull Collection<Supplier<ItemLike>> tabContent) {
            this.tabContent = tabContent;

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
    }
}
