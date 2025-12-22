package net.ethyl.lattice_api.core.data;

import net.ethyl.lattice_api.LatticeApi;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.core.utils.RegistryUtils;
import net.ethyl.lattice_api.modules.base.*;
import net.ethyl.lattice_api.modules.common.items.equipment.tier.LatticeTier;
import net.ethyl.lattice_api.modules.common.other.fx.LatticeFX;
import net.ethyl.lattice_api.modules.common.tabs.LatticeCreativeTab;
import net.ethyl.lattice_api.modules.common.types.lootTypes.LatticeToolType;
import net.ethyl.lattice_api.modules.common.types.modelTypes.LatticeBlockModelType;
import net.ethyl.lattice_api.modules.common.types.modelTypes.LatticeItemModelType;
import net.ethyl.lattice_api.modules.common.types.lootTypes.LatticeLootTable;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;

public class LatticeRegistries {
    private static final Collection<LatticeTag<?>> tags = new LinkedList<>();
    private static final Collection<LatticeType> modelTypes = new LinkedList<>();
    private static final Collection<LatticeItem<?>> items = new LinkedList<>();
    private static final Collection<LatticeBlock<?>> blocks = new LinkedList<>();
    private static final Collection<LatticeCreativeTab> tabs = new LinkedList<>();
    private static final Collection<LatticeTier> tiers = new LinkedList<>();
    private static final Collection<LatticeFX> fx = new LinkedList<>();

    public static Tags createTags(@NotNull String modId) {
        return new Tags(modId);
    }

    public static Types createTypes(@NotNull String modId) {
        return new Types(modId);
    }

    public static Items createItems(@NotNull String modId) {
        return new Items(modId);
    }

    public static Blocks createBlocks(@NotNull String modId) {
        return new Blocks(modId);
    }

    public static Tabs createTabs(@NotNull String modId) {
        return new Tabs(modId);
    }

    public static Tiers createTiers(@NotNull String modId) {
        return new Tiers(modId);
    }

    public static FX createFX(@NotNull String modId) {
        return new FX(modId);
    }

    public static class Tags extends LatticeRegistry<LatticeTag<?>> {
        private Tags(@NotNull String modId) {
            super(modId);
        }

        public <T, I extends LatticeTag<T>> I register(@NotNull String id, @NotNull LatticeTag.AppendableBuilder<T, I, ?> builder) {
            I latticeTag = builder.build(createRegistryId(this, id));
            this.registryContent.add(latticeTag);

            return latticeTag;
        }

        @Override
        public void register(@NotNull IEventBus modEventBus) {
            this.registryContent.forEach(latticeTag -> checkDuplicate(tags, latticeTag.getRegistryId()));

            tags.addAll(this.registryContent);
        }
    }

    public static class Types extends LatticeRegistry<LatticeType> {
        private static final Types BUILT_IN = createTypes(LatticeApi.MOD_ID);

        private static final LatticeItemModelType BASIC_ITEM = BUILT_IN.register("basic_item", LatticeItemModelType.builder());
        private static final LatticeItemModelType HANDHELD_ITEM = BUILT_IN.register("handheld_item", LatticeItemModelType.builder());
        private static final LatticeBlockModelType BASIC_BLOCK = BUILT_IN.register("basic_block", LatticeBlockModelType.builder());
        private static final LatticeBlockModelType WITH_SIDES_BLOCK = BUILT_IN.register("with_sides_block", LatticeBlockModelType.builder());
        private static final LatticeLootTable LOOT_DROP_SELF = BUILT_IN.register("loot_drop_self", LatticeLootTable.builder());
        private static final LatticeLootTable LOOT_DROP_OTHER = BUILT_IN.register("loot_drop_other", LatticeLootTable.builder());
        private static final LatticeLootTable LOOT_DROP_SILK_TOUCH = BUILT_IN.register("loot_drop_silk_touch", LatticeLootTable.builder());
        private static final LatticeLootTable LOOT_DROP_AMOUNT = BUILT_IN.register("loot_drop_amount", LatticeLootTable.builder());
        private static final LatticeLootTable LOOT_DROP_NONE = BUILT_IN.register("loot_drop_none", LatticeLootTable.builder());
        private static final LatticeToolType TOOL_TYPE_PICKAXE = BUILT_IN.register("tool_type_pickaxe", LatticeToolType.builder());
        private static final LatticeToolType TOOL_TYPE_AXE = BUILT_IN.register("tool_type_axe", LatticeToolType.builder());
        private static final LatticeToolType TOOL_TYPE_SHOVEL = BUILT_IN.register("tool_type_shovel", LatticeToolType.builder());
        private static final LatticeToolType TOOL_TYPE_HOE = BUILT_IN.register("tool_type_hoe", LatticeToolType.builder());

