package data_access;

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

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Small HTTP client for Cito's REST API.
 *
 * <p>GET responses are cached locally for 24 hours. If Cito is temporarily
 * unavailable, a stale cached response is used when possible.
 */
public final class CitoApiClient {

    private static final long CACHE_TTL_HOURS = 24;
    private static final int CONNECT_TIMEOUT_SECONDS = 10;
    private static final int READ_TIMEOUT_SECONDS = 20;
    private static final int UNSIGNED_BYTE_MASK = 0xff;

    private final CitoConfig config;
    private final OkHttpClient httpClient;
    private final Path cacheDirectory;

    public CitoApiClient(CitoConfig config) {
        this(
                config,
                new OkHttpClient.Builder()
                        .connectTimeout(
                                CONNECT_TIMEOUT_SECONDS,
                                TimeUnit.SECONDS)
                        .readTimeout(
                                READ_TIMEOUT_SECONDS,
                                TimeUnit.SECONDS)
                        .build(),
                Paths.get(".cito-cache"));
    }

    CitoApiClient(
            CitoConfig config,
            OkHttpClient httpClient,
            Path cacheDirectory) {

        this.config = config;
        this.httpClient = httpClient;
        this.cacheDirectory = cacheDirectory;
    }

    public boolean isConfigured() {
        return config.isConfigured();
    }

    /**
     * Performs a GET request against a Cito API endpoint.
     *
     * <p>A fresh cached response is preferred when available. If the
     * network request fails, a stale cached response is used when possible.
     *
     * @param endpoint API endpoint to request
     * @return parsed JSON response
     * @throws CitoApiException if the API is not configured or no usable
     *         network or cached response is available
     */
    public JSONObject get(String endpoint) {
        if (!isConfigured()) {
            throw new CitoApiException(
                    "Cito API key is not configured.");
        }

        final String normalizedEndpoint;

        if (endpoint.startsWith("/")) {
            normalizedEndpoint = endpoint;
        }
        else {
            normalizedEndpoint = "/" + endpoint;
        }

        final Path cacheFile =
                cacheFile(normalizedEndpoint);

        final String freshCache =
                readCache(cacheFile, false);

        final JSONObject result;

        if (freshCache != null) {
            result =
                    parse(
                            freshCache,
                            normalizedEndpoint);
        }
        else {
            final Request request =
                    new Request.Builder()
                            .url(
                                    config.getBaseUrl()
                                            + normalizedEndpoint)
                            .header(
                                    "x-api-key",
                                    config.getApiKey())
                            .header(
                                    "Accept",
                                    "application/json")
                            .get()
                            .build();

            result =
                    executeRequest(
                            request,
                            cacheFile,
                            normalizedEndpoint);
        }

        return result;
    }

    private JSONObject executeRequest(
            Request request,
            Path cacheFile,
            String endpoint) {

        JSONObject result;

        try (Response response =
                     httpClient.newCall(request).execute()) {

            final ResponseBody body =
                    response.body();

            final String bodyText;

            if (body == null) {
                bodyText = "";
            }
            else {
                bodyText = body.string();
            }

            if (!response.isSuccessful()) {
                result =
                        recoverFromHttpFailure(
                                cacheFile,
                                endpoint,
                                response.code());
            }
            else {
                writeCache(
                        cacheFile,
                        bodyText);

                result =
                        parse(
                                bodyText,
                                endpoint);
            }
        }
        catch (IOException exception) {
            result =
                    recoverFromIoFailure(
                            cacheFile,
                            endpoint,
                            exception);
        }

        return result;
    }

    private JSONObject recoverFromHttpFailure(
            Path cacheFile,
            String endpoint,
            int responseCode) {

        final String staleCache =
                readCache(cacheFile, true);

        final JSONObject result;

        if (staleCache != null) {
            result =
                    parse(
                            staleCache,
                            endpoint);
        }
        else {
            throw new CitoApiException(
                    "Cito request failed with HTTP "
                            + responseCode
                            + " for "
                            + endpoint);
        }

        return result;
    }

    private JSONObject recoverFromIoFailure(
            Path cacheFile,
            String endpoint,
            IOException exception) {

        final String staleCache =
                readCache(cacheFile, true);

        final JSONObject result;

        if (staleCache != null) {
            result =
                    parse(
                            staleCache,
                            endpoint);
        }
        else {
            throw new CitoApiException(
                    "Could not reach Cito for "
                            + endpoint,
                    exception);
        }

        return result;
    }

    private JSONObject parse(
            String json,
            String endpoint) {

        final JSONObject result;

        try {
            result = new JSONObject(json);
        }
        catch (JSONException exception) {
            throw new CitoApiException(
                    "Cito returned invalid JSON for "
                            + endpoint,
                    exception);
        }

        return result;
    }

    private Path cacheFile(String endpoint) {
        return cacheDirectory.resolve(
                hash(endpoint) + ".json");
    }

    private String readCache(
            Path path,
            boolean allowStale) {

        String cacheContent = null;

        if (Files.exists(path)) {
            try {
                boolean shouldRead = true;

                if (!allowStale) {
                    final Instant modified =
                            Files.getLastModifiedTime(path)
                                    .toInstant();

                    final Duration age =
                            Duration.between(
                                    modified,
                                    Instant.now());

                    if (age.toHours()
                            >= CACHE_TTL_HOURS) {

                        shouldRead = false;
                    }
                }

                if (shouldRead) {
                    cacheContent =
                            Files.readString(
                                    path,
                                    StandardCharsets.UTF_8);
                }
            }
            catch (IOException exception) {
                cacheContent = null;
            }
        }

        return cacheContent;
    }

    private void writeCache(
            Path path,
            String body) {

        try {
            Files.createDirectories(
                    cacheDirectory);

            Files.writeString(
                    path,
                    body,
                    StandardCharsets.UTF_8);
        }
        catch (IOException ignored) {
            // Caching is an optimization. A successful API response
            // should still be usable if the local cache cannot be written.
        }
    }

    private String hash(String value) {
        try {
            final MessageDigest digest =
                    MessageDigest.getInstance(
                            "SHA-256");

            final byte[] bytes =
                    digest.digest(
                            value.getBytes(
                                    StandardCharsets.UTF_8));

            final StringBuilder builder =
                    new StringBuilder();

            for (byte byteValue : bytes) {
                builder.append(
                        String.format(
                                "%02x",
                                byteValue
                                        & UNSIGNED_BYTE_MASK));
            }

            return builder.toString();
        }
        catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "SHA-256 is unavailable.",
                    exception);
        }
    }
}
