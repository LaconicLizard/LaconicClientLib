package laconiclizard.laconicclientlib.config;

import laconiclizard.laconicclientlib.FuncUtil;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The "signature" of a config class.
 * Allows efficient operations that might otherwise require reflection.
 */
public class ConfigSig<T extends ConfigData> {

    private static final Map<Class<? extends ConfigData>, ConfigSig<?>> CONFIG_SIGS = new ConcurrentHashMap<>();

    public final String name;
    public final Class<T> clazz;
    // unmodifiable map from field name -> config field
    public final Map<String, ConfigField<T, ?>> fields;

    private <S> ConfigSig(Class<T> clazz) {
        // check args
        Objects.requireNonNull(clazz);
        if (!Modifier.isPublic(clazz.getModifiers()))
            throw new IllegalArgumentException("Cannot inspect non-public class " + clazz);
        Config configAnnot = clazz.getAnnotation(Config.class);
        if (configAnnot == null) {
            throw new IllegalArgumentException("Config class not annotated with @Config: " + clazz);
        }
        this.name = configAnnot.name();
        this.clazz = clazz;
        Map<String, ConfigField<T, ?>> fields = new HashMap<>();
        for (Field f : clazz.getDeclaredFields()) {
            // getters and setters
            int mods = f.getModifiers();
            if (Modifier.isStatic(mods)
                    || Modifier.isFinal(mods)
                    || !Modifier.isPublic(mods)) {
                continue;
            }
            String fname = f.getName();
            //noinspection unchecked
            fields.put(fname, new ConfigField<>(clazz, fname, (Function<T, S>) FuncUtil.getter(clazz, fname),
                    (BiConsumer<T, S>) FuncUtil.setter(clazz, fname)));
        }
        this.fields = Collections.unmodifiableMap(fields);
    }

    /** A single field in a config class. */
    public static class ConfigField<T extends ConfigData, V> {

        public final Class<T> clazz;
        public final String name;
        private final Function<T, V> getter;
        private final BiConsumer<T, V> setter;

        private ConfigField(Class<T> clazz, String name, Function<T, V> getter, BiConsumer<T, V> setter) {
            this.clazz = clazz;
            this.name = name;
            this.getter = getter;
            this.setter = setter;
        }

        /**
         * Get the value of this field on the given instance.
         *
         * @param instance instance of interest
         * @return value of this field on instance
         */
        public V get(T instance) {
            return getter.apply(instance);
        }

        /**
         * Set the value of this field on the given instance.
         *
         * @param instance instance of interest
         * @param value    new value for this field on instance
         */
        public void set(T instance, V value) {
            setter.accept(instance, value);
        }

        /**
         * Set the value of this field with an unchecked cast to the appropriate type.
         *
         * @param instance instance to set the value on
         * @param value    new value for this field on instance
         */
        public void setUnchecked(T instance, Object value) {
            //noinspection unchecked
            setter.accept(instance, (V) value);
        }

    }

    /**
     * Get the signature of the given config class.
     *
     * @param clazz config class of interest
     * @return signature of clazz
     */
    public static <T extends ConfigData> ConfigSig<T> getSig(Class<T> clazz) {
        //noinspection unchecked
        return (ConfigSig<T>) CONFIG_SIGS.computeIfAbsent(clazz, (unused) -> new ConfigSig<>(clazz));
    }

    /**
     * Copy the config options from src to dest.
     * Assumes that src is valid, and therefore does NOT perform validation.
     *
     * @param dest config instance into which data will by copied
     * @param src  config instance from which data will be copied
     * @return dest
     */
    public T copyInto(T dest, T src) {
        Objects.requireNonNull(dest);
        Objects.requireNonNull(src);
        //noinspection unchecked
        ConfigSig<T> sig = getSig((Class<T>) dest.getClass());
        try {
            for (ConfigField<T, ?> f : sig.fields.values()) {
                f.setUnchecked(dest, f.get(src));
            }
        } catch (Throwable t) {
            throw new AssertionError("Impossible", t);
        }
        return dest;
    }

    /**
     * Create an instance of the config class with default values.
     * This is done by invoking its public no-args constructor.
     *
     * @return new config instance with all default values
     * @throws RuntimeException if there is an error while instantiating the class
     * @throws AssertionError   if the config class does not have a public no-args constructor
     */
    public T createDefault() {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Error in config class contructor", e);
        } catch (IllegalAccessException e) {
            throw new AssertionError("Config class should have a public no-args constructor; this " +
                    "is missing in " + clazz);
        }
    }

}
