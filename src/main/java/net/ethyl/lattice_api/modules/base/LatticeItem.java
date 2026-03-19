package net.ethyl.lattice_api.modules.base;

import net.ethyl.lattice_api.core.instances.objects.LatticeBuilder;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.mod.registries.LatticeTypes;
import net.ethyl.lattice_api.modules.common.items.equipment.tier.LatticeTier;
import net.ethyl.lattice_api.modules.common.items.items.TransmutedItem;
import net.ethyl.lattice_api.modules.common.types.modelTypes.LatticeItemModelType;
import net.minecraft.core.Holder;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeItem<T extends Item> extends LatticeObject {
    protected final Supplier<T> supplierItem;
    protected final LatticeItemModelType modelType;
    protected final Item.Properties itemProperties;
    protected final boolean hasDescription;

    protected LatticeItem(@NotNull RegistryId registryId, @NotNull Supplier<T> supplierItem, @NotNull LatticeItem.AppendableBuilder<T, ? extends LatticeItem<T>, ?> builder) {
        super(registryId);
        this.supplierItem = supplierItem;
        this.modelType = builder.modelType;
        this.itemProperties = builder.itemProperties;
        this.hasDescription = builder.hasDescription;
    }

    public boolean isTransmuted() {
        return this instanceof TransmutedItem;
    }

    public Supplier<T> getSupplier() {
        return this.supplierItem;
    }

    public T get() {
        return this.getSupplier().get();
    }

    public LatticeItemModelType getModelType() {
        return this.modelType;
    }

    public Item.Properties getItemProperties() {
        return this.itemProperties;
    }

    public boolean hasDescription() {
        return this.hasDescription;
    }

    public static class AppendableBuilder<T extends Item, I extends LatticeItem<T>, B extends AppendableBuilder<T, I, B>> extends LatticeBuilder.Complex<I, Supplier<T>, B> {
        private final Function<B, T> itemFactory;

        protected LatticeItemModelType modelType = LatticeTypes.Item.BASIC;
        protected Item.Properties itemProperties = new Item.Properties().stacksTo(64);
        private boolean hasDescription = false;

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, Supplier<T>, B, I> latticeFactory, @NotNull Function<B, T> itemFactory) {
            super(latticeFactory);
            this.itemFactory = itemFactory;
        }

        public B from(@NotNull I latticeItem) {
            return this.modelType(latticeItem.getModelType())
                    .itemProperties(latticeItem.getItemProperties())
                    .hasDescription(latticeItem.hasDescription());
        }

        public T generate() {
            return this.itemFactory.apply(this.self());
        }

        public B modelType(@NotNull LatticeItemModelType modelType) {
            this.modelType = modelType;

            return this.self();
        }

        public boolean getHasDescription() {
            return this.hasDescription;
        }

        public B itemProperties(@NotNull Item.Properties itemProperties) {
            this.itemProperties = itemProperties;

            return this.self();
        }

        public Item.Properties getItemProperties() {
            return this.itemProperties;
        }

        public B stackSize(int stackSize) {
            this.itemProperties.stacksTo(stackSize);

            return this.self();
        }

        public B fireResistant() {
            this.itemProperties.fireResistant();

            return this.self();
        }

        protected B hasDescription(boolean hasDescription) {
            return hasDescription ? this.hasDescription() : this.self();
        }

        public B hasDescription() {
            this.hasDescription = true;

            return this.self();
        }

        public static class Tool<T extends TieredItem, I extends LatticeItem<T>, B extends Tool<T, I, B>> extends AppendableBuilder<T, I, B> {
            protected Tier tier = Tiers.DIAMOND;

            protected Tool(@NotNull TriFunction<RegistryId, Supplier<T>, B, I> latticeFactory, @NotNull Function<B, T> itemFactory) {
                super(latticeFactory, itemFactory);
                this.modelType(LatticeTypes.Item.HANDHELD);
            }

            public B attribute(@NotNull ItemAttributeModifiers itemAttributeModifiers) {
                this.itemProperties.attributes(itemAttributeModifiers);

                return this.self();
            }

            public Tier getTier() {
                return this.tier;
            }

            public B tier(@NotNull LatticeTier latticeTier) {
                return this.tier(latticeTier.get());
            }

            public B tier(@NotNull Tier tier) {
                this.tier = tier;

                return this.self();
            }
        }

        public static class Armor<T extends ArmorItem, I extends LatticeItem<T>, B extends Armor<T, I, B>> extends AppendableBuilder<T, I, B> {
            private Holder<ArmorMaterial> armorMaterial = ArmorMaterials.DIAMOND;

            protected Armor(@NotNull TriFunction<RegistryId, Supplier<T>, B, I> latticeFactory, @NotNull Function<B, T> itemFactory) {
                super(latticeFactory, itemFactory);
            }

            protected B durabilityFactor(@NotNull ArmorItem.Type armorType, int durabilityFactor) {
                this.itemProperties.durability(armorType.getDurability(durabilityFactor));

                return this.self();
            }

            public Holder<ArmorMaterial> getArmorMaterial() {
                return this.armorMaterial;
            }

            public B armorMaterial(@NotNull Holder<ArmorMaterial> armorMaterial) {
                this.armorMaterial = armorMaterial;

                return this.self();
            }
        }
    }
}