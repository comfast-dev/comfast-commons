package dev.comfast.errors;
import static java.lang.String.format;

/**
 * Just short version of RuntimeException
 */
public class Fail extends RuntimeException {
    public Fail(String errorMsg, Object... msgArgs) {
        super(format(errorMsg, msgArgs));
    }

    public Fail(String errorMsg, Throwable cause, Object... msgArgs) {
        super(format(errorMsg, msgArgs), cause);
    }
}
