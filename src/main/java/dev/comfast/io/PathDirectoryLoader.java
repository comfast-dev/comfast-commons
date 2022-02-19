package dev.comfast.io;
import dev.comfast.rgx.RgxMatch;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static dev.comfast.io.CopyFileUtils.copyDirFromJar;
import static dev.comfast.rgx.RgxApi.rgx;

@RequiredArgsConstructor
public class PathDirectoryLoader implements Directory {
    private final URL url;

    @SneakyThrows
    public FileLoader getFile(String filePath) {
        return new FileLoader(new URL(url + "/" + filePath));
    }

    @Override public void copyDir(String dirPath, Path targetPath) {

    }

    @Override public List<String> getFileList(String inDirectory) {
        return null;
    }
//
//    @SneakyThrows
//    public void copyTo(Path targetPath) {
//
//        if(url.getProtocol().equals("jar")) copyDirFromJar(url, targetPath);
//        else throw new RuntimeException("");
//    }
//
//    /**
//     * @return relative paths from all files in subfolders
//     */
//    @SneakyThrows
//    public List<String> getFileList() {
//        String protocol = url.getProtocol();
//        if(protocol.equals("jar")) {
//            String[] paths = splitJarUrl(url);
//            return new ZipFileLoader(new URL(paths[0]), "").getFileList(paths[1]);
//        } else if(protocol.equals("file"))
//            throw new RuntimeException("not implemented");
//
//        throw new RuntimeException("not implemented");
//    }
//
//    /**
//     * @param jarUrl
//     * @return [jarPath, filePath]
//     */
//    private static String[] splitJarUrl(URL jarUrl) {
//        RgxMatch match = rgx("(jar:file:[^!]+!\\/)(.*)")
//            .match(jarUrl.toString())
//            .throwIfEmpty("Invalid url for jarfile: " + jarUrl);
//        return new String[] {match.group(1), match.group(2)};
//    }
}
