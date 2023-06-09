package dev.comfast.rgx;

import org.intellij.lang.annotations.Language;

import java.util.regex.Pattern;

/**
 * Provide fluent interface for common Regex operations. Most cases can be done in one line with proper error handling:
 * Usage example: <pre>{@code
 * rgx("\\d+").match("abc123xxx456").get() // returns "123"
 * rgx("\\d+").match("abc123xxx456").matchAllAsString() // returns entire matches: ["123", "456"]
 * rgx("\\d(\\d\\d)").match("abc123xxx456").matchAllAsString(1) // returns groups number '1': ["23", "56"]
 * rgx("\\d(\\d\\d)").match("abc123xxx456").group(1) // returns "23"
 * rgx("ooo").match("abc123xxx456").isPresent() // returns false;
 * // Also throw detailed errors:
 * rgx("yyy").match("abc123xxx456").throwIfEmpty()... // throws "Not found pattern 'yyy' in text 'abc123xxx456'"
 * rgx("yyy").match("abc123xxx456").get() // throws "Not found pattern 'yyy' in text 'abc123xxx456'"
 * rgx("xxx(\\d+)").match("abc123xxx456").group(3) // "throws Match doesn't contain group: 3. Total groups are: 1"
 * }</pre>
 * @see RgxMatch
 */
public class RgxApi {
    /**
     * Usage example: <pre>{@code
     * rgx("\\d(\\d\\d)").match("abc123xxx456").group(1) // returns "123"
     * }</pre>
     * @param pattern regular expression
     * @return Rgx wrapper
     */
    public static Rgx rgx(@Language("regexp") String pattern) {
        return new Rgx(pattern);
    }

    /**
     * <p>Use regex with flags</p>
     * Usage example: <pre>{@code
     * rgx("ABC", Pattern.CASE_INSENSITIVE).match("abc123XXX456").get() // returns "abc"
     * rgx("ABC.+DEF", CASE_INSENSITIVE | DOTALL).match("abc \n def").get() // returns "abc \n def"
     * }</pre>
     * @param pattern regular expression
     * @param flags Regex flags. Same as second argument in: {@link Pattern#compile(String, int)}
     * @return Rgx wrapper
     */
    public static Rgx rgx(@Language("regexp") String pattern, int flags) {
        return new Rgx(pattern).flags(flags);
    }
}
