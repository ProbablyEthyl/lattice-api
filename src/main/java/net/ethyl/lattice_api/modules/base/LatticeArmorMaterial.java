package net.ethyl.lattice_api.modules.base;

import net.ethyl.lattice_api.core.instances.objects.LatticeBuilder;
import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.function.Supplier;

public class LatticeArmorMaterial extends LatticeObject {
    private final Holder<ArmorMaterial> armorMaterial;

    protected LatticeArmorMaterial(@NotNull RegistryId registryId, @NotNull DeferredHolder<ArmorMaterial, ?> armorMaterial, @NotNull AppendableBuilder<? extends LatticeArmorMaterial, ?> builder) {
        super(registryId);
        this.armorMaterial = armorMaterial.getDelegate();
    }

    public Holder<ArmorMaterial> get() {
        return this.armorMaterial;
    }

    public static AppendableBuilder<? extends LatticeArmorMaterial, ?> builder() {
        return new AppendableBuilder<>(LatticeArmorMaterial::new);
    }

    public static class AppendableBuilder<I extends LatticeArmorMaterial, B extends AppendableBuilder<I, B>> extends LatticeBuilder.Complex<I, DeferredHolder<ArmorMaterial, ?>, B> {
        private Holder<SoundEvent> equipSound = SoundEvents.ARMOR_EQUIP_DIAMOND;
        private Supplier<Ingredient> repairIngredient = () -> Ingredient.of(Items.STONE);
        private EnumMap<ArmorItem.Type, Integer> typeProtection = Util.make(new EnumMap<>(ArmorItem.Type.class), attribute -> {
            attribute.put(ArmorItem.Type.BOOTS, 3);
            attribute.put(ArmorItem.Type.LEGGINGS, 8);
            attribute.put(ArmorItem.Type.CHESTPLATE, 6);
            attribute.put(ArmorItem.Type.HELMET, 3);
            attribute.put(ArmorItem.Type.BODY, 8);
        });
        private int enchantmentValue = 16;
        private float toughness = 2.0f;
        private float knockbackResistance = 0.0f;

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredHolder<ArmorMaterial, ?>, B, ? extends I> latticeFactory) {
            super(latticeFactory);
        }

        public Holder<SoundEvent> getEquipSound() {
            return this.equipSound;
        }

        public B equipSound(@NotNull Holder<SoundEvent> soundEvent) {
            this.equipSound = soundEvent;

            return this.self();
        }

        public Supplier<Ingredient> getRepairIngredient() {
            return this.repairIngredient;
        }

        public B repairIngredient(@NotNull LatticeBlock<?> latticeBlock) {
            return this.repairIngredient(latticeBlock::asItem);
        }

        public B repairIngredient(@NotNull Block block) {
            return this.repairIngredient(block::asItem);
        }

        public B repairIngredient(@NotNull LatticeItem<?> latticeItem) {
            return this.repairIngredient(latticeItem::get);
        }

        public B repairIngredient(@NotNull Item item) {
            return this.repairIngredient(() -> item);
        }

        private B repairIngredient(@NotNull Supplier<Item> itemSupplier) {
            this.repairIngredient = () -> Ingredient.of(itemSupplier.get());

            return this.self();
        }

        public EnumMap<ArmorItem.Type, Integer> getTypeProtection() {
            return this.typeProtection;
        }

        public B typeProtection(@NotNull EnumMap<ArmorItem.Type, Integer> typeProtection) {
            this.typeProtection = typeProtection;

            return this.self();
        }

        public int getEnchantmentValue(){
            return this.enchantmentValue;
        }

        public B enchantmentValue(int enchantmentValue) {
            this.enchantmentValue = enchantmentValue;

            return this.self();
        }

        public float getToughness() {
            return this.toughness;
        }

        public B toughness(float toughness) {
            this.toughness = toughness;

            return this.self();
        }

        public float getKnockbackResistance() {
            return this.knockbackResistance;
        }

        public B knockbackResistance(float knockbackResistance) {
            this.knockbackResistance = knockbackResistance;

            return this.self();
        }
    }
}
