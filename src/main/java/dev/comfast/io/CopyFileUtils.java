
package dev.comfast.io;
import dev.comfast.rgx.RgxMatch;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static dev.comfast.rgx.RgxApi.rgx;
import static java.lang.String.format;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@RequiredArgsConstructor
public class CopyFileUtils {
    @SneakyThrows
    static void copyDir(Path fromDir, Path targetDir) {
        Files.list(fromDir).forEach(path -> {
            if(path.equals(fromDir)) return;
            if(Files.isDirectory(path)) {
                copyDir(path, targetDir.resolve(path.getFileName()));
            } else {
                copyFile(targetDir, path);
            }
        });
    }

    @SneakyThrows
    static void copyFile(Path filePath, Path targetDir) {
        Path newPath = targetDir.resolve(filePath.getFileName());
        Files.createDirectories(targetDir);
        Files.copy(filePath, newPath, REPLACE_EXISTING);
    }

    /**
     * Intentionally not public.
     *
     * @see #copyDir(Path, Path)
     * @see ResourceLoader#copy(String, Path)
     */
    @SneakyThrows
    static void copyDir(URL url, Path targetDir) {
        String protocol = url.getProtocol();
        switch(protocol) {
            case "file":
                copyDir(Path.of(url.toURI()), targetDir); break;
            case "jar":
                copyDirFromJar(url, targetDir); break;
            default:
                throw new RuntimeException(format("Not handle '%s' protocol from: %s ", url.getProtocol(), url));
        }
    }

    @SneakyThrows
    @SuppressWarnings("RegExpRedundantEscape")
    private static void copyDirFromJar(URL resourceUrl, Path targetDir) {
        RgxMatch match = rgx("jar:file:([^!]+)!\\/(.*)")
            .match(resourceUrl.toString())
            .throwIfEmpty("Invalid url for jarfile: " + resourceUrl);

        JarFile jar = new JarFile(match.group(1));
        String insideJarPath = match.group(2);

        Collections.list(jar.entries()).stream()
            .filter(e -> !e.isDirectory() &&
                         e.getName().startsWith(insideJarPath) &&
                         !e.getName().equals(insideJarPath))
            .forEach(jarEntry -> {
                String fileDir = jarEntry.getName().replace(insideJarPath, "")
                    .replaceFirst("^\\/", "");
                Path newPath = targetDir.resolve(Path.of(fileDir));
                copyFile(sneakyStream(jar, jarEntry), newPath);
            });
    }

    @SneakyThrows
    private static InputStream sneakyStream(JarFile jar, JarEntry jarEntry) {
        return jar.getInputStream(jarEntry);
    }

    @SneakyThrows
    private static void copyFile(InputStream stream, Path targetFilePath) {
        Files.createDirectories(targetFilePath.getParent());
        Files.copy(stream, targetFilePath, REPLACE_EXISTING);
    }
}