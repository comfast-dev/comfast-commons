package dev.comfast.util;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.IntStream;

import static java.lang.System.clearProperty;
import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static java.util.stream.Collectors.toList;

public class Utils {
    /**
     * Unchecked sleep.
     * Checked {@link InterruptedException} is wrapped into {@link RuntimeException}.
     */
    @SneakyThrows
    public static void sleep(long ms) {
        Thread.sleep(ms);
    }

    /**
     * @param input string or object
     * @param maxLength max length of result string
     * @return First characters from input string
     */
    public static String trimString(Object input, int maxLength) {
        var str = input.toString();
        return str.length() > maxLength
               ? str.substring(0, maxLength) + "..."
               : str;
    }

    /**
     * @param matrix list of lists eg.
     * <pre>{@code
     *   ["1", "2", "3", "4"],
     *   ["5", "6", "7", "8"]
     * }
     * @return transposed list of lists, eg.
     * <pre>{@code
     *   ["1", "5"],
     *   ["2", "6"],
     *   ["3", "7"],
     *   ["4", "8"],
     * }
     */
    public static <T> List<List<T>> transposeMatrix(final List<List<T>> matrix) {
        return IntStream.range(0, matrix.get(0).size())
            .mapToObj(i -> matrix.stream().map(row -> row.get(i)).collect(toList()))
            .collect(toList());
    }

    /**
     * Restore system property after func is done.
     * It's safe to edit it inside given function, for test purposes or any other.
     * e.g. <pre>{@code
     *     // here "my.timeout" is 3000
     *     withSystemProp("my.timeout", () -> {
     *         System.setProperty("my.timeout", "0");
     *         doSomeTests();
     *     })
     *     // here "my.timeout" is restored to 3000
     * }
     * @param func within this function can edit system prop freely without side effects for the rest of the code
     * }
     */
    public static void withSystemProp(String systemPropertyName, Runnable func) {
        final String restoreValue = getProperty(systemPropertyName);
        try {
            func.run();
        } finally {
            if(restoreValue == null) clearProperty(systemPropertyName);
            else setProperty(systemPropertyName, restoreValue);
        }
    }
}
