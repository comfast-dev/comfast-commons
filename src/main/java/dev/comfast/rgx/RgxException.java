package dev.comfast.rgx;

public class RgxException extends RuntimeException {
    public RgxException(String errorMsg, Object... msgArgs) {
        super(String.format(errorMsg, msgArgs));
    }
}
