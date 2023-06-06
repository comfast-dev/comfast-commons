package dev.comfast.util.time;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;
class TimeFormatterTest {
    private static final TimeFormatter timeFormatter = new TimeFormatter();

    @Test void humanReadableDurationTest() {
        assertAll(
            testNanos(1, "1ns"),
            testNanos(999, "999ns"),
            testNanos(1001, "1µs"),
            testNanos(1499, "1.5µs"),
            testNanos(1554, "1.55µs"),
            testNanos(1556, "1.56µs"),
            testNanos(1, "1ns"),
            testMillis(1, "1ms"),
            testMillis(12, "12ms"),
            testMillis(123, "123ms"),
            testMillis(12349, "12.3s"),
            testMillis(12351, "12.4s"),
            testMillis(59900, "59.9s"),
            testMillis(61000, "1m 1s"),
            testMillis(120000, "2m"),
            testMillis(123499, "2m 3s"),
            testMillis(123501, "2m 4s"),
            testMillis(3600000, "1h"),
            testMillis(3601000, "1h 1s"),
            testMillis(3661000, "1h 1m 1s")
        );
    }

    private Executable testMillis(int millisInput, String expectedOutput) {
        return () -> assertThat(timeFormatter.formatDuration(ofMillis(millisInput)))
            .isEqualTo(expectedOutput);
    }

    private Executable testNanos(long nanosInput, String expectedOutput) {
        return () -> assertThat(timeFormatter.formatNanoseconds(nanosInput)).as("formatNanoseconds(%s)", nanosInput)
            .isEqualTo(expectedOutput);
    }
}