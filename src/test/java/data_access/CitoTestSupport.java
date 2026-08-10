package data_access;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

/** Test-only construction helpers for the Cito outer adapter. */
final class CitoTestSupport {
    private static final MediaType JSON =
            MediaType.parse("application/json; charset=utf-8");

    private CitoTestSupport() {
    }

    static CitoConfig config(String key, String baseUrl) {
        try {
            final Constructor<CitoConfig> constructor =
                    CitoConfig.class.getDeclaredConstructor(
                            String.class, String.class);
            constructor.setAccessible(true);
            return constructor.newInstance(key, baseUrl);
        }
        catch (NoSuchMethodException
               | InstantiationException
               | IllegalAccessException
               | InvocationTargetException exception) {
            throw new AssertionError("Could not construct test config.", exception);
        }
    }

    static CitoApiClient client(Path cacheDirectory,
                                Responder responder) {
        final OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> responder.respond(chain.request()))
                .build();
        return new CitoApiClient(
                config("test-key", "https://cito.test/api/v1"),
                httpClient,
                cacheDirectory);
    }

    static Response response(Request request, int code, String body) {
        final Response.Builder response = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(code)
                .message("test response");
        if (body != null) {
            response.body(ResponseBody.create(body, JSON));
        }
        return response.build();
    }

    @FunctionalInterface
    interface Responder {
        Response respond(Request request) throws IOException;
    }
}
