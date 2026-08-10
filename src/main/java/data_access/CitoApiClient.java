package data_access;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * Small HTTP client for Cito's REST API.
 *
 * GET responses are cached locally for 24 hours. If Cito is temporarily
 * unavailable, a stale cached response is used when possible.
 */
public final class CitoApiClient {
    private static final long CACHE_TTL_HOURS = 24;
    private static final int CONNECT_TIMEOUT_SECONDS = 10;
    private static final int READ_TIMEOUT_SECONDS = 20;

    private final CitoConfig config;
    private final OkHttpClient httpClient;
    private final Path cacheDirectory;

    public CitoApiClient(CitoConfig config) {
        this(
                config,
                new OkHttpClient.Builder()
                        .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                        .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                        .build(),
                Paths.get(".cito-cache"));
    }

    CitoApiClient(CitoConfig config,
                  OkHttpClient httpClient,
                  Path cacheDirectory) {
        this.config = config;
        this.httpClient = httpClient;
        this.cacheDirectory = cacheDirectory;
    }

    public boolean isConfigured() {
        return config.isConfigured();
    }

    public JSONObject get(String endpoint) {
        if (!isConfigured()) {
            throw new CitoApiException(
                    "Cito API key is not configured.");
        }

        final String normalizedEndpoint = endpoint.startsWith("/")
                ? endpoint
                : "/" + endpoint;
        final Path cacheFile = cacheFile(normalizedEndpoint);

        final String freshCache = readCache(cacheFile, false);
        if (freshCache != null) {
            return parse(freshCache, normalizedEndpoint);
        }

        final Request request = new Request.Builder()
                .url(config.getBaseUrl() + normalizedEndpoint)
                .header("x-api-key", config.getApiKey())
                .header("Accept", "application/json")
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            final ResponseBody body = response.body();
            final String bodyText = body == null ? "" : body.string();

            if (!response.isSuccessful()) {
                final String staleCache = readCache(cacheFile, true);
                if (staleCache != null) {
                    return parse(staleCache, normalizedEndpoint);
                }

                throw new CitoApiException(
                        "Cito request failed with HTTP "
                                + response.code()
                                + " for " + normalizedEndpoint);
            }

            writeCache(cacheFile, bodyText);
            return parse(bodyText, normalizedEndpoint);
        }
        catch (IOException exception) {
            final String staleCache = readCache(cacheFile, true);
            if (staleCache != null) {
                return parse(staleCache, normalizedEndpoint);
            }

            throw new CitoApiException(
                    "Could not reach Cito for "
                            + normalizedEndpoint,
                    exception);
        }
    }

    private JSONObject parse(String json, String endpoint) {
        try {
            return new JSONObject(json);
        }
        catch (RuntimeException exception) {
            throw new CitoApiException(
                    "Cito returned invalid JSON for " + endpoint,
                    exception);
        }
    }

    private Path cacheFile(String endpoint) {
        return cacheDirectory.resolve(hash(endpoint) + ".json");
    }

    private String readCache(Path path, boolean allowStale) {
        if (!Files.exists(path)) {
            return null;
        }

        try {
            if (!allowStale) {
                final Instant modified = Files.getLastModifiedTime(path)
                        .toInstant();
                final Duration age =
                        Duration.between(modified, Instant.now());
                if (age.toHours() >= CACHE_TTL_HOURS) {
                    return null;
                }
            }
            return Files.readString(path, StandardCharsets.UTF_8);
        }
        catch (IOException exception) {
            return null;
        }
    }

    private void writeCache(Path path, String body) {
        try {
            Files.createDirectories(cacheDirectory);
            Files.writeString(
                    path,
                    body,
                    StandardCharsets.UTF_8);
        }
        catch (IOException ignored) {
            // Caching is an optimization. A successful API response should
            // still be usable if the local cache cannot be written.
        }
    }

    private String hash(String value) {
        try {
            final MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");
            final byte[] bytes = digest.digest(
                    value.getBytes(StandardCharsets.UTF_8));
            final StringBuilder builder = new StringBuilder();
            for (byte byteValue : bytes) {
                builder.append(String.format("%02x", byteValue & 0xff));
            }
            return builder.toString();
        }
        catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "SHA-256 is unavailable.", exception);
        }
    }
}
