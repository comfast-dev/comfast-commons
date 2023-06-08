package dev.comfast.util.waiter;
import dev.comfast.util.TerminalGenerator;
import dev.comfast.util.time.TimeFormatter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class WaiterStats {
    @RequiredArgsConstructor
    private static class WaiterErrorResult {
        final long time;
        final long loopTime;
        final Class<? extends Throwable> clazz;
    }
    private final Map<Class<? extends Throwable>, Throwable> errors = new HashMap<>();
    private final List<WaiterErrorResult> errorResults = new ArrayList<>();

    /**
     * @param error Throwable to be added
     * @param time ms from start waitFor
     * @param loopTime ms from start current loop
     */
    public void addError(Throwable error, long time, long loopTime) {
        errors.put(error.getClass(), error);
        errorResults.add(new WaiterErrorResult(time, loopTime, error.getClass()));
    }

    /**
     * @return last added error
     */
    public Throwable recentError() {
        var type = errorResults.get(errorResults.size() - 1).clazz;
        return errors.get(type);
    }

    /**
     * @return String like:
     * <pre>{@code
     * time | loopTime | error
     * 1s   | 26ms     | RuntimeException
     * 2s   | 330ms    | RuntimeException
     * 3s   | 110ms    | RuntimeException: oh no !
     * 4.5s | 360ms    | InvalidArgumentException
     * 6s   | 12ms     | InvalidArgumentException
     * 7.3s | 130ms    | InvalidArgumentException
     * 8s   | 201ms    | InvalidArgumentException: something has broken
     * }
     */
    public String getStats() {
        var timeFormatter = new TimeFormatter();

        //noinspection unchecked
        List<String>[] data = new List[errorResults.size()];
        List<Class<? extends Throwable>> addedErrors = new ArrayList<>();
        for(int i = errorResults.size() - 1; i >= 0; i--) {
            WaiterErrorResult result = errorResults.get(i);

            String description = "";
            if(!addedErrors.contains(result.clazz)) {
                description = ": " + errors.get(result.clazz).getMessage();
                addedErrors.add(result.clazz);
            }

            data[i] = List.of(
                timeFormatter.formatNanoseconds(result.time * 1_000_000),
                timeFormatter.formatNanoseconds(result.loopTime * 1_000_000),
                result.clazz.getSimpleName() + description);
        }

        return new TerminalGenerator(" | ").table(
            List.of("time", "loopTime", "error"), asList(data));
    }
}
