package dev.comfast.rgx;

import lombok.RequiredArgsConstructor;

import static java.lang.String.format;

/**
 * Represent one Regex match. Can be empty.
 */
@RequiredArgsConstructor
public class RgxMatch {
    public static final int ERR_MSG_MAX_LENGTH = 2000;
    private final String[] foundGroups;

    /**
     * Used only for generate error messages
     */
    private final String pattern;
    private final String input;

    /**
     * @return entire match.
     */
    public String get() {
        return group(0);
    }

    /**
     * @return nth match group.
     */
    public String group(int nth) {
        throwIfEmpty();
        if (foundGroups.length <= nth) {
            throw new RgxException(
                    "Match doesn't contain group: %d. Total groups are: %d\n" +
                    "Found by pattern '%s' in:\n%s",
                    nth, foundGroups.length - 1, pattern, shortInput());
        }
        return foundGroups[nth];
    }

    /**
     * @return true if match found
     */
    public boolean isPresent() {
        return !isEmpty();
    }

    /**
     * @return true if match empty
     */
    public boolean isEmpty() {
        return foundGroups == null;
    }

    /**
     * @return entire match or else default value
     */
    public String orElse(String elseValue) {
        return isPresent() ? get() : elseValue;
    }

    /**
     * Throws Exception with match details if match empty.
     */
    public RgxMatch throwIfEmpty() {
        return throwIfEmpty("");
    }

    /**
     * Throws Exception with additional message and match details if match empty.
     */
    public RgxMatch throwIfEmpty(String userFailMsg) {
        if (isPresent()) return this;
        throw new RgxException(format(
                "%s\nNot found pattern '%s' in text:\n%s",
                userFailMsg, pattern, shortInput()));
    }

    private String shortInput() {
        return input.length() < ERR_MSG_MAX_LENGTH
                ? input
                : input.substring(0, ERR_MSG_MAX_LENGTH);
    }
}
