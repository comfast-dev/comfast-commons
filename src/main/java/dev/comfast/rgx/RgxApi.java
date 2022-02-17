package dev.comfast.rgx;

import org.intellij.lang.annotations.Language;

public class RgxApi {
    public static Rgx rgx(@Language("regexp") String pattern) {
        return new Rgx(pattern);
    }

    public static Rgx rgx(@Language("regexp") String pattern, int flags) {
        return new Rgx(pattern).flags(flags);
    }
}
