package dev.comfast.io;
import dev.comfast.rgx.RgxMatch;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static dev.comfast.rgx.RgxApi.rgx;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@AllArgsConstructor
@RequiredArgsConstructor
public class ZipFileLoader implements Directory {
    private final URL zipUrl;
    private Path baseDir = Path.of("");

    @SneakyThrows
    public static ZipFileLoader combineJarAndDirectory(URL jarUrl, String dir) {
        String[] paths = splitJarUrl(jarUrl);

        URL zipUrl = new URL(paths[0]);
        return new ZipFileLoader(zipUrl, Path.of(paths[1]).resolve(dir));
    }

    public FileLoader getFile(String filePath) {
        return new FileLoader(getFileUrl(filePath));
    }

    @SuppressWarnings("RegExpRedundantEscape")
    public void copyDir(String zipDir, Path targetPath) {
        String dir = resolvePath(zipDir);
        getFileList(dir).forEach(currentFile -> {
            String relativePath = currentFile.replace(dir, "")
                .replaceFirst("^\\/", "");
            copyFile(getFileUrl(currentFile), targetPath.resolve(relativePath));
        });
    }

    /**
     * @return relative paths from all files in directory
     */
    @SneakyThrows
    public List<String> getFileList(String inDirectory) {
        String dir = resolvePath(inDirectory);
        ZipFile zip = new ZipFile(zipUrl.getFile());

        return Collections.list(zip.entries()).stream()
            .filter(e -> !e.isDirectory())
            .map(ZipEntry::getName).filter(name -> name.startsWith(dir))
            .collect(Collectors.toList());
    }

    @SneakyThrows
    private URL getFileUrl(String filePath) {
        return new URL("jar:" + zipUrl + "!/" + resolvePath(filePath));
    }

    private static String[] splitJarUrl(URL jarUrl) {
        @SuppressWarnings("RegExpRedundantEscape")
        RgxMatch match = rgx("jar:file:([^!]+)!\\/(.*)")
            .match(jarUrl.toString())
            .throwIfEmpty("Invalid url for jarfile: " + jarUrl);
        return new String[] {match.group(1), match.group(2)};
    }

    @SneakyThrows
    private static void copyFile(URL url, Path targetFilePath) {
        Files.createDirectories(targetFilePath.getParent());
        Files.copy(url.openStream(), targetFilePath, REPLACE_EXISTING);
    }

    private String resolvePath(String path) {
        return baseDir.resolve(path).toString().replaceAll("\\\\", "/");
    }
}
