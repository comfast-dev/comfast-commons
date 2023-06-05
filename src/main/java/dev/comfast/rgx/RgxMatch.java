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
            throw new RgxNotFound(
                    "Match doesn't contain group #%d in %d total groups\n" +
                    "Match found by pattern '%s' in input:\n%s",
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
     * @param failMsg will throw only cause Exception
     * @param msgArgs printf arguments for failMsg
     * @return this
     */
    public RgxMatch throwIfEmpty(String failMsg, Object... msgArgs) {
        if (isPresent()) return this;

        RgxNotFound cause = new RgxNotFound("Not found pattern '%s' in text:\n%s", pattern, shortInput());
        throw failMsg == null || failMsg.isEmpty()
              ? cause :
              new RgxNotFound(failMsg, cause, msgArgs);
    }

    private String shortInput() {
        return input.length() < ERR_MSG_MAX_LENGTH
                ? input
                : input.substring(0, ERR_MSG_MAX_LENGTH);
    }
}
