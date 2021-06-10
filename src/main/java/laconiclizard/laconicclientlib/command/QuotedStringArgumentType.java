package laconiclizard.laconicclientlib.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/** An ArgumentType for a string that must be quoted. */
public class QuotedStringArgumentType implements ArgumentType<String> {

    public static final QuotedStringArgumentType INSTANCE = new QuotedStringArgumentType();

    public static final List<String> EXAMPLES = Arrays.asList("\"quoted string\"", "\"foo\"", "\"cow says \\\"moo\\\"\"");

    protected QuotedStringArgumentType() {
    }

    @Override public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readQuotedString();
    }

    // no suggestions

    @Override public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
