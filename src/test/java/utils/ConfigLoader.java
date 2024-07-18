package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
public class ConfigLoader {
	
	private static Properties properties = new Properties();

    static {
        try (InputStream inputStream = ConfigLoader.class.getResourceAsStream("/config/config.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new RuntimeException("config.properties not found in the classpath");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getBaseUrl() {
        return properties.getProperty("baseUrl");
    }

    public static String[] getAlphabetsList() {
        return properties.getProperty("alphabetsList").split(",");
    }

    public static boolean isLoadDataRequired() {
        return Boolean.parseBoolean(properties.getProperty("loadDataRequired"));
    }

    public static int getThreadPoolSize() {
        return Integer.parseInt(properties.getProperty("threadPoolSize"));
    }

    public static int getTime() {
        return Integer.parseInt(properties.getProperty("time"));
    }

    public static String getBeginsWithParam() {
        return properties.getProperty("beginsWithParam");
    }

    public static String getRecipeUrlPrefix() {
        return properties.getProperty("recipeUrlPrefix");
    }

    public static String getReportFilePath() {
        return properties.getProperty("reportFilePath");
    }
}


