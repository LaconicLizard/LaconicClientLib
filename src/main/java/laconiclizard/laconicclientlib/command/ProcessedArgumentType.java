package laconiclizard.laconicclientlib.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/** An ArgumentType that processes its output before returning. */
public class ProcessedArgumentType<T, V> implements ArgumentType<V> {

    public final ArgumentType<T> argumentType;
    public final Function<T, V> processor;

    public ProcessedArgumentType(ArgumentType<T> argumentType, Function<T, V> processor) {
        this.argumentType = argumentType;
        this.processor = processor;
    }

    @Override public V parse(StringReader reader) throws CommandSyntaxException {
        T t = argumentType.parse(reader);
        return processor.apply(t);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return argumentType.listSuggestions(context, builder);
    }

    @Override public Collection<String> getExamples() {
        return argumentType.getExamples();
    }

}
