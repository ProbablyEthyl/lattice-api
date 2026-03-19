package net.ethyl.lattice_api.mod.registries;

import net.ethyl.lattice_api.LatticeApi;
import net.ethyl.lattice_api.core.data.LatticeRegistries;
import net.ethyl.lattice_api.modules.common.types.lootTypes.LatticeLootTable;
import net.ethyl.lattice_api.modules.common.types.lootTypes.LatticeToolType;
import net.ethyl.lattice_api.modules.common.types.modelTypes.LatticeBlockModelType;
import net.ethyl.lattice_api.modules.common.types.modelTypes.LatticeItemModelType;
import net.neoforged.bus.api.IEventBus;
import org.jetbrains.annotations.NotNull;

public class LatticeTypes {
    private static final LatticeRegistries.Types BUILT_IN = LatticeRegistries.createTypes(LatticeApi.MOD_ID);

    private static final LatticeBlockModelType BASIC_BLOCK = BUILT_IN.register("basic_block", LatticeBlockModelType.builder());
    private static final LatticeBlockModelType WITH_SIDES_BLOCK = BUILT_IN.register("with_sides_block", LatticeBlockModelType.builder());
    private static final LatticeBlockModelType CUSTOM = BUILT_IN.register("custom_block", LatticeBlockModelType.builder());
    private static final LatticeItemModelType BASIC_ITEM = BUILT_IN.register("basic_item", LatticeItemModelType.builder());
    private static final LatticeItemModelType HANDHELD_ITEM = BUILT_IN.register("handheld_item", LatticeItemModelType.builder());
    private static final LatticeLootTable LOOT_DROP_SELF = BUILT_IN.register("loot_drop_self", LatticeLootTable.builder());
    private static final LatticeLootTable LOOT_DROP_OTHER = BUILT_IN.register("loot_drop_other", LatticeLootTable.builder());
    private static final LatticeLootTable LOOT_DROP_SILK_TOUCH = BUILT_IN.register("loot_drop_silk_touch", LatticeLootTable.builder());
    private static final LatticeLootTable LOOT_DROP_AMOUNT = BUILT_IN.register("loot_drop_amount", LatticeLootTable.builder());
    private static final LatticeLootTable LOOT_DROP_NONE = BUILT_IN.register("loot_drop_none", LatticeLootTable.builder());
    private static final LatticeToolType TOOL_TYPE_PICKAXE = BUILT_IN.register("tool_type_pickaxe", LatticeToolType.builder());
    private static final LatticeToolType TOOL_TYPE_AXE = BUILT_IN.register("tool_type_axe", LatticeToolType.builder());
    private static final LatticeToolType TOOL_TYPE_SHOVEL = BUILT_IN.register("tool_type_shovel", LatticeToolType.builder());
    private static final LatticeToolType TOOL_TYPE_HOE = BUILT_IN.register("tool_type_hoe", LatticeToolType.builder());

    public static class Block {
        public static final LatticeBlockModelType BASIC = LatticeTypes.BASIC_BLOCK;
        public static final LatticeBlockModelType WITH_SIDES = LatticeTypes.WITH_SIDES_BLOCK;
        public static final LatticeBlockModelType CUSTOM = LatticeTypes.CUSTOM;
    }

    public static class Item {
        public static final LatticeItemModelType BASIC = LatticeTypes.BASIC_ITEM;
        public static final LatticeItemModelType HANDHELD = LatticeTypes.HANDHELD_ITEM;
    }

    public static class LootTable {
        public static final LatticeLootTable SELF = LatticeTypes.LOOT_DROP_SELF;
        public static final LatticeLootTable OTHER = LatticeTypes.LOOT_DROP_OTHER;
        public static final LatticeLootTable SILK_TOUCH = LatticeTypes.LOOT_DROP_SILK_TOUCH;
        public static final LatticeLootTable AMOUNT = LatticeTypes.LOOT_DROP_AMOUNT;
        public static final LatticeLootTable NONE = LatticeTypes.LOOT_DROP_NONE;
    }

    public static class ToolType {
        public static final LatticeToolType PICKAXE = LatticeTypes.TOOL_TYPE_PICKAXE;
        public static final LatticeToolType AXE = LatticeTypes.TOOL_TYPE_AXE;
        public static final LatticeToolType SHOVEL = LatticeTypes.TOOL_TYPE_SHOVEL;
        public static final LatticeToolType HOE = LatticeTypes.TOOL_TYPE_HOE;
    }

    public static void register(@NotNull IEventBus modEventBus) {
        BUILT_IN.register(modEventBus);
    }
}
