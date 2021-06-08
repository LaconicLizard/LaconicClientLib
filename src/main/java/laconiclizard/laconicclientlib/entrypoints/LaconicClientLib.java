package laconiclizard.laconicclientlib.entrypoints;

import laconiclizard.laconicclientlib.config.EmptyConfig;
import laconiclizard.laconicclientlib.config.VoidSerializer;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ModInitializer;

public class LaconicClientLib implements ModInitializer {

    @Override public void onInitialize() {
        AutoConfig.register(EmptyConfig.class, new VoidSerializer<>(() -> EmptyConfig.INSTANCE)::reg);
    }

}
