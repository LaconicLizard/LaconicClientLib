package laconiclizard.laconicclientlib.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.ConfigSerializer;

/**
 * A custom de/serialization method for AutoConfig.
 *
 * @param <T> type of config that is being de/serialized
 */
public interface CustomSerializer<T extends ConfigData> extends ConfigSerializer<T> {

    /**
     * Function to use for registering this CustomSerializer with AutoConfig.
     * Default implementation does nothing and returns this.
     *
     * @param definition  the Config definition
     * @param configClass the class of the config
     * @return ConfigSerializer to register with AutoConfig
     */
    default ConfigSerializer<T> reg(Config definition, Class<T> configClass) {
        return this;
    }

}
