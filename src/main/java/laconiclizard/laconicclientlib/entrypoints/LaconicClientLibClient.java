package laconiclizard.laconicclientlib.entrypoints;

import laconiclizard.laconicclientlib.config.EmptyConfig;
import laconiclizard.laconicclientlib.config.VoidSerializer;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT) public class LaconicClientLibClient implements ClientModInitializer {

    @Override public void onInitializeClient() {
        AutoConfig.register(EmptyConfig.class, new VoidSerializer<>(() -> EmptyConfig.INSTANCE)::reg);
    }

}
