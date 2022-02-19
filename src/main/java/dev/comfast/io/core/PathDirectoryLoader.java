package dev.comfast.io.core;
import dev.comfast.io.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.file.PathUtils;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PathDirectoryLoader implements Directory {
    private final URL url;

    @SneakyThrows
    public FileLoader getFile(String filePath) {
        return new FileLoader(new URL(url + "/" + filePath));
    }

    @SneakyThrows
    @Override public void copyTo(Path targetPath) {
        PathUtils.copyDirectory(getPath(), targetPath);
    }

    @SneakyThrows
    @Override public void copyDir(String subPath, Path targetPath) {
        PathUtils.copyDirectory(getPath().resolve(subPath), targetPath);
    }

    @Override public List<String> getFileList(String inDirectory) {
        return FileUtils.getAllFilesAbsolutePaths(getPath().resolve(inDirectory))
            .map(Path::toString).collect(Collectors.toList());
    }

    @SneakyThrows private Path getPath() {
        return Paths.get(url.toURI());
    }

}
