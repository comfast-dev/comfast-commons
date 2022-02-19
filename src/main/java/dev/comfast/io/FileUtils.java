
package dev.comfast.io;
import dev.comfast.errors.Fail;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.file.PathUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.nio.file.Files.isDirectory;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@RequiredArgsConstructor
public class FileUtils {
    public static Stream<Path> getAllFilesRelativePaths(Path path) {
        return getAllFilesAbsolutePaths(path).map(p -> p.relativize(path));
    }

    public static Stream<Path> getAllFilesAbsolutePaths(Path path) {
        return sneakyFilesList(path)
            .flatMap(p -> isDirectory(p) ? getAllFilesAbsolutePaths(p) : Stream.of(p));
    }

    @SneakyThrows
    private static Stream<Path> sneakyFilesList(Path path) {
        return Files.list(path);
    }

    private static void shouldBeDirectory(Path dirPath) {
        if(!isDirectory(dirPath)) throw new Fail("%s should be directory", dirPath);
    }

    private static void shouldBeFile(Path filePath) {
        if(isDirectory(filePath)) throw new Fail("%s should be file, actual is directory", filePath);
    }
}