package dev.comfast.util.time;
import org.junit.jupiter.api.Test;

import static dev.comfast.util.Utils.sleep;
import static org.assertj.core.api.Assertions.assertThat;

class StopwatchTest {
    @Test void measureTest() {
        var result = Stopwatch.measure(() -> sleep(10));

        assertThat(result.getNanos())
            .isGreaterThan(10_000_000);
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
        assertThat(result.nanos)
            .isPositive()
            .isLessThan(MAX_DELAY_MS * 1_000_000);
        assertThat(result.getMillis()).isLessThanOrEqualTo(MAX_DELAY_MS);
        assertThat(result.getDuration().getNano()).isEqualTo(result.nanos);

        var result2ndLap = stopwatch.time();
        assertThat(result2ndLap.startTimestamp).isEqualTo(result.startTimestamp);
        assertThat(result2ndLap.nanos)
            .isGreaterThan(result.nanos)
            .isLessThan(System.nanoTime() - BEFORE_NS);
    }
}