        public static class Item {
            public static final LatticeItemModelType BASIC = Types.BASIC_ITEM;
            public static final LatticeItemModelType HANDHELD = Types.HANDHELD_ITEM;
        }

        public static class Block {
            public static final LatticeBlockModelType BASIC = Types.BASIC_BLOCK;
            public static final LatticeBlockModelType WITH_SIDES = Types.WITH_SIDES_BLOCK;
        }

        public static class LootTable {
            public static final LatticeLootTable SELF = Types.LOOT_DROP_SELF;
            public static final LatticeLootTable OTHER = Types.LOOT_DROP_OTHER;
            public static final LatticeLootTable SILK_TOUCH = Types.LOOT_DROP_SILK_TOUCH;
            public static final LatticeLootTable AMOUNT = Types.LOOT_DROP_AMOUNT;
            public static final LatticeLootTable NONE = Types.LOOT_DROP_NONE;
        }

        public static class ToolType {
            public static final LatticeToolType PICKAXE = Types.TOOL_TYPE_PICKAXE;
            public static final LatticeToolType AXE = Types.TOOL_TYPE_AXE;
            public static final LatticeToolType SHOVEL = Types.TOOL_TYPE_SHOVEL;
            public static final LatticeToolType HOE = Types.TOOL_TYPE_HOE;
        }

        private Types(@NotNull String modId) {
            super(modId);
        }

        public <I extends LatticeType> I register(@NotNull String id, @NotNull LatticeType.AppendableBuilder<I, ?> builder) {
            I latticeType = builder.build(createRegistryId(this, id));
            this.registryContent.add(latticeType);

            return latticeType;
        }

        @Override
        public void register(@NotNull IEventBus modEventBus) {
            this.registryContent.forEach(latticeModelType -> checkDuplicate(modelTypes, latticeModelType.getRegistryId()));

            modelTypes.addAll(this.registryContent);
        }
    }

    public static class Items extends LatticeRegistry<LatticeItem<?>> {
        private final DeferredRegister.Items ITEMS;

        private Items(@NotNull String modId) {
            super(modId);
            this.ITEMS = DeferredRegister.createItems(modId);
        }

        public <T extends Item, I extends LatticeItem<T>> I register(@NotNull String id, @NotNull LatticeItem.AppendableBuilder<T, I, ?> builder) {
            I latticeItem = builder.build(createRegistryId(this, id), this.ITEMS.register(id, builder::generate));
            this.registryContent.add(latticeItem);

            return latticeItem;
        }

        @Override
        public void register(@NotNull IEventBus modEventBus) {
            this.registryContent.forEach(latticeItem -> checkDuplicate(items, latticeItem.getRegistryId()));

            this.ITEMS.register(modEventBus);
            items.addAll(this.registryContent);
        }
    }

    public static class Blocks extends LatticeRegistry<LatticeBlock<?>> {
        private final DeferredRegister.Blocks BLOCKS;
        private final DeferredRegister.Items BLOCK_ITEMS;

        private Blocks(@NotNull String modId) {
            super(modId);
            this.BLOCKS = DeferredRegister.createBlocks(modId);
            this.BLOCK_ITEMS = DeferredRegister.createItems(modId);
        }

        public <T extends Block, I extends LatticeBlock<T>> I register(@NotNull String id, @NotNull LatticeBlock.AppendableBuilder<T, I, ?> builder) {
            I latticeBlock = builder.build(createRegistryId(this, id), this.BLOCKS.register(id, builder::generate));
            this.BLOCK_ITEMS.register(id, () -> new BlockItem(latticeBlock.get(), builder.getBlockItemProperties()));
            this.registryContent.add(latticeBlock);

            return latticeBlock;
        }

