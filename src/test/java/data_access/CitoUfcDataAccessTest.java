package data_access;

import entity.Attribute;
import entity.Division;
import entity.RealFighter;
import entity.UfcEra;
import entity.WeightClass;
import okhttp3.Request;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import use_case.fighter_creation.FighterDataAccessInterface;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CitoUfcDataAccessTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void unconfiguredSourceUsesLocalFightersAndDemoRankings() {
        final CountingFighterSource fallback = new CountingFighterSource(
                fallbackRoster());
        final CitoApiClient unconfigured = new CitoApiClient(
                CitoTestSupport.config(null, "https://cito.test"),
                new okhttp3.OkHttpClient(), temporaryDirectory);
        final CitoUfcDataAccess dataAccess =
                new CitoUfcDataAccess(unconfigured, fallback);

        final List<RealFighter> first = dataAccess.getFighters();
        final List<RealFighter> second = dataAccess.getFighters();
        assertEquals(fallbackRoster().size(), first.size());
        assertNotSame(first, second);
        first.clear();
        assertEquals(fallbackRoster().size(), second.size());
        assertEquals(1, fallback.calls);
        assertSame(second.get(0), dataAccess.getFighterDetails(second.get(0)));
        assertNull(dataAccess.getFighterDetails(null));
        assertFalse(dataAccess.isUsingApi());

        final Division division = dataAccess.getDivision(WeightClass.FLYWEIGHT);
        assertEquals(15, division.getRankedFighters().size());
        assertSame(division, dataAccess.getDivision(WeightClass.FLYWEIGHT));
    }

    @Test
    void directoryDeduplicatesSkipsBadRowsMergesEraFallbacksAndCaches() {
        final AtomicInteger directoryCalls = new AtomicInteger();
        final JSONArray rows = new JSONArray()
                .put(directoryRow("API Modern One", "api-one", "Modern", 1))
                .put(directoryRow("api modern one", "duplicate", "Modern", 2))
                .put(directoryRow("API Modern Two", "api-two", "Modern", 2))
                .put(new JSONObject().put("name", "Unsupported")
                        .put("division", "Strawweight"))
                .put("not an object");
        final CitoApiClient client = CitoTestSupport.client(
                temporaryDirectory,
                request -> {
                    directoryCalls.incrementAndGet();
                    return CitoTestSupport.response(request, 200,
                            new JSONObject().put("data", rows).toString());
                });
        final CountingFighterSource fallback = new CountingFighterSource(
                fallbackRoster());
        final CitoUfcDataAccess dataAccess =
                new CitoUfcDataAccess(client, fallback);

        final List<RealFighter> fighters = dataAccess.getFighters();
        assertEquals(6, fighters.size());
        assertEquals(2, countEra(fighters, UfcEra.MODERN));
        assertEquals(2, countEra(fighters, UfcEra.EARLY_UFC));
        assertEquals(2, countEra(fighters, UfcEra.ZUFFA_ERA));
        assertEquals(1, directoryCalls.get());
        assertEquals(1, fallback.calls);

        fighters.clear();
        assertEquals(6, dataAccess.getFighters().size());
        assertEquals(1, directoryCalls.get());
    }

    @Test
    void fighterDetailsMergeDirectoryProfileAndStatsAndUseExactSlug() {
        final AtomicInteger profileCalls = new AtomicInteger();
        final List<String> requestedPaths = new ArrayList<>();
        final JSONObject directory = directoryRow(
                "Hydrated Fighter", "cito-exact-slug", "Modern", 4)
                .put("record", "1-1");
        final CitoApiClient client = CitoTestSupport.client(
                temporaryDirectory,
                request -> {
                    requestedPaths.add(request.url().encodedPath());
                    final String path = request.url().encodedPath();
                    if (path.endsWith("/ufc/fighters")) {
                        return json(request, new JSONObject().put(
                                "fighters", new JSONArray().put(directory)));
                    }
                    profileCalls.incrementAndGet();
                    if (path.endsWith("/stats")) {
                        return json(request, new JSONObject().put("data",
                                new JSONObject()
                                        .put("record", "22-4-1")
                                        .put("strAcc", 0.6)
                                        .put("cardio", 95)));
                    }
                    return json(request, new JSONObject().put("data",
                            new JSONObject().put("height", "6'0")
                                    .put("reach", 76)));
                });
        final CitoUfcDataAccess dataAccess = new CitoUfcDataAccess(
                client, new CountingFighterSource(fallbackRoster()));

        final RealFighter basic = dataAccess.getFighters().get(0);
        final RealFighter detailed = dataAccess.getFighterDetails(basic);

        assertEquals("22-4-1", detailed.getProfessionalRecord());
        assertEquals(4, detailed.getRank());
        assertTrue(detailed.getAttributes().get(Attribute.CARDIO) >= 95.0);
        assertTrue(requestedPaths.stream().anyMatch(
                path -> path.endsWith("/cito-exact-slug")));
        assertTrue(requestedPaths.stream().anyMatch(
                path -> path.endsWith("/cito-exact-slug/stats")));
        assertSame(detailed, dataAccess.getFighterDetails(basic));
        assertEquals(2, profileCalls.get());
    }

    @Test
    void detailFailureReturnsOriginalAndNameIsSlugifiedWhenDirectoryMissing() {
        final List<String> paths = new ArrayList<>();
        final CitoApiClient client = CitoTestSupport.client(
                temporaryDirectory,
                request -> {
                    paths.add(request.url().encodedPath());
                    return CitoTestSupport.response(request, 500, "offline");
                });
        final CitoUfcDataAccess dataAccess = new CitoUfcDataAccess(
                client, new CountingFighterSource(fallbackRoster()));
        final RealFighter original = fighter(
                "José Test, Jr.", WeightClass.BANTAMWEIGHT,
                3, UfcEra.MODERN);

        assertSame(original, dataAccess.getFighterDetails(original));
        assertTrue(paths.get(0).endsWith("/jos-test-jr"));
    }

    @Test
    void completeRankingsHydrateAllFifteenAndCacheDivision() {
        final JSONArray rows = new JSONArray()
                .put(new JSONObject().put("rank", "C")
                        .put("name", "Champion"))
                .put("not an object")
                .put(new JSONObject().put("rank", 16)
                        .put("name", "Out of Range"));
        for (int rank = 1; rank <= 15; rank++) {
            final JSONObject row = new JSONObject()
                    .put("fighter", new JSONObject()
                            .put("name", "Ranked " + rank)
                            .put("slug", "ranked-" + rank));
            if (rank != 1) {
                row.put("position", rank);
            }
            rows.put(row);
        }
        rows.put(new JSONObject().put("rank", 1)
                .put("name", "Duplicate Rank"));

        final AtomicInteger calls = new AtomicInteger();
        final CitoApiClient client = CitoTestSupport.client(
                temporaryDirectory,
                request -> {
                    calls.incrementAndGet();
                    final String path = request.url().encodedPath();
                    if (path.endsWith("/ufc/rankings/lightweight")) {
                        return json(request,
                                new JSONObject().put("rankings", rows));
                    }
                    if (path.endsWith("/stats")) {
                        return json(request, new JSONObject()
                                .put("record", "10-2")
                                .put("strikingAccuracy", 0.5));
                    }
                    return json(request, new JSONObject().put("data",
                            new JSONObject().put("reach", 72)));
                });
        final CitoUfcDataAccess dataAccess = new CitoUfcDataAccess(
                client, new CountingFighterSource(fallbackRoster()));

        final Division division = dataAccess.getDivision(WeightClass.LIGHTWEIGHT);
        assertEquals(15, division.getRankedFighters().size());
        assertEquals("Ranked 1", division.getRankedFighters().get(0).getName());
        assertEquals(15, division.getRankedFighters().get(14).getRank());
        assertEquals("10-2", division.getRankedFighters().get(0)
                .getProfessionalRecord());
        assertEquals(31, calls.get());
        assertSame(division, dataAccess.getDivision(WeightClass.LIGHTWEIGHT));
        assertEquals(31, calls.get());
    }

    @Test
    void apiErrorsEmptyDirectoryAndIncompleteRankingsUseFallbacks() {
        final CountingFighterSource failureFallback =
                new CountingFighterSource(fallbackRoster());
        final CitoUfcDataAccess failing = new CitoUfcDataAccess(
                CitoTestSupport.client(temporaryDirectory.resolve("failure"),
                        request -> CitoTestSupport.response(
                                request, 500, "offline")),
                failureFallback);
        assertEquals(fallbackRoster().size(), failing.getFighters().size());
        assertEquals(1, failureFallback.calls);

        final CountingFighterSource emptyFallback =
                new CountingFighterSource(fallbackRoster());
        final CitoUfcDataAccess empty = new CitoUfcDataAccess(
                CitoTestSupport.client(temporaryDirectory.resolve("empty"),
                        request -> json(request,
                                new JSONObject().put("data", new JSONArray()))),
                emptyFallback);
        assertEquals(fallbackRoster().size(), empty.getFighters().size());
        assertEquals(1, emptyFallback.calls);

        final Division fallbackDivision = empty.getDivision(
                WeightClass.HEAVYWEIGHT);
        assertEquals(15, fallbackDivision.getRankedFighters().size());
        assertSame(fallbackDivision,
                empty.getDivision(WeightClass.HEAVYWEIGHT));
    }

    @Test
    void directoryContinuesAfterFullPageAndStopsOnEmptyPage() {
        final JSONArray fullPage = new JSONArray();
        for (int index = 0; index < 50; index++) {
            fullPage.put(directoryRow(
                    "Paged Fighter " + index,
                    "paged-" + index,
                    "Modern",
                    index % 15 + 1));
        }
        final AtomicInteger calls = new AtomicInteger();
        final CitoApiClient client = CitoTestSupport.client(
                temporaryDirectory,
                request -> {
                    final int call = calls.incrementAndGet();
                    return json(request, new JSONObject().put("data",
                            call == 1 ? fullPage : new JSONArray()));
                });
        final CitoUfcDataAccess dataAccess = new CitoUfcDataAccess(
                client, new CountingFighterSource(fallbackRoster()));

        assertEquals(54, dataAccess.getFighters().size());
        assertEquals(2, calls.get());
    }

    private static okhttp3.Response json(Request request, JSONObject body) {
        return CitoTestSupport.response(request, 200, body.toString());
    }

    private static JSONObject directoryRow(String name,
                                           String slug,
                                           String era,
                                           int rank) {
        return new JSONObject()
                .put("name", name)
                .put("slug", slug)
                .put("division", "Lightweight")
                .put("era", era)
                .put("rank", rank)
                .put("record", "10-1");
    }

    private static int countEra(List<RealFighter> fighters, UfcEra era) {
        int count = 0;
        for (RealFighter fighter : fighters) {
            if (fighter.getEra() == era) {
                count++;
            }
        }
        return count;
    }

    private static List<RealFighter> fallbackRoster() {
        return List.of(
                fighter("Early One", WeightClass.WELTERWEIGHT,
                        0, UfcEra.EARLY_UFC),
                fighter("Early Two", WeightClass.WELTERWEIGHT,
                        0, UfcEra.EARLY_UFC),
                fighter("Zuffa One", WeightClass.MIDDLEWEIGHT,
                        0, UfcEra.ZUFFA_ERA),
                fighter("Zuffa Two", WeightClass.MIDDLEWEIGHT,
                        0, UfcEra.ZUFFA_ERA),
                fighter("Modern Local One", WeightClass.LIGHTWEIGHT,
                        0, UfcEra.MODERN),
                fighter("Modern Local Two", WeightClass.LIGHTWEIGHT,
                        0, UfcEra.MODERN));
    }

    private static RealFighter fighter(String name,
                                       WeightClass weightClass,
                                       int rank,
                                       UfcEra era) {
        final Map<Attribute, Double> attributes =
                new EnumMap<>(Attribute.class);
        for (Attribute attribute : Attribute.values()) {
            attributes.put(attribute, 75.0);
        }
        return new RealFighter(
                name, weightClass, rank, era, "10-0", attributes);
    }

    private static final class CountingFighterSource
            implements FighterDataAccessInterface {
        private final List<RealFighter> fighters;
        private int calls;

        private CountingFighterSource(List<RealFighter> fighters) {
            this.fighters = fighters;
        }

        @Override
        public List<RealFighter> getFighters() {
            calls++;
            return new ArrayList<>(fighters);
        }
    }
}
