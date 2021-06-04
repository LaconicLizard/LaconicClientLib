package laconiclizard.laconicclientlib;

import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.UnlockRecipesS2CPacket;
import net.minecraft.scoreboard.ScoreboardCriterion;

/** Utils for Minecraft enums. */
public class MCEnumUtil {

    // ----- UnlockRecipesS2CPacket.Action -----

    public static UnlockRecipesS2CPacket.Action unlockRecipesActionByName(String name) {
        switch (name) {
            case "INIT":
                return UnlockRecipesS2CPacket.Action.INIT;
            case "ADD":
                return UnlockRecipesS2CPacket.Action.ADD;
            case "REMOVE":
                return UnlockRecipesS2CPacket.Action.REMOVE;
            default:
                return UnlockRecipesS2CPacket.Action.valueOf(name);
        }
    }

    public static String getName(UnlockRecipesS2CPacket.Action action) {
        switch (action) {
            case INIT:
                return "INIT";
            case ADD:
                return "ADD";
            case REMOVE:
                return "REMOVE";
            default:
                return action.name();
        }
    }

    // ----- MessageType -----

    /**
     * Get the named MessageType.
     *
     * @param name name of the MessageType of interest
     * @return named MessageType
     */
    public static MessageType messageTypeByName(String name) {
        switch (name) {
            case "CHAT":
                return MessageType.CHAT;
            case "SYSTEM":
                return MessageType.SYSTEM;
            case "GAME_INFO":
                return MessageType.GAME_INFO;
            default:
                return MessageType.valueOf(name);
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
