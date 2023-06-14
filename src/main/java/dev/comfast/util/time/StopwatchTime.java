package dev.comfast.util.time;
import lombok.Getter;

import java.time.Duration;

/**
 * <p>Result of {@link Stopwatch} measurement.</p>
 * <p>End time === time when Constructor called</p>
 */
@Getter
public class StopwatchTime {
    public final long startTimestamp;
    public final long nanos;

    StopwatchTime(long startTimestamp, long startTimeNs) {
        this.startTimestamp = startTimestamp;
        nanos = System.nanoTime() - startTimeNs;
    }

    @Override public String toString() {
        return new TimeFormatter().formatNanoseconds(getNanos());
    }

    public Duration getDuration() {
        return Duration.ofNanos(getNanos());
    }

    public long getMillis() {
        return getNanos() / 1_000_000;
    }
}
