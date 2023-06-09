package dev.comfast.rgx;
import static java.lang.String.format;

/**
 * Thrown when regex match not found.
 */
public class RgxNotFound extends RuntimeException {
    public RgxNotFound(String errorMsg, Object... msgArgs) {
        super(format(errorMsg, msgArgs));
    }

    public RgxNotFound(String errorMsg,Throwable cause, Object... msgArgs) {
        super(format(errorMsg, msgArgs), cause);
    }
}
