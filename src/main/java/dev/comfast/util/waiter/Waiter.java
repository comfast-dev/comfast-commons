package dev.comfast.util.waiter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.function.Supplier;

import static dev.comfast.util.Utils.isTruthly;
import static dev.comfast.util.Utils.sleep;
import static java.lang.String.format;

@RequiredArgsConstructor
public class Waiter {
    private final WaiterConfig conf;

    public Waiter(long timeoutMs) {
        conf = new WaiterConfig(timeoutMs, 50, 5000, 5, "");
    }

    /**
     * Repeat the action till it ends without exception.
     *
     * @throws WaitTimeout with detailed information about Exceptions happen
     */
    public void waitFor(Runnable action) {
        doWaitFor(() -> {action.run(); return true;}, false);
    }

    /**
     * Repeat the action till it ends without exception.
     *
     * @return action result
     * @throws WaitTimeout with detailed information about Exceptions happen
     */
    public <T> T waitFor(Supplier<T> action) {
        return doWaitFor(action, false);
    }

    /**
     * Repeat the action till it returns truthy value.
     * <p>Ignores any Exception during subsequent tries.</p>
     *
     * @throws WaitTimeout with detailed information about Exceptions happen
     */
    public <T> T waitForValue(Supplier<T> action) {
        return doWaitFor(action, true);
    }

    /**
     * Repeat given action till it end and return value (depend on shouldAssertResult flag).
     * <p>Ignores every Exception during subsequent tries.</p>
     *
     * @param action Given action
     * @param shouldAssertResult if true - will assert result to be truthly
     * @return action result
     * @throws WaitTimeout with detailed information about Exceptions happen
     */
    private <T> T doWaitFor(Supplier<T> action, boolean shouldAssertResult) {
        var state = new WaiterState(conf);
        var stats = new WaiterStats();

        while(state.nextTry()) {
            try {
                var result = action.get();
                if(shouldAssertResult && !isTruthly(result)) {
                    throw new IllegalArgumentException(format("Wait error. Action result is '%s'", Objects.toString(result, "null")));
                }
                return result;
            } catch(Throwable e) {
                stats.addError(e, state.getTime(), state.getLoopTime());
                sleep(state.getTimeTillNextLoop());
            }
        }
        throw new WaitTimeout(stats, format("Wait failed after %dms, tried %d times.",
                conf.timeoutMs, state.getTries()));
    }
}
