package dev.comfast.rgx;

import lombok.RequiredArgsConstructor;
import org.intellij.lang.annotations.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * {@link RgxApi}
 */
@RequiredArgsConstructor
public class Rgx {
    @Language("regexp") public final String pattern;
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
    public List<String> matchAllAsString(String inputText, int nthGroup) {
        return matchAll(inputText).stream()
                .map(rgxMatch -> rgxMatch.get(nthGroup))
                .collect(Collectors.toList());
    }

    /** @return All matches list. */
    public List<RgxMatch> matchAll(String inputText) {
        Matcher m = Pattern.compile(pattern, flags).matcher(inputText);

        List<RgxMatch> list = new ArrayList<>();
        while (m.find()) {
            list.add(new RgxMatch(readGroups(m), pattern, inputText));
        }

        return list;
    }

    @Override
    public String toString() {
        return pattern;
    }

    /**
     * @param matcher matcher
     * @return All capturing groups
     */
    private String[] readGroups(Matcher matcher) {
        String[] groups = new String[matcher.groupCount() + 1];
        for (int i = 0; i < groups.length; i++) {
            groups[i] = matcher.group(i);
        }

        return groups;
    }
}

