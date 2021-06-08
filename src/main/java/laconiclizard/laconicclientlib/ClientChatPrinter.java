package laconiclizard.laconicclientlib;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

public class ClientChatPrinter {

    public Text prefix;
    public Style infoStyle = Style.EMPTY.withColor(Formatting.WHITE),
            warnStyle = Style.EMPTY.withColor(Formatting.YELLOW),
            errorStyle = Style.EMPTY.withColor(Formatting.RED),
            debugStyle = Style.EMPTY.withColor(Formatting.BLUE);

    public ClientChatPrinter() {
        this(Text.of(""));
    }

    public ClientChatPrinter(Text prefix) {
        this.prefix = prefix;
    }

    /**
     * Prints the given text to client chat.
     * This is the most primitive method for printing; prefer to use a ClientChatPrinter instance.
     *
     * @param msg text to print to client chat
     */
    public static void printRaw(Text msg) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        player.sendSystemMessage(msg, null);
    }

    /**
     * Print msg with this ClientChatPrinter's prefix, but otherwise unmodified.
     *
     * @param msg message to print
     */
    public void printPrefixed(Text msg) {
        printRaw(prefix.copy().append(msg));
    }

    public void printPrefixed(String msg) {
        printPrefixed(Text.of(msg));
    }

    /**
     * Print the given message with the info style.
     *
     * @param msg message to print
     */
    public void info(Text msg) {
        printPrefixed(msg.copy().setStyle(infoStyle));
    }

    public void info(String msg) {
        info(Text.of(msg));
    }

    /**
     * Print the given message with the warn style.
     *
     * @param msg message to print
     */
    public void warn(Text msg) {
        printPrefixed(msg.copy().setStyle(warnStyle));
    }

    public void warn(String msg) {
        warn(Text.of(msg));
    }

    /**
     * Print the given message with the error style.
     *
     * @param msg message to print
     */
    public void error(Text msg) {
        printPrefixed(msg.copy().setStyle(errorStyle));
    }

    public void error(String msg) {
        error(Text.of(msg));
    }

    /**
     * Print the given message with the debug style.
     *
     * @param msg message to print
     */
    public void debug(Text msg) {
        printPrefixed(msg.copy().setStyle(debugStyle));
    }

    public void debug(String msg) {
        debug(Text.of(msg));
    }

}
