package dev.comfast.util;
import lombok.SneakyThrows;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.System.clearProperty;
import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static java.nio.charset.StandardCharsets.UTF_8;
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
     * Can pass any object to check, it will be converted to string.
     * @return true if string/object is null or its toString() is empty
     */
    public static boolean isNullOrEmpty(Object anyString) {
        return anyString == null || anyString.toString().trim().isEmpty();
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
     *         System.setProperty("my.timeout", "456");
     *         //or even System.clearProperty("my.timeout");
     *         doSomeTestsWith456Timeout();
     *     })
     *     // here "my.timeout" is restored to 3000
     * }
     * @param func within this function can edit system prop freely without side effects for the rest of the code
     * }
     */
    public static void withSystemProp(String systemPropertyName, Runnable func) {
        withSystemProp(systemPropertyName, null, func);
    }

    /**
     * Restore system property after func is done.
     * It's safe to edit it inside given function, for test purposes or any other.
     * e.g. <pre>{@code
     *     // here "my.timeout" is 3000
     *     withSystemProp("my.timeout", "0" () -> {
     *         doSomeTestsWithZeroTimeout();
     *     })
     *     // here "my.timeout" is restored to 3000
     * }
     * @param func within this function can edit system prop freely without side effects for the rest of the code
     * }
     */
    public static void withSystemProp(String systemPropertyName, String systemPropertyValue, Runnable func) {
        final String restoreValue = getProperty(systemPropertyName);
        try {
            if(systemPropertyValue != null) System.setProperty(systemPropertyName, systemPropertyValue);
            func.run();
        } finally {
            if(restoreValue == null) clearProperty(systemPropertyName);
            else setProperty(systemPropertyName, restoreValue);
        }
    }

    /**
     * @param input any object
     * @return false if null, false, 0, trimmed empty string
     */
    public static boolean isTruthly(Object input) {
        if(input == null) return false;
        switch(input.toString().trim()) {
            case "":
            case "false":
            case "0":
            case "0.0":
                return false;
            default: return true;
        }
    }

    /**
     * Read resource file content.
     * @param resourcePath e.g. fileNAme.txt or some/folder/file.txt
     * @return file content
     */
    @SneakyThrows
    public static String readResourceFile(String resourcePath) {
        var stream = Utils.class.getClassLoader().getResourceAsStream(resourcePath);
        if(stream == null) throw new RuntimeException("Not found resource file: " + resourcePath);
        BufferedInputStream bis = new BufferedInputStream(stream);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        for (int result = bis.read(); result != -1; result = bis.read()) {
            buf.write((byte) result);
        }

        return buf.toString(UTF_8);
    }
}
