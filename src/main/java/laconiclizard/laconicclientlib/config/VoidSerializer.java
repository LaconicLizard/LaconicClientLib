package laconiclizard.laconicclientlib.config;

import me.shedaniel.autoconfig.ConfigData;

import java.util.function.Supplier;

/**
 * A CustomSerializer that does not store anything, and always loads the default config.
 *
 * @param <T> type of config being de/serialized
 */
public class VoidSerializer<T extends ConfigData> implements CustomSerializer<T> {

    private final Supplier<T> createDefault;

    public VoidSerializer(Supplier<T> createDefault) {
        this.createDefault = createDefault;
    }

    @Override public void serialize(T t) {
    }

    @Override public T deserialize() {
        return createDefault();
    }

    @Override public T createDefault() {
        return createDefault.get();
    }

}
