package net.ethyl.lattice_api.core.instances;

import net.ethyl.lattice_api.core.utils.CoreUtils;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class LatticeConfigurable<T> {
    protected ModConfigSpec.ConfigValue<T> configValue;
    protected final T defaultValue;

    protected LatticeConfigurable(@NotNull T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public T get() {
        return this.configValue.get();
    }

    public void set(@NotNull ModConfigSpec.Builder builder, @NotNull java.lang.String path) {
        this.configValue = builder.define(path, this.defaultValue);
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public static String from(@NotNull java.lang.String defaultValue) {
        return new String(defaultValue);
    }

    public static StringList from(@NotNull java.lang.String... defaultValues) {
        return new StringList(List.of(defaultValues));
    }

    public static StringList emptyStrings() {
        return new StringList(List.of());
    }

    public static Integer from(@NotNull java.lang.Integer defaultValue) {
        return new Integer(defaultValue);
    }

    public static IntegerList from(@NotNull java.lang.Integer... defaultValues) {
        return new IntegerList(List.of(defaultValues));
    }

    public static IntegerList emptyIntegers() {
        return new IntegerList(List.of());
    }

    public static Float from(@NotNull java.lang.Float defaultValue) {
        return new Float(defaultValue);
    }

    public static FloatList from(@NotNull java.lang.Float... defaultValues) {
        return new FloatList(List.of(defaultValues));
    }

    public static FloatList emptyFloats() {
        return new FloatList(List.of());
    }

    public static Boolean from(@NotNull java.lang.Boolean defaultValue) {
        return new Boolean(defaultValue);
    }

    public static BooleanList from(@NotNull java.lang.Boolean... defaultValues) {
        return new BooleanList(List.of(defaultValues));
    }

    public static BooleanList emptyBooleans() {
        return new BooleanList(List.of());
    }

    public static class String extends LatticeConfigurable<java.lang.String> {
        protected String(@NotNull java.lang.String defaultValue) {
            super(defaultValue);
        }
    }

    public static class StringList extends LatticeConfigurable<Collection<java.lang.String>> {
        protected StringList(@NotNull Collection<java.lang.String> defaultValue) {
            super(defaultValue);
        }

        @Override
        public void set(@NotNull ModConfigSpec.Builder builder, @NotNull java.lang.String path) {
            this.configValue = builder.define(path, this.defaultValue, object -> object instanceof String);
        }
    }

    public static class Integer extends LatticeConfigurable<java.lang.Integer> {
        protected Integer(@NotNull java.lang.Integer defaultValue) {
            super(defaultValue);
        }
    }

    public static class IntegerList extends LatticeConfigurable<Collection<java.lang.Integer>> {
        protected IntegerList(@NotNull Collection<java.lang.Integer> defaultValue) {
            super(defaultValue);
        }

        @Override
        public void set(@NotNull ModConfigSpec.Builder builder, @NotNull java.lang.String path) {
            this.configValue = builder.define(path, this.defaultValue, object -> object instanceof Integer);
        }
    }

    public static class Float extends LatticeConfigurable<java.lang.Float> {
        protected Float(@NotNull java.lang.Float defaultValue) {
            super(defaultValue);
        }
    }

    public static class FloatList extends LatticeConfigurable<Collection<java.lang.Float>> {
        protected FloatList(@NotNull Collection<java.lang.Float> defaultValue) {
            super(defaultValue);
        }

        @Override
        public void set(@NotNull ModConfigSpec.Builder builder, @NotNull java.lang.String path) {
            this.configValue = builder.define(path, this.defaultValue, object -> object instanceof Float);
        }
    }

    public static class Boolean extends LatticeConfigurable<java.lang.Boolean> {
        protected Boolean(@NotNull java.lang.Boolean defaultValue) {
            super(defaultValue);
        }
    }

    public static class BooleanList extends LatticeConfigurable<Collection<java.lang.Boolean>> {
        protected BooleanList(@NotNull Collection<java.lang.Boolean> defaultValue) {
            super(defaultValue);
        }

        @Override
        public void set(@NotNull ModConfigSpec.Builder builder, @NotNull java.lang.String path) {
            this.configValue = builder.define(path, this.defaultValue, object -> object instanceof Boolean);
        }
    }
}