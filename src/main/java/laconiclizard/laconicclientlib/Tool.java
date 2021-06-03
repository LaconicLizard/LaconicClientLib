package laconiclizard.laconicclientlib;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** A user-visible tool.  This interface provides utility methods for configuration via the command-line. */
public interface Tool {

    /* Map of Tool name -> that Tool for all registered Tool s. */
    Map<String, Tool> TOOLS = new ConcurrentHashMap<>();

    /**
     * Register the given Tool.  All Tool s should be registered exactly once at initialization.
     *
     * @param tool Tool to register
     * @param <T>  type of Tool being registered
     * @return tool, for convenience
     */
    static <T extends Tool> T register(T tool) {
        TOOLS.put(tool.name(), tool);
        return tool;
    }

    /**
     * The unique name of this Tool.
     *
     * @return the unique name of this tool
     */
    String name();

    /**
     * Map from name -> Option for all options on this Tool.
     *
     * @return map of all Option s on this Tool.
     */
    Map<String, Option<?>> options();

    /**
     * The command under which the configuration options should be registered.
     *
     * @return node under which to register configuration options
     */
    LiteralArgumentBuilder<FabricClientCommandSource> configRootCommand();

    /** An individual option on a Tool. */
    class Option<T> {

        public final ArgumentType<T> argumentType;
        public final Class<T> valueType;
        private T value;

        /**
         * @param argumentType ArgumentType used to parse values for this Option
         * @param valueType    type of value this Option holds
         * @param defaultValue default value of this Option
         */
        public Option(ArgumentType<T> argumentType, Class<T> valueType, T defaultValue) {
            this.argumentType = argumentType;
            this.valueType = valueType;
            this.value = defaultValue;
        }

        /**
         * Get the value of this Option
         *
         * @return the current value of this Option
         */
        public T get() {
            return value;
        }

        /**
         * Set the value of this Option
         *
         * @param value new value for this Option
         */
        public void set(T value) {
            this.value = value;
        }

        /**
         * Set the value of this Option with an unchecked cast.
         *
         * @param value new value for this Option
         * @throws ClassCastException if value is of the wrong type for this Option
         */
        public void setUnchecked(Object value) {
            //noinspection unchecked
            this.value = (T) value;
        }

    }

    /** A Boolean option class, for convenience. */
    class BooleanOption extends Option<Boolean> {
        public BooleanOption(Boolean defaultValue) {
            super(BoolArgumentType.bool(), boolean.class, defaultValue);
        }
    }

}
