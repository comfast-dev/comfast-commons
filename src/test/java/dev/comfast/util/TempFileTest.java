package dev.comfast.util;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TempFileTest {
    public static final String CONTENT = "lol abc";

    @Test void readAndWriteFile() {
        var file = new TempFile("file.txt", true);
        file.write(CONTENT);
        assertThat(file.read()).isEqualTo(CONTENT);
    }

    @Test void readAndWriteFileInDirectory() {
        var file = new TempFile("dev.comfast/unittest/file.txt", true);
        file.write(CONTENT);
        assertThat(file.read()).isEqualTo(CONTENT);
    }

    /**
     * Run twice to check autoRemove
     */
    @Test void autoRemove() {
        var file = new TempFile("dev.comfast/fileToRemove.txt", true);
        assertThat(file.file).doesNotExist();

        file.write("abc");
        // ...
        // after all tests, file should be auto-removed
    }
}