
package dev.comfast.io;
import dev.comfast.errors.Fail;
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
                throw new Fail("Not handle '%s' protocol from: %s ", url.getProtocol(), url);
        }
    }

    @SneakyThrows
    @SuppressWarnings("RegExpRedundantEscape")
    public static void copyDirFromJar(URL resourceUrl, Path targetDir) {
        String[] paths = splitJarUrl(resourceUrl);
        JarFile jar = new JarFile(paths[0]);
        String insideJarPath = paths[1];

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

    private static String[] splitJarUrl(URL jarUrl) {
        @SuppressWarnings("RegExpRedundantEscape")
        RgxMatch match = rgx("jar:file:([^!]+)!\\/(.*)")
            .match(jarUrl.toString())
            .throwIfEmpty("Invalid url for jarfile: " + jarUrl);
        return new String[]{match.group(1), match.group(2)};
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