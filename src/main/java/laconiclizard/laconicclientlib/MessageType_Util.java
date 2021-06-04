package laconiclizard.laconicclientlib;

import net.minecraft.network.MessageType;

public class MessageType_Util {

    /**
     * Get the named MessageType.
     *
     * @param name name of the MessageType of interest
     * @return named MessageType, or null if there is no such MessageType
     */
    public static MessageType byName(String name) {
        switch (name) {
            case "CHAT":
                return MessageType.CHAT;
            case "SYSTEM":
                return MessageType.SYSTEM;
            case "GAME_INFO":
                return MessageType.GAME_INFO;
            default:
                return null;
        }
    }

    /**
     * The name of the given MessageType.
     * Defaults to enum name for non-vanilla MessageType s.
     *
     * @param messageType MessageType of interest
     * @return name of the given MessageType
     */
    public static String getName(MessageType messageType) {
        switch (messageType) {
            case CHAT:
                return "CHAT";
            case SYSTEM:
                return "SYSTEM";
            case GAME_INFO:
                return "GAME_INFO";
            default:
                return messageType.name();
        }
    }

}
