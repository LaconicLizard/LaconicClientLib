package laconiclizard.laconicclientlib.command;

import com.google.gson.JsonNull;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class JsonNullArgumentType implements ArgumentType<JsonNull> {

    public static final JsonNullArgumentType INSTANCE
            = new JsonNullArgumentType();
    private static final Collection<String> EXAMPLES = Collections.singleton("null");

    protected JsonNullArgumentType() {
    }

    @Override public JsonNull parse(StringReader reader) throws CommandSyntaxException {
        int cursorStart = reader.getCursor();
        try {
            CommandUtil.expect(reader, "null", true);
        } catch (CommandSyntaxException e) {
            reader.setCursor(cursorStart);
            throw e;
        }
        return JsonNull.INSTANCE;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(EXAMPLES, builder);
    }

    @Override public Collection<String> getExamples() {
        return EXAMPLES;
    }

}
