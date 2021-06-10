package laconiclizard.laconicclientlib.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.text.TranslatableText;

import java.util.Objects;

public class CommandUtil {

    /**
     * Expect the given string from the input.
     *
     * @param reader        reader to expect from
     * @param s             string to expect
     * @param caseSensitive whether expected string is case sensitive
     * @return s
     * @throws CommandSyntaxException If the next sequence of characters from reader is not s.  Does not move
     *                                the reader if this exception is thrown.
     */
    public static String expect(StringReader reader, String s, boolean caseSensitive) throws CommandSyntaxException {
        String r;
        try {
            r = read(reader, s.length());
        } catch (CommandSyntaxException e) {
            throw new SimpleCommandExceptionType(
                    new TranslatableText("commands.laconic-client-lib.expected_literal", s))
                    .createWithContext(reader);
        }
        String v;
        if (!caseSensitive) {
            v = r.toLowerCase();
            s = s.toLowerCase();
        } else {
            v = r;
        }
        if (!Objects.equals(v, s)) {
            throw new SimpleCommandExceptionType(
                    new TranslatableText("commands.laconic-client-lib.expected_literal", s))
                    .createWithContext(reader);
        }
        return r;
    }

    /**
     * Read n characters from reader.
     *
     * @param reader reader to read from
     * @param n      number of characters to read
     * @return the characters that were read
     * @throws CommandSyntaxException if fewer than n characters could be read
     */
    public static String read(StringReader reader, int n) throws CommandSyntaxException {
        if (n < 0) {
            throw new IllegalArgumentException("n should be nonnegative, got " + n);
        }
        if (!reader.canRead(n)) {
            throw new SimpleCommandExceptionType(
                    new TranslatableText("commands.laconic-client-lib.too_short", n))
                    .createWithContext(reader);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i += 1) {
            sb.append(reader.read());
        }
        return sb.toString();
    }

    public static CommandSyntaxException invalidJson(String msg, StringReader reader) {
        SimpleCommandExceptionType scet = new SimpleCommandExceptionType(
                new TranslatableText("commands.laconic-client-lib.invalid_json", msg));
        if (reader == null) {
            return scet.create();
        } else {
            return scet.createWithContext(reader);
        }
    }

}
