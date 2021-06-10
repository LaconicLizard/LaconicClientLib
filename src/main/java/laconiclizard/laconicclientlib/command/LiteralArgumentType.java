package laconiclizard.laconicclientlib.command;

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

/** Reads a literal string from the input stream.  Return the literal that was read. */
public class LiteralArgumentType implements ArgumentType<String> {

    public final String literal;
    public final boolean caseSensitive;
    protected final Collection<String> suggestions;

    public LiteralArgumentType(String literal, boolean caseSensitive) {
        this.literal = literal;
        this.caseSensitive = caseSensitive;
        suggestions = Collections.singleton(literal);
    }

    @Override public String parse(StringReader reader) throws CommandSyntaxException {
        return CommandUtil.expect(reader, literal, caseSensitive);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(suggestions, builder);
    }

    @Override public Collection<String> getExamples() {
        return suggestions;
    }
}
