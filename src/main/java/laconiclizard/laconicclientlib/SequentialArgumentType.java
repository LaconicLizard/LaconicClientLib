package laconiclizard.laconicclientlib;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.TranslatableText;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * An ArgumentType of several arguments that each depend on prior arguments for suggestions, parsing, etc.
 *
 * @param <T> type of values produced
 */
public class SequentialArgumentType<T> implements ArgumentType<List<T>> {

    private static final SimpleCommandExceptionType INCOMPLETE_EXCTYPE = new SimpleCommandExceptionType(
            new TranslatableText("commands.laconic-client-lib.incomplete_sequential_args"));
    private final List<ArgumentType<? extends T>> argumentTypes;

    @SafeVarargs
    public SequentialArgumentType(ArgumentType<? extends T>... argumentTypes) {
        this.argumentTypes = Arrays.asList(argumentTypes);
    }

    @Override public List<T> parse(StringReader reader) throws CommandSyntaxException {
        List<T> result = new ArrayList<>();
        T item;
        for (ArgumentType<? extends T> at : argumentTypes) {
            try {
                item = at.parse(reader);
            } catch (CommandSyntaxException e) {
                throw INCOMPLETE_EXCTYPE.createWithContext(reader);
            }
            result.add(item);
        }
        return result;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        StringReader reader = new StringReader(builder.getRemaining());
        boolean lst = false;
        for (ArgumentType<? extends T> at : argumentTypes) {
            try {
                at.parse(reader);
            } catch (CommandSyntaxException e) {
                lst = true;
            }
            if (!reader.canRead() || lst) {
                return at.listSuggestions(context, new SuggestionsBuilder(reader.getString(), reader.getCursor()));
            }
            reader.skipWhitespace();
        }
        return Suggestions.empty();
    }

    @Override public Collection<String> getExamples() {
        // examples from all argtypes, in order
        List<List<String>> allExamples = argumentTypes.stream().map(ArgumentType::getExamples)
                .map(ArrayList::new).collect(Collectors.toList());
        // size of the largest examples collection of any argtype
        int maxSize = allExamples.stream().mapToInt(Collection::size).max().orElse(0);
        List<String> result = new ArrayList<>(maxSize);
        List<String> entries = new ArrayList<>(allExamples.size());
        // for each line of output
        for (int i = 0; i < maxSize; i += 1) {
            entries.clear();
            // add an entry from each argtype, defaulting to "" if there isn't one from that argtype
            for (List<String> exs : allExamples) {
                entries.add(i < exs.size() ? exs.get(i) : "");
            }
            result.add(String.join(" ", entries));
        }
        return result;
    }
}
