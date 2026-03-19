package net.ethyl.lattice_api.modules.base;

import net.ethyl.lattice_api.core.instances.objects.LatticeBuilder;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class LatticeTrimMaterial extends LatticeObject {
    private final ResourceKey<TrimMaterial> resourceKey;
    private final Supplier<Item> trimIngredient;
    private final String hexColor;
    private final float itemModelIndex;
    private final Map<Holder<ArmorMaterial>, String> overrides;

    protected LatticeTrimMaterial(@NotNull RegistryId registryId, @NotNull ResourceKey<TrimMaterial> resourceKey, @NotNull AppendableBuilder<? extends LatticeTrimMaterial, ?> builder) {
        super(registryId);
        this.resourceKey = resourceKey;
        this.trimIngredient = builder.trimIngredient;
        this.hexColor = builder.hexColor;
        this.itemModelIndex = builder.itemModelIndex;
        this.overrides = builder.overrides;
    }

    public ResourceKey<TrimMaterial> get() {
        return this.resourceKey;
    }

    public Item getTrimIngredient() {
        return this.trimIngredient.get();
    }

    public float getModelIndex() {
        return this.itemModelIndex;
    }

    public void bootstrap(@NotNull BootstrapContext<TrimMaterial> context) {
        ResourceLocation resourceLocation = this.resourceKey.location();
        context.register(
                this.resourceKey,
                TrimMaterial.create(
                        resourceLocation.getPath(),
                        this.trimIngredient.get(),
                        this.itemModelIndex,
                        Component.translatable(Util.makeDescriptionId("trim_material", resourceLocation))
                                .withStyle(Style.EMPTY.withColor(TextColor.parseColor(this.hexColor)
                                        .getOrThrow())),
                        this.overrides
                )
        );
    }

    public static AppendableBuilder<? extends LatticeTrimMaterial, ?> builder() {
        return new AppendableBuilder<>(LatticeTrimMaterial::new);
    }

    public static class AppendableBuilder<I extends LatticeTrimMaterial, B extends AppendableBuilder<I, B>> extends LatticeBuilder.Complex<I, ResourceKey<TrimMaterial>, B> {
        private Supplier<Item> trimIngredient = () -> Items.STONE;
        private String hexColor = "#FFFFFF";
        private float itemModelIndex = 0.1f;
        private final Map<Holder<ArmorMaterial>, String> overrides = new HashMap<>();

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, ResourceKey<TrimMaterial>, B, ? extends I> latticeFactory) {
            super(latticeFactory);
        }

        public B trimIngredient(@NotNull LatticeBlock<?> latticeBlock) {
            return this.trimIngredient(latticeBlock::asItem);
        }

        public B trimIngredient(@NotNull Block block) {
            return this.trimIngredient(block::asItem);
        }

        public B trimIngredient(@NotNull LatticeItem<?> latticeItem) {
            return this.trimIngredient(latticeItem::get);
        }

        public B trimIngredient(@NotNull Item item) {
            return this.trimIngredient(() -> item);
        }

        protected B trimIngredient(@NotNull Supplier<Item> itemSupplier) {
            this.trimIngredient = itemSupplier;

            return this.self();
        }

        public B hexColor(@NotNull String hexColor) {
            this.hexColor = hexColor;

            return this.self();
        }

        public B itemModelIndex(float itemModelIndex) {
            this.itemModelIndex = itemModelIndex;

            return this.self();
        }

        public B override(@NotNull Holder<ArmorMaterial> armorMaterial, @NotNull String overrideId) {
            this.overrides.put(armorMaterial, overrideId);

            return this.self();
        }
    }
}