package dev.comfast.util.time;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Duration;

import static dev.comfast.rgx.RgxApi.rgx;
import static java.lang.Double.parseDouble;

public class TimeFormatter {
    /**
     * Format any duration into human-friendly format e.g.
     * "120ns", vcgf"400µs", "10ms", "30s", "3h 20m 10s"
     * @param duration input duraiton
     * @return formatted string
     */
    public String formatDuration(Duration duration) {
        if(duration.toSeconds() < 60) return formatNanoseconds(duration.toNanos());

        var result = duration.toString()
            .replaceFirst("^PT", "")
            .replaceAll("([H|M])", "$1 ") //add spaces between segments
            .trim()
            .toLowerCase();

        // format seconds fractional part
        var match = rgx("(\\d\\.\\d+)s$").match(result);
        if(match.isPresent()) {
            var formattedSeconds = roundNumber(1, parseDouble(match.get(1)));
            return result.replaceFirst(match.get() + "$", formattedSeconds) + "s";
        } else {
            return result;
        }
    }

    /**
     * Format any nanoseconds using proper SI unit in human-friendly format e.g.
     * "120ns", "400µs", "10ms", "30s", "3h 20m 10s"
     * @param nanos input
     * @return formatted string
     */
    public String formatNanoseconds(long nanos) {
        if(nanos < 1_000) return roundNumber(nanos) + "ns";
        else if(nanos < 1_000_000) return roundNumber(nanos / 1000d) + "µs";
        else if(nanos < 1_000_000_000) return roundNumber(nanos / 1000_000d) + "ms";
        else return roundNumber(nanos / 1000_000_000d) + "s";
    }

    private String roundNumber(double input) {
        return roundNumber(3, input);
    }

    private String roundNumber(int significantDigits, double input) {
        return new BigDecimal(input)
            .round(new MathContext(significantDigits, RoundingMode.HALF_EVEN))
            .stripTrailingZeros()
            .toPlainString();
    }
}
