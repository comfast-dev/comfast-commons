package dev.comfast.io;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static dev.comfast.io.CopyFileUtils.copyDir;

@RequiredArgsConstructor
public class ResourceLoader {
    private final Class<?> resourceClass;

    @SneakyThrows
    public String read(String resourcePath) {
        return IOUtils.toString(getUrl(resourcePath), UTF_8);
    }

    @SneakyThrows
    public void copy(String resourcePath, Path targetDir) {
        copyDir(getUrl(resourcePath), targetDir);
    }

    @SneakyThrows
    private URL getUrl(String filePath) {
        URL url = resourceClass.getResource(filePath);
        if(url == null) throw new RuntimeException("Not found resource in: " + fullPath(filePath));
        return url;
    }

    private String fullPath(String filePath) {
        return resourceClass.getPackageName().replaceFirst("^package", "")
            .replaceAll("\\.", "/") + "/" + filePath;
    }
}
