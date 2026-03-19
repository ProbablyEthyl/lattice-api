package net.ethyl.lattice_api.modules.base;

import net.ethyl.lattice_api.core.instances.objects.LatticeBuilder;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimPattern;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

public class LatticeTrimPattern extends LatticeObject {
    private final ResourceKey<TrimPattern> resourceKey;
    private final Holder<Item> trimItem;

    protected LatticeTrimPattern(@NotNull RegistryId registryId, @NotNull ResourceKey<TrimPattern> resourceKey, @NotNull AppendableBuilder<? extends LatticeTrimPattern, ?> builder) {
        super(registryId);
        this.resourceKey = resourceKey;
        this.trimItem = builder.trimItem;
    }

    public ResourceKey<TrimPattern> getResourceKey() {
        return this.resourceKey;
    }

    public Item getTrimItem() {
        return this.trimItem.value();
    }

    public void bootstrap(@NotNull BootstrapContext<TrimPattern> context) {
        ResourceLocation resourceLocation = this.resourceKey.location();
        context.register(
                this.resourceKey,
                new TrimPattern(
                        resourceLocation,
                        this.trimItem,
                        Component.translatable(Util.makeDescriptionId("trim_pattern", resourceLocation)),
                        false
                )
        );
    }

    public static AppendableBuilder<? extends LatticeTrimPattern, ?> builder() {
        return new AppendableBuilder<>(LatticeTrimPattern::new);
    }

    public static class AppendableBuilder<I extends LatticeTrimPattern, B extends AppendableBuilder<I, B>> extends LatticeBuilder.Complex<I, ResourceKey<TrimPattern>, B> {
        private Holder<Item> trimItem;

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, ResourceKey<TrimPattern>, B, ? extends I> latticeFactory) {
            super(latticeFactory);
        }

        public B setTrimItem(@NotNull Holder<Item> trimItem) {
            this.trimItem = trimItem;

            return this.self();
        }

//        public B trimItem(@NotNull LatticeBlock<?> latticeBlock) {
//            return this.trimItem(latticeBlock::asItem);
//        }
//
//        public B trimItem(@NotNull Block block) {
//            return this.trimItem(block::asItem);
//        }
//
//        public B trimItem(@NotNull LatticeItem<?> latticeItem) {
//            return this.trimItem(latticeItem::get);
//        }
//
//        public B trimItem(@NotNull Item item) {
//            return this.trimItem(() -> item);
//        }
//
//        protected B trimItem(@NotNull Supplier<Item> itemSupplier) {
//            this.trimItem = itemSupplier;
//
//            return this.self();
//        }
    }
}
