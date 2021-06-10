package laconiclizard.laconicclientlib;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

/** An ArgumentType that receives context when parsing its command. */
public interface ContextualArgumentType<T> extends ArgumentType<T> {

    /** Parse an argument of this type from reader.  Do NOT mutate context. */
    default <S> T parseWithContext(StringReader reader, CommandContext<S> context) throws CommandSyntaxException {
        return parse(reader);
    }

    /** Whether this ArgumentType depends on context.  Override this if you use parseWithContext. */
    default boolean isContextDependent() {
        return false;
    }

    /** A context-free way to check whether the given input *could* be valid, for some context. */
    default boolean isPotentiallyValidInput(StringReader reader) {
        if (isContextDependent()) {
            return true;
        } else {
            try {
                parse(reader);
                return !reader.canRead() || reader.peek() == ' ';  // from ArgumentCommandNode.isValidInput
            } catch (CommandSyntaxException e) {
                return false;
            }
        }
    }

}
