package dev.comfast.util;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.comfast.util.Utils.transposeMatrix;
import static dev.comfast.util.Utils.trimString;
import static dev.comfast.util.Utils.withSystemProp;
import static org.assertj.core.api.Assertions.assertThat;

class UtilsTest {
    @Test
    void trimStringTest() {
        assertThat(trimString("some string", 3))
            .isEqualTo("som...");

        assertThat(trimString("some string", 300))
            .isEqualTo("some string");
    }

    @Test
    void transposeMatrixTest() {
        var inputMatrix = List.of(
            List.of(1, 2, 3),
            List.of(4, 5, 6));

        assertThat(transposeMatrix(inputMatrix))
            .isEqualTo(List.of(
                List.of(1, 4),
                List.of(2, 5),
                List.of(3, 6)));
    }

    @Test
    void withSystemPropTest() {
        //given
        final String KEY = "xx.test", VALUE = "test value";
        System.setProperty(KEY, VALUE);

        //when
        withSystemProp(KEY, () -> System.setProperty(KEY, "changed value"));
        assertThat(System.getProperty(KEY))
            .isEqualTo(VALUE);

        //when
        withSystemProp(KEY, () -> System.clearProperty(KEY));
        assertThat(System.getProperty(KEY))
            .isEqualTo(VALUE);
    }
}