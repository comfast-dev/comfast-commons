package dev.comfast.io;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceLoaderTest {
    private static final String FILE_CONTENT = "some content";
    private final ResourceLoader rootLoader = new ResourceLoader();
    private final ResourceLoader packageLoader = new ResourceLoader(getClass());

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
}
