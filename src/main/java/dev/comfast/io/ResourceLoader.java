package dev.comfast.io;
import dev.comfast.errors.Fail;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.URL;

@RequiredArgsConstructor
public class ResourceLoader {
    private final Class<?> resourceClass;

    public ResourceLoader() {
        resourceClass = null;
    }

    @SneakyThrows
    public String readFile(String filePath) {
        return getFile(filePath).read();
    }

    @SneakyThrows
    public FileLoader getFile(String filePath) {
        return new FileLoader(getUrl(filePath));
    }

    public ZipFileLoader getZipFile(String zipFilePath) {
        if(!zipFilePath.endsWith(".zip")) throw new Fail("Archive file should end with .zip");
        URL url = getUrl(zipFilePath);

        boolean shouldFail = true;
        //noinspection ConstantConditions
        if(shouldFail && url.getProtocol().equals("jar")) {
            throw new Fail("Probably will not work (need to unpack zip before), todo check this.");
        }

        return new ZipFileLoader(url);
    }

    @SneakyThrows
    public Directory getDir(String directoryPath) {
        URL url = getUrl(directoryPath);
        return url.getProtocol().equals("jar")
               ? ZipFileLoader.combineJarAndDirectory(url, directoryPath)
               : new PathDirectoryLoader(getUrl(directoryPath));
    }

    @SneakyThrows
    private URL getUrl(String filePath) {
        URL url = resourceClass != null
                  ? resourceClass.getResource(filePath)
                  : ResourceLoader.class.getClassLoader().getResource(filePath);

        if(url == null) throw new Fail("Not found resource: " + fullPath(filePath));
        return url;
    }

    private String fullPath(String filePath) {
        @SuppressWarnings("ConstantConditions")
        String basePath = resourceClass == null
                          ? ResourceLoader.class.getClassLoader().getResource("").getPath()
                          : resourceClass.getResource("").getPath();

        return basePath + filePath;
    }
}
