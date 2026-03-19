package net.ethyl.lattice_api.core.data;

import net.ethyl.lattice_api.core.instances.objects.RegistryId;
import net.ethyl.lattice_api.core.utils.utility.RegistryUtils;
import net.ethyl.lattice_api.modules.base.*;
import net.ethyl.lattice_api.modules.common.items.equipment.tier.LatticeTier;
import net.ethyl.lattice_api.modules.common.items.items.TransmutedItem;
import net.ethyl.lattice_api.modules.common.other.fx.LatticeFX;
import net.ethyl.lattice_api.modules.common.tabs.LatticeCreativeTab;
import net.ethyl.lattice_api.modules.common.trims.TransmutedTrimMaterial;
import net.ethyl.lattice_api.modules.common.trims.TransmutedTrimPattern;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class LatticeRegistries {
    private static final Collection<LatticeType> modelTypes = new LinkedList<>();
    private static final Collection<LatticeTag<?>> tags = new LinkedList<>();
    private static final Collection<LatticeRecipe> recipes = new LinkedList<>();
    private static final Collection<LatticeItem<?>> items = new LinkedList<>();
    private static final Collection<LatticeBlock<?>> blocks = new LinkedList<>();
    private static final Collection<LatticeCreativeTab> tabs = new LinkedList<>();
    private static final Collection<LatticeTier> tiers = new LinkedList<>();
    private static final Collection<LatticeFX> fx = new LinkedList<>();
    private static final Collection<LatticeBlockEntity<?, ?>> blockEntities = new LinkedList<>();
    private static final Collection<LatticeArmorMaterial> armorMaterials = new LinkedList<>();
    private static final Collection<LatticeTrimMaterial> trimMaterials = new LinkedList<>();
    private static final Collection<LatticeTrimPattern> trimPatterns = new LinkedList<>();

    public static Types createTypes(@NotNull String modId) {
        return new Types(modId);
    }

    public static Tags createTags(@NotNull String modId) {
        return new Tags(modId);
    }

    public static Recipes createRecipes(@NotNull String modId) {
        return new Recipes(modId);
    }

    public static void transmute(@NotNull TransmutedItem transmutedItem) {
        transmute(items, transmutedItem);
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

    public static BlockEntities createBlockEntities(@NotNull String modId) {
        return new BlockEntities(modId);
    }

    public static ArmorMaterials createArmorMaterials(@NotNull String modId) {
        return new ArmorMaterials(modId);
    }

    public static void transmute(@NotNull TransmutedTrimPattern transmutedTrimPattern) {
        transmute(trimPatterns, transmutedTrimPattern);
    }

    public static TrimMaterials createTrimMaterials(@NotNull String modId) {
        return new TrimMaterials(modId);
    }

    public static void transmute(@NotNull TransmutedTrimMaterial transmutedTrimMaterial) {
        transmute(trimMaterials, transmutedTrimMaterial);
    }

    public static TrimPatterns createTrimPatterns(@NotNull String modId) {
        return new TrimPatterns(modId);
    }

    public static class Types extends LatticeRegistry<LatticeType> {
        private Types(@NotNull String modId) {
            super(modId);
        }

        public <I extends LatticeType> I register(@NotNull String id, @NotNull LatticeType.AppendableBuilder<I, ?> builder) {
            I latticeType = builder.build(createRegistryId(this, id), null);
            this.registryContent.add(latticeType);

            return latticeType;
        }

        @Override
        public void register(@NotNull IEventBus modEventBus) {
            this.registryContent.forEach(latticeModelType -> checkDuplicate(modelTypes, latticeModelType.getRegistryId()));

            modelTypes.addAll(this.registryContent);
        }
    }

    public static class Tags extends LatticeRegistry<LatticeTag<?>> {
        private Tags(@NotNull String modId) {
            super(modId);
        }

        public <T, I extends LatticeTag<T>> I register(@NotNull String id, @NotNull LatticeTag.AppendableBuilder<T, I, ?> builder) {
            RegistryId registryId = createRegistryId(this, id);
            I latticeTag = builder.build(registryId, builder.generate(registryId));
            this.registryContent.add(latticeTag);

            return latticeTag;
        }

        @Override
        public void register(@NotNull IEventBus modEventBus) {
            this.registryContent.forEach(latticeTag -> checkDuplicate(tags, latticeTag.getRegistryId()));

            tags.addAll(this.registryContent);
        }
    }

    public static class Recipes extends LatticeRegistry<LatticeRecipe> {
        protected Recipes(@NotNull String modId) {
            super(modId);
        }

        public <I extends LatticeRecipe> I register(@NotNull String id, @NotNull LatticeRecipe.AppendableBuilder<I, ?> builder) {
            I latticeRecipe = builder.build(createRegistryId(this, id), null);
            this.registryContent.add(latticeRecipe);

            return latticeRecipe;
        }

        @Override
        public void register(@NotNull IEventBus modEventBus) {
            this.registryContent.forEach(latticeRecipe -> checkDuplicate(recipes, latticeRecipe.getRegistryId()));

            recipes.addAll(this.registryContent);
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

        public <T extends Block, I extends LatticeBlock<T>> I register(@NotNull String id, @NotNull LatticeBlock.AppendableBuilder<T, ? extends I, ?> builder) {
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
            I latticeTier = builder.build(createRegistryId(this, id), null);
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

        public <I extends LatticeFX> I register(@NotNull String id, @NotNull LatticeFX.AppendableBuilder<I, ?> builder) {
            I latticeFX = builder.build(createRegistryId(this, id), null);
            this.registryContent.add(latticeFX);

            return latticeFX;
        }

        @Override
        public void register(@NotNull IEventBus modEventBus) {
            this.registryContent.forEach(latticeFX -> checkDuplicate(fx, latticeFX.getRegistryId()));

            fx.addAll(this.registryContent);
        }
    }

    public static class BlockEntities extends LatticeRegistry<LatticeBlockEntity<?, ?>> {
        private final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES;
        private final DeferredRegister.Blocks BLOCKS;
        private final DeferredRegister.Items BLOCK_ITEMS;

        protected BlockEntities(@NotNull String modId) {
            super(modId);
            this.BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, modId);
            this.BLOCKS = DeferredRegister.createBlocks(modId);
            this.BLOCK_ITEMS = DeferredRegister.createItems(modId);
        }

        @SuppressWarnings("DataFlowIssue")
        public <T extends Block, BE extends BlockEntity, I extends LatticeBlockEntity<T, BE>> I register(@NotNull String id, LatticeBlockEntity.AppendableBuilder<T, BE, I, ?, ?> builder) {
            DeferredBlock<T> deferredBlock = BLOCKS.register(id, builder::generate);
            I latticeBlockEntity = builder.build(createRegistryId(this, id), deferredBlock);

            Supplier<BlockEntityType<BE>> blockEntityType = BLOCK_ENTITY_TYPES.register(id + "_block_entity",
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> {
                                try {
                                    return builder.buildBlockEntity(pos, state);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            },
                            latticeBlockEntity.get()
                    ).build(null)
            );

            latticeBlockEntity.setType(blockEntityType);
            builder.type(blockEntityType);
            this.BLOCK_ITEMS.register(id, () -> new BlockItem(deferredBlock.get(), builder.getBlockBuilder().getBlockItemProperties()));
            this.registryContent.add(latticeBlockEntity);

            return latticeBlockEntity;
        }

        @Override
        public void register(@NotNull IEventBus modEventBus) {
            this.registryContent.forEach(latticeBlockEntity -> {
                checkDuplicate(blockEntities, latticeBlockEntity.getRegistryId());
                checkDuplicate(blocks, latticeBlockEntity.getRegistryId());
                checkDuplicate(items, latticeBlockEntity.getRegistryId());
            });

            this.BLOCK_ENTITY_TYPES.register(modEventBus);
            this.BLOCKS.register(modEventBus);
            this.BLOCK_ITEMS.register(modEventBus);
            blockEntities.addAll(this.registryContent);
            blocks.addAll(this.registryContent);
        }
    }

    public static class ArmorMaterials extends LatticeRegistry<LatticeArmorMaterial> {
        private final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS;

        protected ArmorMaterials(@NotNull String modId) {
            super(modId);
            this.ARMOR_MATERIALS = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, modId);
        }

        public <I extends LatticeArmorMaterial> I register(@NotNull String id, @NotNull LatticeArmorMaterial.AppendableBuilder<I, ?> builder) {
            RegistryId registryId = createRegistryId(this, id);
            ResourceLocation resourceLocation = registryId.toResourceLoc();
            I latticeArmorType = builder.build(registryId, this.ARMOR_MATERIALS.register(id, () ->
                    new ArmorMaterial(
                            builder.getTypeProtection(),
                            builder.getEnchantmentValue(),
                            builder.getEquipSound(),
                            builder.getRepairIngredient(),
                            List.of(new ArmorMaterial.Layer(resourceLocation)),
                            builder.getToughness(),
                            builder.getKnockbackResistance()
                    )

            ));
            this.registryContent.add(latticeArmorType);

            return latticeArmorType;
        }

        @Override
        public void register(@NotNull IEventBus modEventBus) {
            this.registryContent.forEach(latticeArmorMaterial -> checkDuplicate(armorMaterials, latticeArmorMaterial.getRegistryId()));

            this.ARMOR_MATERIALS.register(modEventBus);
            armorMaterials.addAll(this.registryContent);
        }
    }

    public static class TrimMaterials extends LatticeRegistry<LatticeTrimMaterial> {
        protected TrimMaterials(@NotNull String modId) {
            super(modId);
        }

        public <I extends LatticeTrimMaterial> I register(@NotNull String id, @NotNull LatticeTrimMaterial.AppendableBuilder<I, ?> builder) {
            RegistryId registryId = createRegistryId(this, id);
            I latticeTrimMaterial = builder.build(registryId, ResourceKey.create(Registries.TRIM_MATERIAL, registryId.toResourceLoc()));
            this.registryContent.add(latticeTrimMaterial);

            return latticeTrimMaterial;
        }

        @Override
        public void register(@NotNull IEventBus modEventBus) {
            this.registryContent.forEach(latticeTrimMaterial -> checkDuplicate(trimMaterials, latticeTrimMaterial.getRegistryId()));

            trimMaterials.addAll(this.registryContent);
        }
    }

    public static class TrimPatterns extends LatticeRegistry<LatticeTrimPattern> {
        private final DeferredRegister.Items SMITHING_TEMPLATES;

        protected TrimPatterns(@NotNull String modId) {
            super(modId);
            this.SMITHING_TEMPLATES = DeferredRegister.createItems(modId);
        }

        public <I extends LatticeTrimPattern> I register(@NotNull String id, @NotNull LatticeTrimPattern.AppendableBuilder<I, ?> builder) {
            RegistryId registryId = createRegistryId(this, id);
            ResourceKey<TrimPattern> resourceKey = ResourceKey.create(Registries.TRIM_PATTERN, registryId.toResourceLoc());
            builder.setTrimItem(SMITHING_TEMPLATES.register(id + "_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(resourceKey)));
            I latticeTrimPattern = builder.build(registryId, resourceKey);
            this.registryContent.add(latticeTrimPattern);

            return latticeTrimPattern;
        }

        @Override
        public void register(@NotNull IEventBus modEventBus) {
            this.registryContent.forEach(latticeTrimPattern -> checkDuplicate(trimPatterns, latticeTrimPattern.getRegistryId()));

            this.SMITHING_TEMPLATES.register(modEventBus);
            trimPatterns.addAll(this.registryContent);
        }
    }

    public static Collection<LatticeType> getModelTypes() {
        return new LinkedList<>(modelTypes);
    }

    public static Collection<LatticeTag<?>> getTags() {
        return new LinkedList<>(tags);
    }

    public static Collection<LatticeRecipe> getRecipes() {
        return new LinkedList<>(recipes);
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

    public static Collection<LatticeBlockEntity<?, ?>> getBlockEntities() {
        return new LinkedList<>(blockEntities);
    }

    public static Collection<LatticeArmorMaterial> getArmorMaterials() {
        return new LinkedList<>(armorMaterials);
    }

    public static Collection<LatticeTrimMaterial> getTrimMaterials() {
        return new LinkedList<>(trimMaterials);
    }

    public static Collection<LatticeTrimPattern> getTrimPatterns() {
        return new LinkedList<>(trimPatterns);
    }

    private static <T extends LatticeObject> void transmute(@NotNull Collection<T> collection, @NotNull T transmutable) {
        checkDuplicate(collection, transmutable.getRegistryId());
        collection.add(transmutable);
    }

    public static <R extends LatticeRegistry<? extends LatticeObject>> RegistryId createRegistryId(@NotNull R registry, @NotNull String path) {
        RegistryId registryId = RegistryId.create(registry.modId, path);
        checkDuplicate(registry.registryContent, registryId);

        return registryId;
    }

    public static void checkDuplicate(@NotNull Collection<? extends LatticeObject> collection, @NotNull RegistryId registryId) {
        RegistryUtils.checkDuplicate(collection, registryId);
    }
}