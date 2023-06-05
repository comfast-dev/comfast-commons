package dev.comfast.errors;
import java.util.function.Supplier;

import static java.lang.String.format;

public class ErrorKit {
    /**
     * @param msgParams printf params for errorMsg
     * @return Supplier for RuntimeException, fits to Streams/Optionals
     * Usage:
     * <li> found = someStream.findFirst().orElseThrow(_fail("Not found '%s'", someParam))
     */
    public static Supplier<RuntimeException> _fail(String errorMsg, Object... msgParams) {
        return () -> new RuntimeException(format(errorMsg, msgParams));
    }

    /**
     * In case of fail rethrows RuntimeException with additional error message.
     * Usage:
     * <li>rethrow(() -> somethingCanFail(), "Something failed");
     * <li>rethrow(() -> somethingCanFail(), "Something failed, see: '%s'", "some param");
     */
    public static <T> T rethrow(Supplier<T> getter, String errorMsg, Object... msgParams) {
        try {
            return getter.get();
        } catch(Throwable e) {
            throw new RuntimeException(format(errorMsg, msgParams), e);
        }
    }
}
