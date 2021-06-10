package laconiclizard.laconicclientlib.command;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class JsonArrayArgumentType implements ArgumentType<JsonArray> {

    public static final JsonArrayArgumentType INSTANCE = new JsonArrayArgumentType();
    public static final List<String> EXAMPLES = Arrays.asList("[]", "[1, 3, -2]", "[\"hello\", \"world\", \"!\"]");

    protected JsonArrayArgumentType() {
    }

    @Override public JsonArray parse(StringReader reader) throws CommandSyntaxException {
        JsonArray result = new JsonArray();
        JsonElement elt;
        reader.expect('[');
        while (true) {
            reader.skipWhitespace();
            // bubble up exception
            elt = JsonElementArgumentType.INSTANCE.parse(reader);
            result.add(elt);
            reader.skipWhitespace();
            if (!reader.canRead()) {
                throw CommandUtil.invalidJson("incomplete array", reader);
            }
            switch (reader.peek()) {
                case ',':
                    reader.read();
                    continue;
                case ']':
                    reader.read();
                    return result;
                default:
                    throw CommandUtil.invalidJson("expected ',' or ']', got " + reader.peek(), reader);
            }
        }
    }

    // no suggestions

    @Override public Collection<String> getExamples() {
        return EXAMPLES;
    }

}
