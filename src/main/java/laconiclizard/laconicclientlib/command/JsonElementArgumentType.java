package laconiclizard.laconicclientlib.command;

import com.google.gson.JsonElement;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class JsonElementArgumentType implements ArgumentType<JsonElement> {

    public static final JsonElementArgumentType INSTANCE
            = new JsonElementArgumentType();

    public static final List<String> EXAMPLES = Arrays.asList("124", "[0, 1, \"hello\"]", "{\"x\": 12, \"y\": 14}", "null");

    protected JsonElementArgumentType() {
    }

    @Override public JsonElement parse(StringReader reader) throws CommandSyntaxException {
        int cursorStart = reader.getCursor();
        reader.skipWhitespace();
        if (!reader.canRead()) {
            throw CommandUtil.invalidJson("no input", reader);
        }
        switch (reader.peek()) {
            case '{':
                return JsonObjectArgumentType.INSTANCE.parse(reader);
            case '[':
                return JsonArrayArgumentType.INSTANCE.parse(reader);
            case 'n':
                try {
                    return JsonNullArgumentType.INSTANCE.parse(reader);
                } catch (CommandSyntaxException ignored) {
                }
                reader.setCursor(cursorStart);
            default:
                return JsonPrimitiveArgumentType.INSTANCE.parse(reader);
        }
    }

    // no suggestions

    @Override public Collection<String> getExamples() {
        return EXAMPLES;
    }

}
