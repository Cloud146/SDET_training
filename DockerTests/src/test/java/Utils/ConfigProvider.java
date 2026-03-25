package Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProvider {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigProvider.class.getClassLoader().getResourceAsStream("test.properties")) {
            if (input == null) System.err.println("WARN: test.properties not found!");
            else properties.load(input);
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

//    public static String get(String key) {
//        return properties.getProperty(key);
//    }

    public static String get(String key) {
        String envKey = key.replace(".", "_").toUpperCase();
        String envValue = System.getenv(envKey);

        System.out.println("Config check: Key='" + key + "', EnvKey='" + envKey + "', EnvValue='" + envValue + "'");

        if (envValue != null) {
            return envValue;
        }
        return properties.getProperty(key);
    }

    public static String getHostUrl() { return get("host.url"); }
    public static String getDbUrl() { return get("db.url"); }
    public static String getDbUser() { return get("db.user"); }
    public static String getDbPassword() { return get("db.password"); }
}