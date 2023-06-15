package dev.comfast.util;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.NoSuchFileException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TempFileTest {
    public static final String CONTENT = "test lol abc";
    TempFile file = new TempFile("dev.comfast/unittest/file.txt", true);
    TempFile fileToRemove = new TempFile("dev.comfast/unittest/fileToRemove.txt", true);
    TempFile notExistFile = new TempFile("notExistFile.txt");

    @Test void readAndWriteTest() {
        file.write(CONTENT);
        assertThat(file.read()).isEqualTo(CONTENT);
    }

    @Test void deleteAndExistsTest() {
        file.write("abc");
        assertThat(file.exists()).isTrue();
        assertThat(file.file).exists();

        assertThat(file.delete()).isTrue();
        assertThat(file.delete()).isFalse();
        assertThat(file.exists()).isFalse();
        assertThat(file.file).doesNotExist();
    }

    @Test void readEmptyFileThrowsTest() {
        assertThatThrownBy(() -> notExistFile.read())
            .isInstanceOf(NoSuchFileException.class)
            .hasMessageContaining("notExistFile.txt");
    }

    @Disabled("run manually, twice to verify feature")
    @Test void autoRemove() {
        assertThat(fileToRemove.file).doesNotExist();

        file.write("abc");
        // ...
        // while JVM shutdown, file should be auto-removed
    }
}