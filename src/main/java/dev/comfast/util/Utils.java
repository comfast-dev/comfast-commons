package dev.comfast.util;
import lombok.SneakyThrows;

public class Utils {
    /**
     * Unchecked sleep.
     * Checked {@link InterruptedException} is wrapped into {@link RuntimeException}.
     */
    @SneakyThrows
    public static void sleep(long ms) {
        Thread.sleep(ms);
    }
}
