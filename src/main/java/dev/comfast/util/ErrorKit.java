package dev.comfast.util;
import lombok.SneakyThrows;

import java.util.function.Supplier;

import static java.lang.String.format;

/**
 * ErrorKit is a set of useful functions for error handling.
 */
public class ErrorKit {
    /**
     * Short supplier for RuntimeException, fits to Streams/Optionals
     * <p>Usage example: </p><pre>{@code
     * found = someStream.findFirst().orElseThrow(_fail("Not found '%s'", someParam))
     * value = someOptional.orElseThrow(_fail("Oh no !"))
     * }</pre>
     * @param errorMsg message if error happens
     * @param msgParams format params for errorMsg
     * @return Supplier for RuntimeException, fits to Streams/Optionals
     */
    public static Supplier<RuntimeException> _fail(String errorMsg, Object... msgParams) {
        return () -> new RuntimeException(format(errorMsg, msgParams));
    }

    /**
     * Similarly to lombok {@link SneakyThrows }, wraps any checked / unchecked Exception into RuntimeException
     * <p>Adds additional error message and original Exception as cause.</p>
     * Usage example: <pre>{@code
     * rethrow(() -> somethingThrowsCheckedException(), "Something failed");
     * rethrow(() -> somethingCanFail(), "Something failed, see: '%s'", "some param");
     * }</pre>
     * @param getter wrapped function
     * @param errorMsg message if error happen
     * @param msgParams format params for errorMsg
     * @return function result
     * @param <T> function return type
     */
    public static <T> T rethrow(Supplier<T> getter, String errorMsg, Object... msgParams) {
        try {
            return getter.get();
        } catch(Throwable e) {
            throw new RuntimeException(format(errorMsg, msgParams), e);
        }
    }
}
