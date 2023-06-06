package dev.comfast.util.time;
import org.junit.jupiter.api.Test;

import static dev.comfast.util.Utils.sleep;
import static org.assertj.core.api.Assertions.assertThat;

class StopwatchTest {
    @Test void measureTest() {
        var result = Stopwatch.measure(() -> sleep(10));

        assertThat(result.getNanos())
            .isGreaterThan(10_000_000)
            .isLessThan(20_000_000);
    }

    @Test void stopwatchResult() {
        final int MAX_DELAY_MS = 2;
        final long BEFORE_NS = System.nanoTime();
        final long BEFORE_MS = System.currentTimeMillis();

        var stopwatch = new Stopwatch(); //started
        var result = stopwatch.time(); //ended

        assertThat(result.startTimestamp)
            .isGreaterThanOrEqualTo(BEFORE_MS)
            .isLessThan(BEFORE_MS + MAX_DELAY_MS);
        assertThat(result.startTimeNs)
            .isGreaterThan(BEFORE_NS)
            .isLessThan(BEFORE_NS + MAX_DELAY_MS * 1_000_000);
        assertThat(result.endTimeNs)
            .isGreaterThan(BEFORE_NS)
            .isLessThan(System.nanoTime());
        assertThat(result.getDuration().toMillis())
            .isLessThan(MAX_DELAY_MS);

        var result2ndLap = stopwatch.time();
        assertThat(result2ndLap.startTimestamp).isEqualTo(result.startTimestamp);
        assertThat(result2ndLap.startTimeNs).isEqualTo(result.startTimeNs);
        assertThat(result2ndLap.endTimeNs).isGreaterThan(result.endTimeNs);
        assertThat(result2ndLap.endTimeNs).isLessThan(System.nanoTime());
    }
}