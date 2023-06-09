package dev.comfast.util.time;
import lombok.Getter;

import java.time.Duration;

/**
 * <p>Result of {@link Stopwatch} measurement.</p>
 * <p>End time === time when Constructor called</p>
 */
public class StopwatchTime {
    @Getter public final long startTimestamp, startTimeNs, endTimeNs;

    StopwatchTime(long startTimestamp, long startTimeNs) {
        this.startTimestamp = startTimestamp;
        this.startTimeNs = startTimeNs;
        endTimeNs = System.nanoTime();
    }

    @Override public String toString() {
        return new TimeFormatter().formatNanoseconds(getNanos());
    }

    public Duration getDuration() {
        return Duration.ofNanos(getNanos());
    }

    public long getNanos() {
        return endTimeNs - startTimeNs;
    }

    public long getMillis() {
        return getNanos() / 1_000_000;
    }
}
