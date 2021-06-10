package laconiclizard.laconicclientlib.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.text.TranslatableText;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/** The user must pick one of a fixed collection of options. */
public class OptionsArgumentType implements ArgumentType<String> {

    public final Collection<String> options;

    public OptionsArgumentType(Collection<String> options) {
        Objects.requireNonNull(options);
        this.options = options;
    }

    @Override public String parse(StringReader reader) throws CommandSyntaxException {
        String value = reader.readString();
        if (!options.contains(value)) {
            throw new SimpleCommandExceptionType(new TranslatableText("commands.laconic-client-lib.no_such_option", value))
                    .createWithContext(reader);
        }
        return value;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(options, builder);
    }

    @Override public Collection<String> getExamples() {
        return options;
    }


}
