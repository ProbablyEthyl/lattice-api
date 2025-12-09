package net.ethyl.lattice_api.modules.common.items.equipment.tier;

import net.ethyl.lattice_api.LatticeApi;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeBlock;
import net.ethyl.lattice_api.modules.base.LatticeItem;
import net.ethyl.lattice_api.modules.base.LatticeObject;
import net.ethyl.lattice_api.modules.common.tags.LatticeBlockTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class LatticeTier extends LatticeObject {
    protected final Tier tier;

    protected LatticeTier(@NotNull RegistryId registryId, @NotNull AppendableBuilder<? extends LatticeTier, ?> builder) {
        super(registryId);

        if (builder.latticeTag == null) LatticeApi.incompleteObjectErr(registryId);

        this.tier = builder.generate();
    }

    public Tier get() {
        return this.tier;
    }

    public static Builder builder() {
        return new Builder(LatticeTier::new);
    }

    public static class Builder extends AppendableBuilder<LatticeTier, Builder> {
        protected Builder(@NotNull BiFunction<RegistryId, Builder, LatticeTier> latticeFactory) {
            super(latticeFactory);
        }
    }

    public static class AppendableBuilder<I extends LatticeTier, B extends AppendableBuilder<I, B>> {
        private final BiFunction<RegistryId, B, I> latticeFactory;
        private LatticeBlockTag latticeTag = null;
        private int uses = 100;
        private float speed = 1f;
        private float attackDamageBonus = 1f;
        private int enchantmentValue = 30;
        private Supplier<Ingredient> repairIngredient = () -> Ingredient.of(Items.DIAMOND);

        @SuppressWarnings("unchecked")
        protected B self() {
            return (B) this;
        }

        protected AppendableBuilder(@NotNull BiFunction<RegistryId, B, I> latticeFactory) {
            this.latticeFactory = latticeFactory;
        }

        public Tier generate() {
            return new SimpleTier(this.latticeTag.get(), this.uses, this.speed, this.attackDamageBonus, this.enchantmentValue, this.repairIngredient);
        }

        public B latticeTag(@NotNull LatticeBlockTag latticeTag) {
            this.latticeTag = latticeTag;

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

        public I build(@NotNull RegistryId registryId) {
            return this.latticeFactory.apply(registryId, this.self());
        }
    }
}