        @Override
        public void register(@NotNull IEventBus modEventBus) {
            this.registryContent.forEach(latticeBlock -> {
                checkDuplicate(blocks, latticeBlock.getRegistryId());
                checkDuplicate(items, latticeBlock.getRegistryId());
            });

            this.BLOCKS.register(modEventBus);
            this.BLOCK_ITEMS.register(modEventBus);
            blocks.addAll(this.registryContent);
        }
    }

    public static class Tabs extends LatticeRegistry<LatticeCreativeTab> {
        private final DeferredRegister<CreativeModeTab> TABS;

        private Tabs(@NotNull String modId) {
            super(modId);
            this.TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modId);
        }

        public <I extends LatticeCreativeTab> I register(@NotNull String id, @NotNull LatticeCreativeTab.AppendableBuilder<I, ?> builder) {
            RegistryId registryId = createRegistryId(this, id);
            I latticeTab = builder.build(registryId, this.TABS.register(id, () -> RegistryUtils.createTab(builder, registryId)));
            this.registryContent.add(latticeTab);

            return latticeTab;
        }

        @Override
        public void register(@NotNull IEventBus modEventBus) {
            this.registryContent.forEach(latticeTab -> checkDuplicate(tabs, latticeTab.getRegistryId()));

            this.TABS.register(modEventBus);
            tabs.addAll(this.registryContent);
        }
    }

    public static class Tiers extends LatticeRegistry<LatticeTier> {
        protected Tiers(@NotNull String modId) {
            super(modId);
        }

        public <I extends LatticeTier> I register(@NotNull String id, @NotNull LatticeTier.AppendableBuilder<I, ?> builder) {
            RegistryId registryId = createRegistryId(this, id);
            I latticeTier = builder.build(registryId);
            this.registryContent.add(latticeTier);

            return latticeTier;
        }

        @Override
        public void register(@NotNull IEventBus modEventBus) {
            this.registryContent.forEach(latticeTier -> checkDuplicate(tiers, latticeTier.getRegistryId()));

            tiers.addAll(this.registryContent);
        }
    }

    public static class FX extends LatticeRegistry<LatticeFX> {
        protected FX(@NotNull String modId) {
            super(modId);
        }

        public<I extends LatticeFX> I register(@NotNull String id, @NotNull LatticeFX.AppendableBuilder<I, ?> builder) {
            RegistryId registryId = createRegistryId(this, id);
            I latticeFX = builder.build(registryId);
            this.registryContent.add(latticeFX);

            return latticeFX;
        }

        @Override
        public void register(@NotNull IEventBus modEventBus) {
            this.registryContent.forEach(latticeFX -> checkDuplicate(fx, latticeFX.getRegistryId()));

            fx.addAll(this.registryContent);
        }
    }

    public static Collection<LatticeTag<?>> getTags() {
        return new LinkedList<>(tags);
    }

    public static Collection<LatticeType> getModelTypes() {
        return new LinkedList<>(modelTypes);
    }

    public static Collection<LatticeItem<?>> getItems() {
        return new LinkedList<>(items);
    }

    public static Collection<LatticeBlock<?>> getBlocks() {
        return new LinkedList<>(blocks);
    }

    public static Collection<LatticeCreativeTab> getTabs() {
        return new LinkedList<>(tabs);
    }

    public static Collection<LatticeTier> getTiers() {
        return new LinkedList<>(tiers);
    }

    public static Collection<LatticeFX> getFX() {
        return new LinkedList<>(fx);
    }

    public static <R extends LatticeRegistry<? extends LatticeObject>> RegistryId createRegistryId(@NotNull R registry, @NotNull String path) {
        RegistryId registryId = RegistryId.create(registry.modId, path);
        checkDuplicate(registry.registryContent, registryId);

        return registryId;
    }

    public static void checkDuplicate(@NotNull Collection<? extends LatticeObject> collection, @NotNull RegistryId registryId) {
        RegistryUtils.checkDuplicate(collection, registryId);
    }

    public static void register(@NotNull IEventBus modEventBus) {
        Types.BUILT_IN.register(modEventBus);
    }
}