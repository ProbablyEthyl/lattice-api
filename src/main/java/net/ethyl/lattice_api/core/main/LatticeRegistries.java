package net.ethyl.lattice_api.core.main;

import net.ethyl.lattice_api.core.instances.RegistryId;
import net.ethyl.lattice_api.core.utils.RegistryUtils;
import net.ethyl.lattice_api.modules.base.LatticeObject;
import net.ethyl.lattice_api.modules.base.LatticeRegistry;
import net.ethyl.lattice_api.modules.base.LatticeTag;
import net.neoforged.bus.api.IEventBus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;

public class LatticeRegistries {
    private static final Collection<LatticeTag<?>> tags = new LinkedList<>();

    public static Tags createTags(@NotNull String modId) {
        return new Tags(modId);
    }

    public static class Tags extends LatticeRegistry<LatticeTag<?>> {
        private Tags(@NotNull String modId) {
            super(modId);
        }

        public <T, I extends LatticeTag<T>, B extends LatticeTag.Builder<T, I, B>> I register(@NotNull String id, @NotNull B builder) {
            RegistryId registryId = createRegistryId(this.modId, id);
            checkDuplicate(this.registryContent, registryId);
            I latticeTag = builder.build(registryId);
            this.registryContent.add(latticeTag);

            return latticeTag;
        }

        @Override
        public void register(@NotNull IEventBus modEventBus) {
            this.registryContent.forEach(latticeTag -> checkDuplicate(tags, latticeTag.getRegistryId()));

            tags.addAll(this.registryContent);
        }
    }

    public static Collection<LatticeTag<?>> getTags() {
        return new LinkedList<>(tags);
    }

    private static RegistryId createRegistryId(@NotNull String namespace, @NotNull String path) {
        return RegistryId.create(namespace, path);
    }

    private static void checkDuplicate(@NotNull Collection<? extends LatticeObject> collection, @NotNull RegistryId registryId) {
        RegistryUtils.checkDuplicate(collection, registryId);
    }
}