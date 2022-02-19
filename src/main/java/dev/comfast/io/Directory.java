package dev.comfast.io;
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.util.List;

public interface Directory {
    FileLoader getFile(String filePath);
    void copyDir(String dirPath, Path targetPath);
    /**
     * @return relative paths from all files in directory
     */
    @SneakyThrows
    List<String> getFileList(String inDirectory);
}
