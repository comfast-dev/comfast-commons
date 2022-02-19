package dev.comfast.io;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceLoaderTest {
    private static final String FILE_CONTENT = "some content";
    private final ResourceLoader rootLoader = new ResourceLoader();
    private final ResourceLoader packageLoader = new ResourceLoader(getClass());

    private final Path tempPath = Path.of(System.getProperty("java.io.tmpdir")).resolve("ResourceLoaderTest");

    @BeforeEach
    public void prepareDirs() throws IOException {
//        Files.deleteIfExists(tmpPath);
        Files.createDirectories(tempPath);
    }

    @Test public void readFile() {
        assertThat(rootLoader.getFile("myFile.txt").read())
            .isEqualTo(FILE_CONTENT);
        assertThat(packageLoader.getFile("some-dir/myFile.txt").read())
            .isEqualTo(FILE_CONTENT);
        assertThat(packageLoader.getFile("myFile.txt").read())
            .isEqualTo(FILE_CONTENT);
//        new ResourceLoader(Path.of("something")).readFile("myFile.txt");
    }

    @Test public void readFileFromArchive() {
        assertThat(rootLoader.getZipFile("dev/comfast/io/files.zip").getFile("archivedFile.txt").read())
            .isEqualTo(FILE_CONTENT);
        assertThat(rootLoader.getZipFile("dev/comfast/io/files.zip").getFile("archivedDir/archivedFile.txt").read())
            .isEqualTo(FILE_CONTENT);
        assertThat(packageLoader.getZipFile("files.zip").getFile("archivedDir/archivedFile.txt").read())
            .isEqualTo(FILE_CONTENT);
        assertThat(packageLoader.getZipFile("files.zip").getFile("archivedDir/archivedFile.txt").read())
            .isEqualTo(FILE_CONTENT);

//        new ResourceLoader(path).readFromArchive("files.zip", "myFile.txt");
    }

    @Test public void copyFile() {
        rootLoader.getFile("myFile.txt").copyTo(tempPath);
        packageLoader.getFile("myFile.txt").copyTo(tempPath);
//        new ResourceLoader(path).copyFile("myFile.txt", tempPath);
    }

    @Test public void getFileList() {
        packageLoader.getZipFile("files.zip").getFileList("");
        //zip
        //no zip
    }

    @Test public void copyDir() {
        packageLoader.getZipFile("files.zip").copyDir("archivedDir", tempPath.resolve("copy"));
//        packageLoader.getDir("some-dir").copyTo(tempPath.resolve("copy"));
        //zip
        //no zip
    }

//    @Test public void copyDir() {
//        new ResourceLoader().copyDir("files", targetDir);
//        new ResourceLoader(getClass()).copyDir("files", targetDir);
//        new ResourceLoader(path).copyDir("files", targetDir);
//    }
}
