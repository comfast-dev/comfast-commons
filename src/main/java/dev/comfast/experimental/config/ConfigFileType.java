package dev.comfast.experimental.config;
import static dev.comfast.util.ErrorKit.rethrow;
import static dev.comfast.rgx.RgxApi.rgx;
import static java.lang.String.format;

enum ConfigFileType {
    JSON, YAML, PROPERTIES;

    public static ConfigFileType parse(String filePath) {
        String extension = rgx("\\.(\\w+)$").match(filePath)
            .throwIfEmpty("Failed to get file extension from filePath: " + filePath)
            .get(1);
        return rethrow(() -> valueOf(extension.toUpperCase()),
            (format("Extension '%s' in file '%s' is not handled." +
                    "\nHandle only file types: .json, .yaml, .properties",
                extension, filePath)));
    }
}
