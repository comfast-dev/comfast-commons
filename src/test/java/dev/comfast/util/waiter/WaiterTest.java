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
    Waiter waiter0 = new Waiter(0);
    Waiter waiter = new Waiter(150);
    Waiter longWaiter = new Waiter(300);

    @Test
    void waitForTest() {
        var time = Stopwatch.measure(() -> waiter.waitFor(() -> sleep(5)));
        assertThat(time.getMillis())
            .isGreaterThanOrEqualTo(5);
    }

    @Test
    void waitForTimeoutTest() {
        assertThatCode(() -> longWaiter.waitFor(veryBusyFunction())).doesNotThrowAnyException();
        assertThatThrownBy(() -> waiter.waitFor(veryBusyFunction()))
            .isInstanceOf(WaitTimeout.class)
            .hasMessageContaining("Wait failed after 150ms,")
            .cause().hasMessageContaining("I'm busy now !");
    }

    @Test
    void waitForValueTest() {
        var result = longWaiter.waitForValue(veryBusyFunction());
        assertThat(result).isEqualTo("ok, I'm done");

        var someFalsyValue = 0;
        assertThatThrownBy(() -> waiter.waitForValue(() -> someFalsyValue))
            .isInstanceOf(WaitTimeout.class)
            .hasMessageContaining("Wait failed after 150ms")
            .cause().hasMessageContaining("Wait error. Action result is '0'");
    }

    @Test void waitStatsTest() {
        assertThatExceptionOfType(WaitTimeout.class)
            .isThrownBy(() -> waiter.waitFor(veryBusyFunction()))
            .extracting(WaitTimeout::getStats).asString()
            .contains("IllegalArgumentException: It's illegal, sorry\n")
            .contains("RuntimeException: I'm busy now !");
    }

    @Test void includeCauseInErrorMessageTest() {
        var waiter1 = new Waiter(10).configure(c -> c.includeCauseInErrorMessage(false));
        var waiter2 = new Waiter(100).configure(c -> c.includeCauseInErrorMessage(true));

        assertThatThrownBy(() -> waiter1.waitFor(this::failFunction))
            .hasMessageNotContaining("failFunction error");

        assertThatThrownBy(() -> waiter2.waitFor(this::failFunction))
            .hasMessageContaining("failFunction error");
    }

    @Test void timeout0_shouldBeCalledOnce() {
        var res = waiter0.waitFor(() -> "ok");
        assertThat(res).isEqualTo("ok");

        assertThatExceptionOfType(WaitTimeout.class)
            .isThrownBy(() -> waiter0.waitFor(this::failFunction))
            .withMessageContaining("Wait failed after 0ms")
            .withCauseExactlyInstanceOf(RuntimeException.class)
            .withStackTraceContaining("failFunction error");
    }

    @Test void waiterShouldLogDescription() {
        var waiter = new Waiter(10).configure(c -> c.description("very busy function"));

        assertThatThrownBy(() -> waiter.waitFor(veryBusyFunction()))
            .hasMessageContaining("Wait for very busy function failed after");

        assertThatThrownBy(() -> waiter.configure(c -> c.description(null)).waitFor(veryBusyFunction()))
            .hasMessageContaining("Wait failed after");
    }

    /**
     * Time 0 is time when function is created.
     *
     * @return function that will return after 200ms
     * @throws IllegalArgumentException before 100ms
     * @throws RuntimeException         before 200ms
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

    private void failFunction() {
        throw new RuntimeException("failFunction error");
    }
}