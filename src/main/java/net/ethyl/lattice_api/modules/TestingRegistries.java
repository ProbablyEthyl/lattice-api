package net.ethyl.lattice_api.modules;

import net.ethyl.lattice_api.LatticeApi;
import net.ethyl.lattice_api.core.main.LatticeRegistries;
import net.ethyl.lattice_api.modules.common.tags.LatticeBlockTag;
import net.ethyl.lattice_api.modules.common.tags.LatticeItemTag;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import org.jetbrains.annotations.NotNull;

public class TestingRegistries {
    public static final LatticeRegistries.Tags TAGS = LatticeRegistries.createTags(LatticeApi.MOD_ID);

    public static final LatticeItemTag ITEM_TAG = TAGS.register("lattice_item_tag", LatticeItemTag.builder().add(Items.ACACIA_BOAT));
    public static final LatticeBlockTag BLOCK_TAG = TAGS.register("lattice_block_tag", LatticeBlockTag.builder().add(Blocks.BONE_BLOCK));

    public static void register(@NotNull IEventBus modEventBus) {
        TAGS.register(modEventBus);
    }
}
