package dev.comfast.util;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.Runtime.getRuntime;
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
        if(autoRemove) {
            getRuntime().addShutdownHook(new Thread(this::delete));
        }
    }

    /**
     * Write content to file. Clear previous content.
     */
    @SneakyThrows
    public void write(String content) {
        Files.createDirectories(file.getParent());
        Files.writeString(file, content, UTF_8);
    }

    /**
     * Read entire file.
     */
    @SneakyThrows
    public String read() {
        return Files.readString(file, UTF_8);
    }

    public boolean exists() {
        return Files.exists(file);
    }

    public boolean delete() {
        try {
            return Files.deleteIfExists(file);
        } catch(IOException ignored) {
            return false;
        }
    }
}
