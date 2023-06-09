package dev.comfast.util;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Represents file in default temp directory. File name can be prefixed with directory.
 * e.g. <pre>{@code
 * var myFile = new TempFile("comfastDir/myFile.txt");
 * myFile.save("abc");
 *
 * //... few moments later ...
 * myFile.load() // -> "abc"
 * }
 */
public class TempFile {
    public final Path file;
    public final boolean autoRemove;

    public TempFile(String tmpPath) {
        this(tmpPath, false);
    }

    public TempFile(String tmpPath, boolean autoRemove) {
        this.file = Path.of(System.getProperty("java.io.tmpdir"), tmpPath);
        this.autoRemove = autoRemove;
    }

    /**
     * Write content to file. Clear previous content.
     */
    @SneakyThrows
    public void write(String content) {
        Files.createDirectories(file.getParent());
        Files.writeString(file, content, UTF_8);

        if(autoRemove) addAutoRemoveHook();
    }

    /**
     * Read entire file.
     */
    @SneakyThrows
    public String read() {
        return Files.readString(file, UTF_8);
    }

    /**
     * Add shutdown hook that will try to remove file after main thread end.
     */
    private void addAutoRemoveHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(
            () -> {
                try {Files.deleteIfExists(file);
                } catch(IOException ignored) {} //don't care
            }
        ));
    }
}
