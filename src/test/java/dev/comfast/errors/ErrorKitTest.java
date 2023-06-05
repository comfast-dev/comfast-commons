package dev.comfast.errors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static dev.comfast.errors.ErrorKit._fail;
import static dev.comfast.errors.ErrorKit.rethrow;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ErrorKitTest {
    public static final String EXPECTED_ERROR_MSG = "Not found 'c'";

    @Test
    public void failSupply() {
        assertThat(_fail("some text").get())
            .isInstanceOf(RuntimeException.class)
            .hasMessage("some text");

        assertThat(_fail("some %s text %d", "other", 2).get())
            .isInstanceOf(RuntimeException.class)
            .hasMessage("some other text 2");
    }

    @Test
    public void failUsageInOptional() {
        Assertions.assertThatThrownBy(this::failInStream)
            .hasMessageContaining(EXPECTED_ERROR_MSG)
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void rethrowPassTest() {
        var result = rethrow(() -> "all is fine", "error msg never happen");
        assertThat(result).isEqualTo("all is fine");
    }

    @Test
    public void rethrowFailTest() {
        assertThatThrownBy(() -> rethrow(this::failInStream, "Additional error message"))
            .hasMessage("Additional error message")
            .isInstanceOf(RuntimeException.class)
            .hasRootCauseMessage(EXPECTED_ERROR_MSG)
            .hasCauseInstanceOf(RuntimeException.class);
    }

    @SuppressWarnings("ConstantConditions")
    private String failInStream() {
        final String SEARCH_TEXT = "c";
        return Stream.of("a", "b").filter(s -> s.contains(SEARCH_TEXT))
            .findFirst()
            .orElseThrow(_fail("Not found '%s'", SEARCH_TEXT));
    }
}