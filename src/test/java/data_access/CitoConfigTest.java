package data_access;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CitoConfigTest {
    private final String originalKey =
            System.getProperty("cito.api.key");
    private final String originalBaseUrl =
            System.getProperty("cito.api.baseUrl");

    @AfterEach
    void restoreProperties() {
        restore("cito.api.key", originalKey);
        restore("cito.api.baseUrl", originalBaseUrl);
    }

    @Test
    void constructorCleansValuesDefaultsUrlAndRejectsPlaceholder() {
        final CitoConfig configured = CitoTestSupport.config(
                "  secret  ", " https://example.test/api/// ");
        assertTrue(configured.isConfigured());
        assertEquals("secret", configured.getApiKey());
        assertEquals("https://example.test/api", configured.getBaseUrl());

        final CitoConfig missing = CitoTestSupport.config("   ", null);
        assertFalse(missing.isConfigured());
        assertNull(missing.getApiKey());
        assertEquals(CitoConfig.DEFAULT_BASE_URL, missing.getBaseUrl());

        assertFalse(CitoTestSupport.config(
                "paste_your_cito_api_key_here", "https://example.test")
                .isConfigured());
    }

    @Test
    void loadUsesSystemPropertiesWhenEnvironmentDoesNotOverrideThem() {
        if (hasText(System.getenv("CITO_API_KEY"))
                || hasText(System.getenv("CITO_API_BASE_URL"))) {
            return;
        }

        System.setProperty("cito.api.key", " system-key ");
        System.setProperty("cito.api.baseUrl", "https://system.test///");

        final CitoConfig config = CitoConfig.load();
        assertEquals("system-key", config.getApiKey());
        assertEquals("https://system.test", config.getBaseUrl());
    }

    private static boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private static void restore(String key, String value) {
        if (value == null) {
            System.clearProperty(key);
        }
        else {
            System.setProperty(key, value);
        }
    }
}
