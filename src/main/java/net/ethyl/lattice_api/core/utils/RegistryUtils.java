package net.ethyl.lattice_api.core.utils;

import net.ethyl.lattice_api.LatticeApi;
import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.modules.base.LatticeObject;
import net.ethyl.lattice_api.modules.common.tabs.LatticeCreativeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class RegistryUtils {
    public static void checkDuplicate(@NotNull Collection<? extends LatticeObject> collection, @NotNull RegistryId registryId) {
        if (collection.stream().anyMatch(latticeObject -> latticeObject.getRegistryId().toString().equals(registryId.toString()))) {
            LatticeApi.duplicateObjectErr(registryId);
        }
    }

    public static CreativeModeTab createTab(@NotNull LatticeCreativeTab.AppendableBuilder<? extends LatticeCreativeTab, ?> builder, @NotNull RegistryId registryId) {
        return CreativeModeTab.builder()
                .title(Component.translatable("tab." + registryId.getNamespace() + "." + registryId.getPath()))
                .icon(builder.getIcon())
                .displayItems(((itemDisplayParameters, output) -> builder.getTabContent().forEach(itemLikeSupplier -> output.accept(itemLikeSupplier.get()))))
                .build();
    }

    @SuppressWarnings("unchecked")
    public static <I extends LatticeObject> Collection<I> cloneRegister(Collection<I> collection) {
        return collection.stream().map(element -> (I) element.clone()).collect(Collectors.toCollection(LinkedList::new));
    }
}
