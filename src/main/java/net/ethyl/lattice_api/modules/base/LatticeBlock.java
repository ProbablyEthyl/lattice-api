package net.ethyl.lattice_api.modules.base;

import net.ethyl.lattice_api.core.data.LatticeRegistries;
import net.ethyl.lattice_api.core.instances.LatticeBuilder;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.common.types.lootTypes.LatticeToolType;
import net.ethyl.lattice_api.modules.common.types.modelTypes.LatticeBlockModelType;
import net.ethyl.lattice_api.modules.common.types.lootTypes.LatticeLootTable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LatticeBlock<T extends Block> extends LatticeObject {
    protected final DeferredBlock<T> deferredBlock;
    protected final LatticeBlockModelType modelType;
    protected final LatticeLootTable lootType;
    protected final LatticeToolType toolType;
    protected final boolean hasDescription;
    protected final BlockBehaviour.Properties blockProperties;
    protected final Item.Properties blockItemProperties;
    protected final Supplier<Block> defaultBlock;

    protected LatticeBlock(@NotNull RegistryId registryId, @NotNull DeferredBlock<T> deferredBlock, @NotNull AppendableBuilder<T, ? extends LatticeBlock<T>, ?> builder) {
        super(registryId);
        this.deferredBlock = deferredBlock;
        this.modelType = builder.modelType;
        this.lootType = builder.lootType;
        this.toolType = builder.toolType;
        this.hasDescription = builder.hasDescription;
        this.blockProperties = builder.blockProperties;
        this.blockItemProperties = builder.blockItemProperties;
        this.defaultBlock = builder.defaultBlock == null ? this.deferredBlock::get : builder.defaultBlock;
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

    public LatticeToolType getToolType() {
        return this.toolType;
    }

    public boolean hasDescription() {
        return this.hasDescription;
    }

    public BlockBehaviour.Properties getBlockProperties() {
        return this.blockProperties;
    }

    public Item.Properties getBlockItemProperties() {
        return this.blockItemProperties;
    }

    public Supplier<Block> getDefaultBlock() {
        return this.defaultBlock;
    }

    public static class AppendableBuilder<T extends Block, I extends LatticeBlock<T>, B extends AppendableBuilder<T, I, B>> extends LatticeBuilder.Complex<I, DeferredBlock<T>, B> {
        private final Function<B, T> blockFactory;

        protected LatticeBlockModelType modelType = LatticeRegistries.Types.Block.BASIC;
        protected LatticeLootTable lootType = LatticeRegistries.Types.LootTable.SELF;
        protected LatticeToolType toolType = LatticeRegistries.Types.ToolType.PICKAXE;
        private boolean hasDescription = false;
        protected BlockBehaviour.Properties blockProperties = BlockBehaviour.Properties.of().strength(1f);
        protected Item.Properties blockItemProperties = new Item.Properties().stacksTo(64);
        protected Supplier<Block> defaultBlock = () -> Blocks.STONE;

        protected AppendableBuilder(@NotNull TriFunction<RegistryId, DeferredBlock<T>, B, I> latticeFactory, @NotNull Function<B, T> blockFactory) {
            super(latticeFactory);
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
            this.lootType = lootType.drop(itemSupplier).minDrops(minDrops).maxDrops(maxDrops);

            return this.self();
        }

        public B toolType(@NotNull LatticeToolType toolType) {
            this.toolType = toolType;

            return this.self();
        }

        public boolean getHasDescription() {
            return this.hasDescription;
        }

        public B hasDescription() {
            this.hasDescription = true;

            return this.self();
        }

        public BlockBehaviour.Properties getBlockProperties() {
            return this.blockProperties;
        }

        public B blockProperties(@NotNull BlockBehaviour.Properties blockProperties) {
            this.blockProperties = blockProperties;

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

        public B requiresCorrectTool() {
            this.blockProperties.requiresCorrectToolForDrops();

            return this.self();
        }

        public B noCollision() {
            this.blockProperties.noCollission();

            return this.self();
        }

        public B noOcclusion() {
            this.blockProperties.noOcclusion();

            return this.self();
        }

        public Item.Properties getBlockItemProperties() {
            return this.blockItemProperties;
        }

        public B blockItemProperties(@NotNull Item.Properties blockItemProperties) {
            this.blockItemProperties = blockItemProperties;

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

        public Supplier<Block> getDefaultBlock() {
            return this.defaultBlock;
        }

        public B defaultBlock(@NotNull LatticeBlock<?> latticeBlock) {
            this.defaultBlock = latticeBlock::get;

            return this.self();
        }

        public B defaultBlock(@NotNull Block block) {
            this.defaultBlock = () -> block;

            return this.self();
        }

//        protected I build(@NotNull LatticeBlock<T> latticeBlock) {
//            return this.build(latticeBlock.getRegistryId(), latticeBlock.getDeferred());
//        }

//        public I build(@NotNull RegistryId registryId, @NotNull DeferredBlock<T> deferredBlock) {
//            return this.latticeFactory.apply(registryId, deferredBlock, this.self());
//        }
    }
}
