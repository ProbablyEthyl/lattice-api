package net.ethyl.lattice_api.modules.base;

import net.ethyl.lattice_api.core.data.LatticeRegistries;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.common.types.modelTypes.LatticeBlockModelType;
import net.ethyl.lattice_api.modules.common.types.other.LatticeLootTable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeBlock<T extends Block> extends LatticeObject {
    private final DeferredBlock<T> deferredBlock;
    private final LatticeBlockModelType modelType;
    private final LatticeLootTable lootType;

    protected LatticeBlock(@NotNull RegistryId registryId, @NotNull DeferredBlock<T> deferredBlock, @NotNull LatticeBlock.AppendableBuilder<T, ? extends LatticeBlock<T>, ?> builder) {
        super(registryId);
        this.deferredBlock = deferredBlock;
        this.modelType = builder.modelType;
        this.lootType = builder.lootType;
    }

    public DeferredBlock<T> getDeferred() {
        return this.deferredBlock;
    }

    public T get() {
        return this.getDeferred().get();
    }

    public Item asItem() {
        return this.getDeferred().asItem();
    }

    public LatticeBlockModelType getModelType() {
        return this.modelType;
    }

    public LatticeLootTable getLootType() {
        return this.lootType;
    }

    public static class AppendableBuilder<T extends Block, I extends LatticeBlock<T>, B extends AppendableBuilder<T, I, B>> {
        private final TriFunction<RegistryId, DeferredBlock<T>, B, I> latticeFactory;
        private final Function<B, T> blockFactory;
        private LatticeBlockModelType modelType = LatticeRegistries.Types.Block.BASIC;
        private LatticeLootTable lootType = LatticeRegistries.Types.LootTable.SELF;
        public boolean hasDescription = false;
        public final BlockBehaviour.Properties blockProperties = BlockBehaviour.Properties.of().strength(1f);
        public final Item.Properties blockItemProperties = new Item.Properties().stacksTo(64);

        @SuppressWarnings("unchecked")
        protected B self() {
            return (B) this;
        }

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<T>, B, I> latticeFactory, @NotNull Function<B, T> blockFactory) {
            this.latticeFactory = latticeFactory;
            this.blockFactory = blockFactory;
        }

        public T generate() {
            return this.blockFactory.apply(this.self());
        }

        public B modelType(@NotNull LatticeBlockModelType modelType) {
            this.modelType = modelType;

            return this.self();
        }

        public B lootType(@NotNull LatticeLootTable lootType) {
            if (lootType == LatticeRegistries.Types.LootTable.NONE) {
                this.blockProperties.noLootTable();

                return this.self();
            }

            return this.lootType(lootType, () -> Items.STONE, 1f, 1f);
        }

        public B lootType(@NotNull Item item) {
            return this.lootType(() -> item);
        }

        public B lootType(@NotNull LatticeItem<?> latticeItem) {
            return this.lootType(latticeItem::get);
        }

        private B lootType(@NotNull LatticeBlock<?> latticeBlock) {
            return this.lootType(latticeBlock::asItem);
        }

        private B lootType(@NotNull Supplier<Item> itemSupplier) {
            return this.lootType(LatticeRegistries.Types.LootTable.OTHER, itemSupplier, 1f, 1f);
        }

        public B lootType(@NotNull LatticeItem<?> latticeItem, float minDrops, float maxDrops) {
            return this.lootType(latticeItem::get, minDrops, maxDrops);
        }

        public B lootType(@NotNull LatticeBlock<?> latticeBlock, float minDrops, float maxDrops) {
            return this.lootType(latticeBlock::asItem, minDrops, maxDrops);
        }

        public B lootType(@NotNull Item item, float minDrops, float maxDrops) {
            return this.lootType(() -> item, minDrops, maxDrops);
        }

        private B lootType(@NotNull Supplier<Item> itemSupplier, float minDrops, float maxDrops) {
            return this.lootType(LatticeRegistries.Types.LootTable.AMOUNT, itemSupplier, minDrops, maxDrops);
        }

        public B lootType(@NotNull LatticeLootTable lootType, @NotNull Supplier<Item> itemSupplier, float minDrops, float maxDrops) {
            this.lootType = lootType;
            this.lootType.drop = itemSupplier;
            this.lootType.minDrops = minDrops;
            this.lootType.maxDrops = maxDrops;

            return this.self();
        }

        public B hasDescription() {
            this.hasDescription = true;

            return this.self();
        }

        public B strength(float strength) {
            this.blockProperties.strength(strength);

            return this.self();
        }

        public B instaBreak() {
            this.blockProperties.instabreak();

            return this.self();
        }

        public B canIgnite() {
            this.blockProperties.ignitedByLava();

            return this.self();
        }

        public B stackSize(int stackSize) {
            this.blockItemProperties.stacksTo(stackSize);

            return this.self();
        }

        public B fireResistant() {
            this.blockItemProperties.fireResistant();

            return this.self();
        }

        public I build(@NotNull RegistryId registryId, @NotNull DeferredBlock<T> deferredBlock) {
            return this.latticeFactory.apply(registryId, deferredBlock, this.self());
        }
    }
}
