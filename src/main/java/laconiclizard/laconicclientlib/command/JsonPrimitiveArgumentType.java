package laconiclizard.laconicclientlib.command;

import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

/** Parse a Json primitive from the input.  Does NOT include null, as JsonNull is not a JsonPrimitive. */
public class JsonPrimitiveArgumentType implements ArgumentType<JsonPrimitive> {

    private static final BoolArgumentType BOOL_AT = BoolArgumentType.bool();
    private static final DoubleArgumentType DOUBLE_AT = DoubleArgumentType.doubleArg();
    private static final LongArgumentType LONG_AT = LongArgumentType.longArg();
    private static final QuotedStringArgumentType STRING_AT = QuotedStringArgumentType.INSTANCE;

    public static final JsonPrimitiveArgumentType INSTANCE = new JsonPrimitiveArgumentType();

    protected JsonPrimitiveArgumentType() {
    }

    @Override public JsonPrimitive parse(StringReader reader) throws CommandSyntaxException {
        // repetition necessary due to type issues with JsonPrimitive constructors
        int cursorStart = reader.getCursor();
        try {
            return new JsonPrimitive(BOOL_AT.parse(reader));
        } catch (CommandSyntaxException ignored) {
        }
        reader.setCursor(cursorStart);
        try {
            return new JsonPrimitive(DOUBLE_AT.parse(reader));
        } catch (CommandSyntaxException ignored) {
        }
        reader.setCursor(cursorStart);
        try {
            return new JsonPrimitive(LONG_AT.parse(reader));
        } catch (CommandSyntaxException ignored) {
        }
        reader.setCursor(cursorStart);
        try {
            return new JsonPrimitive(STRING_AT.parse(reader));
        } catch (CommandSyntaxException ignored) {
        }
        throw CommandUtil.invalidJson("unrecognized primitive", reader);
    }

}
