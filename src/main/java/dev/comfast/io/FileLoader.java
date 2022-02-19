package dev.comfast.io;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;

@RequiredArgsConstructor
public class FileLoader {
    private final URL fileUrl;

    @SneakyThrows
    public String read() {
        return IOUtils.toString(fileUrl, UTF_8);
    }

    @SneakyThrows
    public void copyTo(Path targetDir) {
        Path fileName = Path.of(fileUrl.toURI()).getFileName();
        IOUtils.copy(fileUrl, targetDir.resolve(fileName).toFile());
    }
}
