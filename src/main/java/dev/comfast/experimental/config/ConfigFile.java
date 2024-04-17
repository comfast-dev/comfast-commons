package dev.comfast.experimental.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

@RequiredArgsConstructor
public class ConfigFile {
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private static final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
    private static final MapFlatter flatter = new MapFlatter();
    /**
     * in root project path or resources path
     */
    private final String fileName;

    /**
     * Does all what need to get flat map of any type of config file.
     *
     * @param required Ignore or not e.g. FileNotFound Exceptions
     * @return flatted file map
     */
    @SneakyThrows
    public Map<String, String> readAsFlatMap(boolean required) {
        try {
            ConfigFileType fileType = ConfigFileType.parse(fileName);
            String fileContent = readFileContent(fileName);
            Map<?, ?> fullMap = objectMapperRead(fileType, fileContent);
            return flatter.flat(fullMap);
        } catch(IOException e) {
            if(!required && e.getMessage().startsWith("Not found config file")) {
                return new HashMap<>();
            }
            throw e;
        }
    }

    /**
     * Read given file. Search in locations: <ul>
     * <li>1. App root</li>
     * <li>2. resources folder</li>
     * </ul>
     *
     * @param fileName config file name
     * @return file content
     */
    private String readFileContent(String fileName) throws IOException {
        Path rootPath = Path.of(fileName);
        if(rootPath.toFile().isFile() && rootPath.toFile().exists()) {
            return Files.readString(rootPath, UTF_8);
        }
        String fileFromResources = readResourceStream(fileName);
        if(fileFromResources != null) return fileFromResources;

        else throw new IOException(format("Not found config file '%s', searched in locations: %n%s%n%s",
                fileName, rootPath.toFile().getAbsolutePath(), printResourcesFolders(fileName)
            ));
    }

    @SneakyThrows
    private Map<?, ?> objectMapperRead(ConfigFileType type, String fileContent) {
        switch(type) {
            case JSON:
                return jsonMapper.readValue(fileContent, Map.class);
            case YAML:
                return yamlMapper.readValue(fileContent, Map.class);
            case PROPERTIES:
                var p = new Properties();
                p.load(new StringReader(fileContent));
                return p;
        }
        throw new IllegalArgumentException("never happen");
    }

    /**
     * Read file from resources folder using ResourceStream.
     * @return file content or null
     */
    @Nullable private String readResourceStream(String fileName) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(joining(System.lineSeparator()));
            }
        }
    }

    private String printResourcesFolders(String suffix) {
        try {
            StringBuilder result = new StringBuilder();
            getClass().getClassLoader().getResources("").asIterator().forEachRemaining(url -> {
                String path = url.toString();
                if(path.startsWith("file") && path.contains("resources")) {
                    result.append(path.replaceFirst("^file:.", ""))
                        .append(suffix)
                        .append("\n");
                }
            });
            return result.toString();
        } catch(IOException e) {
            return "java resources folders";
        }
    }
}
