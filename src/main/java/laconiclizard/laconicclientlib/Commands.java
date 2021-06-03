package laconiclizard.laconicclientlib;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.Map;
import java.util.Objects;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.*;

public class Commands {

    public static void initializeTools() {
        for (Tool tool : Tool.TOOLS.values()) {
            Map<String, Tool.Option<?>> options = tool.options();
            if (options.isEmpty()) continue;  // do not add configuration code if there are no options
            LiteralArgumentBuilder<FabricClientCommandSource> get = literal("get"),
                    set = literal("set");
            for (Map.Entry<String, Tool.Option<?>> entry : options.entrySet()) {
                String name = entry.getKey();
                Tool.Option<?> option = entry.getValue();
                get.then(literal(name).executes(context -> {
                    ClientPlayerEntity player = MinecraftClient.getInstance().player;
                    if (player == null) return 0;
                    player.sendSystemMessage(new LiteralText(Objects.toString(option.get())), null);
                    return 0;
                }));
                set.then(literal(name).then(argument("value", option.argumentType).executes(context -> {
                    tool.options().get(name).setUnchecked(context.getArgument("value", option.valueType));
                    return 0;
                })));
            }
            tool.configRootCommand().then(get).then(set);
        }
    }

}
