package dev.comfast.experimental.config;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class ConfigApi {
    /**
     * Creates ConfigReader that read all configurations from <ol>
     * <li>System properties</li>
     * <li>appConfig.yaml</li>
     * <li>referenceConfig.yaml</li>
     * </ol>
     * For example: <pre>{@code
     * public static final ConfigReader conf = ConfigApi.readConfig();
     * // somewhere in application:
     * conf.getString("my.prop") // get my.prop from systemProperty, appConfig.yaml or referenceConfig.yaml
     * conf.getLong("my.prop") // parse my.prop to long
     * }</pre>
     *
     * @return ConfigReader
     */
    public static ConfigReader readConfig() {
        return new ConfigReader();
    }
}
