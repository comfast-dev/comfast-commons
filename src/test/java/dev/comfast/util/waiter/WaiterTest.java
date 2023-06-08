package dev.comfast.util.waiter;
import dev.comfast.util.time.Stopwatch;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static dev.comfast.util.Utils.sleep;
import static java.lang.System.currentTimeMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WaiterTest {
    Waiter waiter = new Waiter(150);
    Waiter longWaiter = new Waiter(300);
    @Test
    void waitForTest() {
        var time = Stopwatch.measure(() -> waiter.waitFor(() -> sleep(5)));
        assertThat(time.getMillis())
            .isGreaterThanOrEqualTo(5)
            .isLessThan(10);
    }

    @Test
    void waitForTimeoutTest() {
        assertThatCode(() -> longWaiter.waitFor(veryBusyFunction())).doesNotThrowAnyException();
        assertThatThrownBy(() -> waiter.waitFor(veryBusyFunction()))
            .isInstanceOf(WaitTimeout.class)
            .hasMessageContaining("Wait failed after 150ms, tried 4 times.")
            .cause().hasMessageContaining("I'm busy now !");
    }

    @Test
    void waitForValueTest() {
        var result = longWaiter.waitForValue(veryBusyFunction());
        assertThat(result).isEqualTo("ok, I'm done");

        var someFalsyValue = 0;
        assertThatThrownBy(() -> waiter.waitForValue(() -> someFalsyValue))
            .isInstanceOf(WaitTimeout.class)
            .hasMessageContaining("Wait failed after 150ms, tried 4 times.")
            .cause().hasMessageContaining("Wait error. Action result is '0'");
    }

    @Test void waitStatsTest() {
        assertThatExceptionOfType(WaitTimeout.class)
            .isThrownBy(() -> waiter.waitFor(veryBusyFunction()))
            .extracting(WaitTimeout::getStats).asString()
            .contains("IllegalArgumentException: It's illegal, sorry\n")
            .contains("RuntimeException: I'm busy now !");
    }

    /**
     * Time 0 is time when function is created.
     * @throws IllegalArgumentException before 100ms
     * @throws RuntimeException before 200ms
     * @return function that will return after 200ms
     */
    private Supplier<String> veryBusyFunction() {
        long successTime = currentTimeMillis() + 200;
        long illegalTime = successTime - 100;

        return () -> {
            long now = currentTimeMillis();
            if(now < illegalTime) throw new IllegalArgumentException("It's illegal, sorry");
            if(now < successTime) throw new RuntimeException("I'm busy now !");
            return "ok, I'm done";
        };
    }
}