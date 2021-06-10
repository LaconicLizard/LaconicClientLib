package laconiclizard.laconicclientlib.command;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class JsonObjectArgumentType implements ArgumentType<JsonObject> {

    public static final JsonObjectArgumentType INSTANCE = new JsonObjectArgumentType();
    public static final List<String> EXAMPLES = Arrays.asList("{\"x\": 12, \"y\": 14}", "{}", "{\"foo\": \"bar\"}");

    protected JsonObjectArgumentType() {
    }

    @Override public JsonObject parse(StringReader reader) throws CommandSyntaxException {
        JsonObject result = new JsonObject();
        String key;
        char c;

        reader.expect('{');
        while (true) {
            reader.skipWhitespace();
            key = QuotedStringArgumentType.INSTANCE.parse(reader);
            reader.skipWhitespace();
            reader.expect(':');
            reader.skipWhitespace();
            result.add(key, JsonElementArgumentType.INSTANCE.parse(reader));
            reader.skipWhitespace();
            if (!reader.canRead()) {
                throw CommandUtil.invalidJson("incomplete object", reader);
            }
            c = reader.peek();
            switch (c) {
                case ',':
                    reader.read();
                    continue;
                case '}':
                    reader.read();
                    return result;
                default:
                    throw CommandUtil.invalidJson("Expected ',' or '}', got " + reader.peek(), reader);
            }
        }
    }

    // no suggestions

    @Override public Collection<String> getExamples() {
        return EXAMPLES;
    }

}
