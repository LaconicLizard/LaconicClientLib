package laconiclizard.laconicclientlib.client;

import laconiclizard.laconicclientlib.Commands;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT) public class LaconicClientLibClient implements ClientModInitializer {

    @Override public void onInitializeClient() {
        Commands.initializeTools();
    }

}
