package dev.comfast.rgx;

import lombok.RequiredArgsConstructor;
import org.intellij.lang.annotations.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Provide fluent interface for common Regex operations. Most cases can be done in one line with proper error handling:
 *
 * <p>rgx("\\d+").match("abc123xxx456").get() -> returns "123"
 * <p>rgx("\\d+").match("abc123xxx456").matchAllAsString() -> returns ["123", "456"]
 * <p>rgx("xxx(\\d+)").match("abc123xxx456").group(1) -> returns "456"
 * <p>rgx("yyy").match("abc123xxx456").isPresent() -> returns false;
 * <p>Also throwing detailed errors:
 * <p>rgx("yyy").match("abc123xxx456").throwIfEmpty() -> throws "Not found pattern 'yyy' in text 'abc123xxx456'"
 * <p>rgx("yyy").match("abc123xxx456").get() -> throws "Not found pattern 'yyy' in text 'abc123xxx456'"
 * <p>rgx("xxx(\\d+)").match("abc123xxx456").group(3) -> "throws Match doesn't contain group: 3. Total groups are: 1"
 */
@RequiredArgsConstructor
public class Rgx {
    @Language("regexp")
    public final String pattern;
    private int flags = 0;

    /**
     * @param flags Regex flags. Same as second argument in: Pattern.compile(String, int)
     * @see Pattern#compile(String, int)
     */
    public Rgx flags(int flags) {
        this.flags = flags;
        return this;
    }

    /** @return First match. */
    public RgxMatch match(String inputText) {
        Matcher m = Pattern.compile(pattern, flags).matcher(inputText);
        String[] found = m.find() ? readGroups(m) : null;

        return new RgxMatch(found, pattern, inputText);
    }

    /** @return All matches. */
    public List<String> matchAllAsString(String inputText) {
        return matchAllAsString(inputText, 0);
    }

    /** @return Nth group from all matches. */
    public List<String> matchAllAsString(String input, int nthGroup) {
        return matchAll(input).stream()
                .map(rgxMatch -> rgxMatch.group(nthGroup))
                .collect(Collectors.toList());
    }

    /** @return All matches list. */
    public List<RgxMatch> matchAll(String input) {
        Matcher m = Pattern.compile(pattern, flags).matcher(input);

        List<RgxMatch> list = new ArrayList<>();
        while (m.find()) {
            list.add(new RgxMatch(readGroups(m), pattern, input));
        }

        return list;
    }

    @Override
    public String toString() {
        return pattern;
    }

    private String[] readGroups(Matcher m) {
        String[] groups = new String[m.groupCount() + 1];
        for (int i = 0; i < groups.length; i++) {
            groups[i] = m.group(i);
        }

        return groups;
    }
}

