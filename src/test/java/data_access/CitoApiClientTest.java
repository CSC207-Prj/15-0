package data_access;

import okhttp3.OkHttpClient;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CitoApiClientTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void successfulGetNormalizesEndpointSendsHeadersAndUsesFreshCache()
            throws IOException {
        final AtomicInteger calls = new AtomicInteger();
        final AtomicReference<String> path = new AtomicReference<>();
        final AtomicReference<String> key = new AtomicReference<>();
        final CitoApiClient client = CitoTestSupport.client(
                temporaryDirectory,
                request -> {
                    calls.incrementAndGet();
                    path.set(request.url().encodedPath());
                    key.set(request.header("x-api-key"));
                    assertEquals("application/json", request.header("Accept"));
                    return CitoTestSupport.response(
                            request, 200, "{\"value\":7}");
                });

        assertEquals(7, client.get("fighters").getInt("value"));
        assertEquals(7, client.get("fighters").getInt("value"));
        assertEquals(1, calls.get());
        assertEquals("/api/v1/fighters", path.get());
        assertEquals("test-key", key.get());
        assertEquals(1, Files.list(temporaryDirectory).count());
    }

    @Test
    void staleCacheIsUsedForHttpFailure() throws IOException {
        final AtomicInteger calls = new AtomicInteger();
        final CitoApiClient client = CitoTestSupport.client(
                temporaryDirectory,
                request -> {
                    final int call = calls.incrementAndGet();
                    if (call == 1) {
                        return CitoTestSupport.response(
                                request, 200, "{\"cached\":true}");
                    }
                    return CitoTestSupport.response(request, 503, "offline");
                });

        assertTrue(client.get("/rankings").getBoolean("cached"));
        final Path cacheFile = Files.list(temporaryDirectory)
                .findFirst().orElseThrow();
        Files.setLastModifiedTime(cacheFile, FileTime.from(
                Instant.now().minus(25, ChronoUnit.HOURS)));

        assertTrue(client.get("/rankings").getBoolean("cached"));
        assertEquals(2, calls.get());
    }

    @Test
    void staleCacheIsUsedForIoFailure() throws IOException {
        final AtomicInteger calls = new AtomicInteger();
        final CitoApiClient client = CitoTestSupport.client(
                temporaryDirectory,
                request -> {
                    if (calls.incrementAndGet() == 1) {
                        return CitoTestSupport.response(
                                request, 200, "{\"source\":\"cache\"}");
                    }
                    throw new IOException("connection reset");
                });

        client.get("stats");
        final Path cacheFile = Files.list(temporaryDirectory)
                .findFirst().orElseThrow();
        Files.setLastModifiedTime(cacheFile, FileTime.from(
                Instant.now().minus(2, ChronoUnit.DAYS)));

        assertEquals("cache", client.get("stats").getString("source"));
    }

    @Test
    void failuresWithoutCacheHaveUsefulExceptions() {
        final CitoApiClient httpFailure = CitoTestSupport.client(
                temporaryDirectory.resolve("http"),
                request -> CitoTestSupport.response(request, 401, "denied"));
        final CitoApiException statusException = assertThrows(
                CitoApiException.class,
                () -> httpFailure.get("fighters"));
        assertTrue(statusException.getMessage().contains("HTTP 401"));
        assertTrue(statusException.getMessage().contains("/fighters"));

        final CitoApiClient ioFailure = CitoTestSupport.client(
                temporaryDirectory.resolve("io"),
                request -> {
                    throw new IOException("offline");
                });
        final CitoApiException ioException = assertThrows(
                CitoApiException.class,
                () -> ioFailure.get("fighters"));
        assertTrue(ioException.getMessage().contains("Could not reach Cito"));
        assertNotNull(ioException.getCause());
    }

    @Test
    void invalidOrMissingJsonBodyIsRejected() {
        final CitoApiClient invalid = CitoTestSupport.client(
                temporaryDirectory.resolve("invalid"),
                request -> CitoTestSupport.response(request, 200, "not json"));
        final CitoApiException invalidException = assertThrows(
                CitoApiException.class,
                () -> invalid.get("bad"));
        assertTrue(invalidException.getMessage().contains("invalid JSON"));
        assertNotNull(invalidException.getCause());

        final CitoApiClient empty = CitoTestSupport.client(
                temporaryDirectory.resolve("empty"),
                request -> CitoTestSupport.response(request, 200, ""));
        assertThrows(CitoApiException.class, () -> empty.get("empty"));
    }

    @Test
    void unconfiguredClientRejectsRequestsBeforeCallingHttp() {
        final CitoApiClient client = new CitoApiClient(
                CitoTestSupport.config("  ", "https://cito.test"),
                new OkHttpClient(),
                temporaryDirectory);

        assertFalse(client.isConfigured());
        final CitoApiException exception = assertThrows(
                CitoApiException.class,
                () -> client.get("fighters"));
        assertEquals("Cito API key is not configured.", exception.getMessage());
    }

    @Test
    void cacheWriteFailureDoesNotDiscardSuccessfulResponse()
            throws IOException {
        final Path fileInsteadOfDirectory =
                temporaryDirectory.resolve("cache-file");
        Files.writeString(fileInsteadOfDirectory, "occupied");
        final CitoApiClient client = CitoTestSupport.client(
                fileInsteadOfDirectory,
                request -> CitoTestSupport.response(
                        request, 200, "{\"ok\":true}"));

        final JSONObject response = client.get("fighters");
        assertTrue(response.getBoolean("ok"));
    }

    @Test
    void publicConstructorReflectsLoadedConfiguration() {
        final CitoApiClient client = new CitoApiClient(
                CitoTestSupport.config("key", "https://cito.test///"));

        assertTrue(client.isConfigured());
    }
}
