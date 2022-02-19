package dev.comfast.io.core;
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.util.List;

public interface Directory {
    FileLoader getFile(String filePath);

    void copyTo(Path targetPath);
    void copyDir(String subPath, Path targetPath);
    /**
     * @return relative paths from all files in directory
     */
    List<String> getFileList(String inDirectory);
}
