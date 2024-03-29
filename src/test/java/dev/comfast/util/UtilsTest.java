package dev.comfast.util;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.comfast.util.Utils.isNullOrEmpty;
import static dev.comfast.util.Utils.isTruthly;
import static dev.comfast.util.Utils.readResourceFile;
import static dev.comfast.util.Utils.transposeMatrix;
import static dev.comfast.util.Utils.trimString;
import static dev.comfast.util.Utils.withSystemProp;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void isTruthlyTest() {
        assertThat(isTruthly(null)).as("null").isFalse();
        assertThat(isTruthly(0)).as("int 0").isFalse();
        assertThat(isTruthly(0L)).as("long 0").isFalse();
        assertThat(isTruthly(0.0f)).as("float 0").isFalse();
        assertThat(isTruthly(0.0d)).as("double 0").isFalse();
        assertThat(isTruthly("false")).as("string: 'false'").isFalse();
        assertThat(isTruthly("")).as("empty string").isFalse();
        assertThat(isTruthly("0")).as("string: '0'").isFalse();
        assertThat(isTruthly("0.0")).as("string: '0.0'").isFalse();

        assertThat(isTruthly("1")).as("string: '1'").isTrue();
        assertThat(isTruthly(1)).as("int: 1").isTrue();
        assertThat(isTruthly(-1)).as("int: -1").isTrue();
        assertThat(isTruthly(1L)).as("long: '1'").isTrue();
        assertThat(isTruthly(0.001f)).as("float: '0.001'").isTrue();
    }

    @Test void isNullOrEmptyTest() {
        assertTrue(isNullOrEmpty(null));
        assertTrue(isNullOrEmpty(""));
        assertTrue(isNullOrEmpty(" "));
        assertTrue(isNullOrEmpty("  "));

        assertFalse(isNullOrEmpty("a"));
        assertFalse(isNullOrEmpty(" a "));
        assertFalse(isNullOrEmpty(0));
        assertFalse(isNullOrEmpty(1));
        assertFalse(isNullOrEmpty(0.0f));
        assertFalse(isNullOrEmpty(0.0d));
        assertFalse(isNullOrEmpty(0L));
        assertFalse(isNullOrEmpty(0.1f));
        assertFalse(isNullOrEmpty(0.1d));
        assertFalse(isNullOrEmpty(1L));
    }

    @Test void readResourceFileTest() {
        assertThat(readResourceFile("test.txt")).isEqualTo("test content");
        assertThat(readResourceFile("some/test2.txt")).isEqualTo("test2 content");

        assertThatThrownBy(() -> readResourceFile("nothing.txt"))
            .hasMessageContaining("Not found resource file: nothing.txt");
    }
}