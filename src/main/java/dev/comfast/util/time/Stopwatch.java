package dev.comfast.util.time;
import lombok.RequiredArgsConstructor;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.nanoTime;

/**
 * <p>Class for time measurements.</p>
 * <p>Start measurement === Constructor call time</p>
 * <p>Every call {@link Stopwatch#time()} creates measurement with the same start time</p>
 */
@RequiredArgsConstructor
public class Stopwatch {
    private final long startTimestamp, startTimeNs;

    public Stopwatch() {
        this(currentTimeMillis(), nanoTime());
    }

    /** Measure time of running given function */
    @SuppressWarnings({"ReturnInsideFinallyBlock", "finally"})
    public static StopwatchTime measure(Runnable measuredFunction) {
        var stopwatch = new Stopwatch();
        try { measuredFunction.run(); }
        finally { return stopwatch.time(); }
    }

    /**
     * Can be used multiple times to get intermediate times.
     *
     * @return current stopwatch time
     */
    public StopwatchTime time() {
        return new StopwatchTime(startTimestamp, startTimeNs);
    }
}
