package net.ethyl.lattice_api.modules.base;

import net.ethyl.lattice_api.core.instances.LatticeConfigurable;
import net.ethyl.lattice_api.core.utils.CoreUtils;
import net.ethyl.lattice_api.core.utils.ErrUtils;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public abstract class LatticeConfig<T extends LatticeObject> {
    protected final String modId;
    protected final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
    protected final ModConfigSpec spec;

    protected LatticeConfig(@NotNull ModContainer modContainer) {
        this.modId = modContainer.getModId();
        defineEntries();
        spec = builder.build();
    }

    protected abstract void defineEntries();

    public ModConfigSpec getSpec() {
        return this.spec;
    }

    protected void assignConfigEntries(@NotNull Collection<T> configurables) {
        for (T configurable : configurables) {
            if (!configurable.getModId().equals(this.modId)) {
                continue;
            }

            this.builder.push(configurable.getId());

            for (Field field : new LinkedList<>(Arrays.stream(configurable.getClass().getDeclaredFields()).toList())) {
                if (!LatticeConfigurable.class.isAssignableFrom(field.getType())) {
                    continue;
                }

                String fieldName = field.getName();
                field.setAccessible(true);

                try {
                    LatticeConfigurable<?> entry = (LatticeConfigurable<?>) field.get(configurable);

                    if (entry != null) {
                        entry.set(this.builder, CoreUtils.camelToSnakeCase(fieldName));
                    }
                } catch (IllegalAccessException ignored) {
                    ErrUtils.illegalAccessErr(fieldName);
                }
            }

            this.builder.pop();
        }
    }
}
