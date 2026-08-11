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
 * <p>Configuration is resolved from environment variables, JVM system
 * properties, and finally {@code cito.local.properties}.
 */
public final class CitoConfig {

    public static final String DEFAULT_BASE_URL =
            "https://api.citoapi.com/api/v1";

    private static final String PLACEHOLDER =
            "PASTE_YOUR_CITO_API_KEY_HERE";

    private final String apiKey;
    private final String baseUrl;

    private CitoConfig(
            String apiKey,
            String baseUrl) {

        this.apiKey = clean(apiKey);

        final String cleanedBase =
                clean(baseUrl);

        if (cleanedBase == null) {
            this.baseUrl = DEFAULT_BASE_URL;
        }
        else {
            this.baseUrl =
                    stripTrailingSlash(cleanedBase);
        }
    }

    /**
     * Loads the available Cito API configuration.
     *
     * <p>Environment variables are checked first, followed by JVM system
     * properties and values from {@code cito.local.properties}.
     *
     * @return resolved Cito configuration
     */
    public static CitoConfig load() {
        final Properties local =
                loadLocalProperties();

        final String apiKey =
                firstNonBlank(
                        System.getenv("CITO_API_KEY"),
                        System.getProperty("cito.api.key"),
                        local.getProperty("cito.api.key"));

        final String baseUrl =
                firstNonBlank(
                        System.getenv("CITO_API_BASE_URL"),
                        System.getProperty("cito.api.baseUrl"),
                        local.getProperty("cito.api.baseUrl"),
                        DEFAULT_BASE_URL);

        return new CitoConfig(
                apiKey,
                baseUrl);
    }

    /**
     * Reports whether a usable Cito API key is configured.
     *
     * @return {@code true} when a non-placeholder API key is available
     */
    public boolean isConfigured() {
        return apiKey != null
                && !PLACEHOLDER.equalsIgnoreCase(apiKey);
    }

    /**
     * Returns the configured Cito API key.
     *
     * @return API key, or {@code null} when none is configured
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Returns the configured Cito API base URL.
     *
     * @return normalized API base URL
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    private static Properties loadLocalProperties() {
        final Properties properties =
                new Properties();

        final Path path =
                Paths.get("cito.local.properties");

        if (Files.exists(path)) {
            try (InputStream input =
                         Files.newInputStream(path)) {

                properties.load(input);
            }
            catch (IOException exception) {
                throw new IllegalStateException(
                        "Could not read cito.local.properties.",
                        exception);
            }
        }

        return properties;
    }

    private static String firstNonBlank(
            String... values) {

        String result = null;

        for (String value : values) {
            final String cleaned =
                    clean(value);

            if (result == null
                    && cleaned != null) {

                result = cleaned;
            }
        }

        return result;
    }

    private static String clean(String value) {
        String result = null;

        if (value != null) {
            final String trimmed =
                    value.trim();

            if (!trimmed.isEmpty()) {
                result = trimmed;
            }
        }

        return result;
    }

    private static String stripTrailingSlash(
            String value) {

        String result = value;

        while (result.endsWith("/")) {
            result =
                    result.substring(
                            0,
                            result.length() - 1);
        }

        return result;
    }
}
