package laconiclizard.laconicclientlib.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "laconic-client-lib-EmptyConfig")
public final class EmptyConfig implements ConfigData {
    public static final EmptyConfig INSTANCE = new EmptyConfig();

    private EmptyConfig() {
    }

}
