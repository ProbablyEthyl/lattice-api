package net.ethyl.lattice_api.modules.common.items.equipment.tier;

import net.ethyl.lattice_api.core.instances.LatticeBuilder;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.core.utils.ErrUtils;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.base.LatticeObject;
import net.ethyl.lattice_api.modules.common.tags.LatticeBlockTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.SimpleTier;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class LatticeTier extends LatticeObject {
    protected final Tier tier;
    protected final Supplier<TagKey<Block>> tagKey;
    protected final int uses;
    protected final float speed;
    protected final float attackDamageBonus;
    protected final int enchantmentValue;
    protected final Supplier<Ingredient> repairIngredient;

    protected LatticeTier(@NotNull RegistryId registryId, @NotNull AppendableBuilder<? extends LatticeTier, ?> builder) {
        super(registryId);

        if (builder.tagKey == null) ErrUtils.incompleteObjectErr(registryId);

        this.tier = builder.generate();
        this.tagKey = builder.tagKey;
        this.uses = builder.uses;
        this.speed = builder.speed;
        this.attackDamageBonus = builder.attackDamageBonus;
        this.enchantmentValue = builder.enchantmentValue;
        this.repairIngredient = builder.repairIngredient;
    }

    public Tier get() {
        return this.tier;
    }

    public TagKey<Block> getTagKey() {
        return this.tagKey.get();
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.attackDamageBonus;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Supplier<Ingredient> getRepairIngredient() {
        return this.repairIngredient;
    }

    public static AppendableBuilder<LatticeTier, ?> builder() {
        return new AppendableBuilder<>(LatticeTier::new);
    }

    public static class AppendableBuilder<I extends LatticeTier, B extends AppendableBuilder<I, B>> extends LatticeBuilder.Advanced<I, B> {
        private Supplier<TagKey<Block>> tagKey = null;
        private int uses = 100;
        private float speed = 1f;
        private float attackDamageBonus = 1f;
        private int enchantmentValue = 30;
        private Supplier<Ingredient> repairIngredient = () -> Ingredient.of(Items.COBWEB);

        protected AppendableBuilder(@NotNull BiFunction<RegistryId, B, I> latticeFactory) {
            super(latticeFactory);
        }

        public Tier generate() {
            return new SimpleTier(this.tagKey.get(), this.uses, this.speed, this.attackDamageBonus, this.enchantmentValue, this.repairIngredient);
        }

        public B tag(@NotNull LatticeBlockTag latticeTag) {
            this.tagKey = latticeTag::get;

            return this.self();
        }

        public B tag(@NotNull TagKey<Block> tagKey) {
            this.tagKey = () -> tagKey;

            return this.self();
        }

        public B uses(int uses) {
            this.uses = uses;

            return this.self();
        }

        public B speed(float speed) {
            this.speed = speed;

            return this.self();
        }

        public B attackDamageBonus(float attackDamageBonus) {
            this.attackDamageBonus = attackDamageBonus;

            return this.self();
        }

        public B enchantmentValue(int enchantmentValue) {
            this.enchantmentValue = enchantmentValue;

            return this.self();
        }

        public B repairItem(@NotNull LatticeItem<?> latticeItem) {
            return this.repairItem(latticeItem::get);
        }

        public B repairItem(@NotNull LatticeBlock<?> latticeBlock) {
            return this.repairItem(latticeBlock::asItem);
        }

        public B repairItem(@NotNull Item item) {
            return this.repairItem(() -> item);
        }

        private B repairItem(@NotNull Supplier<Item> itemSupplier) {
            this.repairIngredient = () -> Ingredient.of(itemSupplier.get());

            return this.self();
        }

        public B repairItem(@NotNull Ingredient ingredient) {
            this.repairIngredient = () -> ingredient;

            return this.self();
        }
    }
}
