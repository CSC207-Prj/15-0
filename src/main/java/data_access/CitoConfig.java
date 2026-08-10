package data_access;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Loads Cito API configuration without hard-coding secrets in source control.
 *
 * Resolution order:
 * 1. CITO_API_KEY / CITO_API_BASE_URL environment variables.
 * 2. -Dcito.api.key / -Dcito.api.baseUrl JVM system properties.
 * 3. cito.local.properties in the repository working directory.
 */
public final class CitoConfig {
    public static final String DEFAULT_BASE_URL =
            "https://api.citoapi.com/api/v1";

    private static final String PLACEHOLDER =
            "PASTE_YOUR_CITO_API_KEY_HERE";

    private final String apiKey;
    private final String baseUrl;

    private CitoConfig(String apiKey, String baseUrl) {
        this.apiKey = clean(apiKey);
        final String cleanedBase = clean(baseUrl);
        this.baseUrl = cleanedBase == null
                ? DEFAULT_BASE_URL
                : stripTrailingSlash(cleanedBase);
    }

    public static CitoConfig load() {
        final Properties local = loadLocalProperties();

        final String apiKey = firstNonBlank(
                System.getenv("CITO_API_KEY"),
                System.getProperty("cito.api.key"),
                local.getProperty("cito.api.key"));

        final String baseUrl = firstNonBlank(
                System.getenv("CITO_API_BASE_URL"),
                System.getProperty("cito.api.baseUrl"),
                local.getProperty("cito.api.baseUrl"),
                DEFAULT_BASE_URL);

        return new CitoConfig(apiKey, baseUrl);
    }

    public boolean isConfigured() {
        return apiKey != null
                && !PLACEHOLDER.equalsIgnoreCase(apiKey);
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    private static Properties loadLocalProperties() {
        final Properties properties = new Properties();
        final Path path = Paths.get("cito.local.properties");

        if (!Files.exists(path)) {
            return properties;
        }

        try (InputStream input = Files.newInputStream(path)) {
            properties.load(input);
        }
        catch (IOException exception) {
            throw new IllegalStateException(
                    "Could not read cito.local.properties.", exception);
        }

        return properties;
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            final String cleaned = clean(value);
            if (cleaned != null) {
                return cleaned;
            }
        }
        return null;
    }

    private static String clean(String value) {
        if (value == null) {
            return null;
        }
        final String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static String stripTrailingSlash(String value) {
        String result = value;
        while (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
