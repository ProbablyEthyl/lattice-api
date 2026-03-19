package net.ethyl.lattice_api;

import net.ethyl.lattice_api.core.data.LatticeRegistries;
import net.ethyl.lattice_api.modules.base.LatticeBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.jetbrains.annotations.NotNull;

@Mod(value = LatticeApi.MOD_ID, dist = Dist.CLIENT)
public class LatticeApiClient {
    public LatticeApiClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    public static void registerRenderersEvent(EntityRenderersEvent.RegisterRenderers event) {
        LatticeRegistries.getBlockEntities().forEach(latticeBlockEntity -> registerRenderer(event, latticeBlockEntity));
    }

    private static <BE extends BlockEntity> void registerRenderer(@NotNull EntityRenderersEvent.RegisterRenderers event, @NotNull LatticeBlockEntity<?, BE> latticeBlockEntity) {
        event.registerBlockEntityRenderer(
                latticeBlockEntity.getType().get(),
                latticeBlockEntity::createRendererDefinition
        );
    }

    public static void registerLayerDefinitionsEvent(EntityRenderersEvent.RegisterLayerDefinitions event) {
        LatticeRegistries.getBlockEntities().forEach(latticeBlockEntity ->
                event.registerLayerDefinition(
                        latticeBlockEntity.getLayerLocation(),
                        latticeBlockEntity::createLayerDefinition
                )
        );
    }

    public static void addListeners(@NotNull IEventBus modEventBus) {
        modEventBus.addListener(LatticeApiClient::registerRenderersEvent);
        modEventBus.addListener(LatticeApiClient::registerLayerDefinitionsEvent);
    }
}
