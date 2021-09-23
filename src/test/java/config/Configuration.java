package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

    private static Properties properties = null;

    static {
        properties = new Properties();
        InputStream in
                = Configuration.class.getClassLoader().getResourceAsStream("test.properties");

        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        };
    }

    // METADATA CONFIGURATIONS
    public static String METADATA_HOSTNAME
            = properties.getProperty("metadata.server.hostname", "metadata-server");

    public static int METADATA_PORT
            = Integer.parseInt(properties.getProperty("metadata.server.port", "9090"));

    public static int METADATA_TIMEOUT
            = Integer.parseInt(properties.getProperty("metadata.server.timeout", "3000"));

}
