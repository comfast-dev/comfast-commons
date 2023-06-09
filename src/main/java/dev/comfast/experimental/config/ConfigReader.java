package dev.comfast.experimental.config;
import java.util.Map;

import static dev.comfast.util.ErrorKit.rethrow;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;
import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static java.lang.String.join;
import static java.lang.System.getProperty;

/**
 * Read config from system properties / appConfig / referenceConfig
 */
public class ConfigReader {
    private final Map<String, String> appConfig;
    private final Map<String, String> referenceConfig;

    public ConfigReader() {
        referenceConfig = new ConfigFile("referenceConfig.yaml").readAsFlatMap(false);
        appConfig = new ConfigFile("appConfig.yaml").readAsFlatMap(false);
    }

    public double getDouble(String key) {
        return rethrow(
            () -> parseDouble(getString(key)),
            "Invalid format for config key: " + key);
    }

    public long getLong(String key) {
        return rethrow(
            () -> parseLong(getString(key)),
            "Invalid format for config key: " + key);
    }

    public boolean getBool(String key) {
        var str = getString(key);
        return rethrow(() -> parseBoolean(str),
            "Invalid format for config key: " + key);
    }

    /**
     * Get config value from: <ol>
     *     <li>System property</li>
     *     <li>appConfig.yaml/json/properties</li>
     *     <li>referenceConfig.yaml/json/properties</li>
     * </ol>
     * @param key config key
     * @return config value
     */
    public String getString(String key) {
        String sysProp = getProperty(key);
        if(sysProp != null) return sysProp;

        String appProp = appConfig.get(key);
        if(appProp != null) return appProp;

        String defaultProp = referenceConfig.get(key);
        if(defaultProp != null) return defaultProp;

        throw new RuntimeException(format(
            "Not found key: '%s' in config. Available keys are: \n%s", key, join("\n", referenceConfig.keySet())));
    }
}
