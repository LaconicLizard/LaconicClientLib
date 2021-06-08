package laconiclizard.laconicclientlib;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

public class ClientChatPrinter {

    public Text prefix;
    public boolean stylePrefix;
    public Style infoStyle = Style.EMPTY.withColor(Formatting.WHITE),
            warnStyle = Style.EMPTY.withColor(Formatting.YELLOW),
            errorStyle = Style.EMPTY.withColor(Formatting.RED),
            debugStyle = Style.EMPTY.withColor(Formatting.BLUE);

    public ClientChatPrinter() {
        this("");
    }

    public ClientChatPrinter(Text prefix) {
        this(prefix, false);
    }

    public ClientChatPrinter(Text prefix, boolean stylePrefix) {
        this.prefix = prefix;
        this.stylePrefix = stylePrefix;
    }

    public ClientChatPrinter(String prefix) {
        this(Text.of(prefix));
    }

    public ClientChatPrinter(String prefix, boolean stylePrefix) {
        this(Text.of(prefix), stylePrefix);
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
     * @param msg      message to print
     * @param msgStyle the style of the message; used to style the prefix if .stylePrefix is true
     */
    public void printPrefixed(Text msg, Style msgStyle) {
        MutableText p = prefix.copy();
        if (stylePrefix) {
            p.setStyle(msgStyle);
        }
        printRaw(p.append(msg));
    }

    public void printPrefixed(String msg, Style msgStyle) {
        printPrefixed(Text.of(msg), msgStyle);
    }

    /**
     * Print the given message with the info style.
     *
     * @param msg message to print
     */
    public void info(Text msg) {
        printPrefixed(msg.copy().setStyle(infoStyle), infoStyle);
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
        printPrefixed(msg.copy().setStyle(warnStyle), warnStyle);
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
        printPrefixed(msg.copy().setStyle(errorStyle), errorStyle);
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
        printPrefixed(msg.copy().setStyle(debugStyle), debugStyle);
    }

    public void debug(String msg) {
        debug(Text.of(msg));
    }

}
