package data_access;

import entity.Division;
import entity.RealFighter;
import entity.UfcEra;
import entity.WeightClass;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.fighter_creation.FighterDataAccessInterface;
import use_case.fighter_creation.FighterDetailsDataAccessInterface;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Cito-backed UFC data source used by the stories that need real UFC data.
 *
 * US1 / US2 / US6 use getFighters() through FighterDataAccessInterface.
 * US3 uses getDivision() to create the ranked gauntlet.
 *
 * Network failures or an absent API key fall back to the project's existing
 * local data so the application remains runnable offline.
 */
public final class CitoUfcDataAccess
        implements FighterDataAccessInterface, FighterDetailsDataAccessInterface {

    private static final int FIGHTER_PAGE_LIMIT = 50;
    private static final int MAX_FIGHTER_PAGES = 2;

    private final CitoApiClient client;
    private final FighterDataAccessInterface fallbackFighters;

    private List<RealFighter> fighterCache;
    private final Map<WeightClass, Division> divisionCache =
            new EnumMap<>(WeightClass.class);
    private final Map<String, JSONObject> directoryJsonByName =
            new LinkedHashMap<>();
    private final Map<String, String> slugByName =
            new LinkedHashMap<>();
    private final Map<String, RealFighter> detailCache =
            new LinkedHashMap<>();

    public CitoUfcDataAccess(CitoApiClient client,
                             FighterDataAccessInterface fallbackFighters) {
        this.client = client;
        this.fallbackFighters = fallbackFighters;
    }

    @Override
    public synchronized List<RealFighter> getFighters() {
        if (fighterCache != null) {
            return new ArrayList<>(fighterCache);
        }

        if (!client.isConfigured()) {
            fighterCache = fallbackFighters.getFighters();
            return new ArrayList<>(fighterCache);
        }

        try {
            final List<RealFighter> apiFighters =
                    loadFighterDirectory();

            if (apiFighters.isEmpty()) {
                fighterCache = fallbackFighters.getFighters();
            }
            else {
                fighterCache = mergeWithRequiredEraFallbacks(
                        apiFighters,
                        fallbackFighters.getFighters());
            }
        }
        catch (RuntimeException exception) {
            fighterCache = fallbackFighters.getFighters();
        }

        return new ArrayList<>(fighterCache);
    }

    @Override
    public synchronized RealFighter getFighterDetails(
            RealFighter fighter) {
        if (fighter == null || !client.isConfigured()) {
            return fighter;
        }

        final String key = normalizeName(fighter.getName());
        final RealFighter cached = detailCache.get(key);
        if (cached != null) {
            return cached;
        }

        String slug = slugByName.get(key);
        if (slug == null || slug.isBlank()) {
            slug = slugify(fighter.getName());
        }

        try {
            final JSONObject profileResponse =
                    client.get("/ufc/fighters/" + slug);
            final JSONObject statsResponse =
                    client.get("/ufc/fighters/" + slug + "/stats");

            final JSONObject profile =
                    primaryObject(profileResponse);
            final JSONObject stats =
                    primaryObject(statsResponse);
            final JSONObject directory =
                    directoryJsonByName.get(key);

            final JSONObject merged =
                    CitoFighterMapper.merge(
                            directory,
                            profile,
                            stats);

            final RealFighter detailed =
                    CitoFighterMapper.toFighter(
                            merged,
                            fighter.getWeightClass(),
                            fighter.getRank(),
                            fighter.getEra());

            detailCache.put(key, detailed);
            return detailed;
        }
        catch (RuntimeException exception) {
            return fighter;
        }
    }

    public synchronized Division getDivision(
            WeightClass weightClass) {
        final Division cached = divisionCache.get(weightClass);
        if (cached != null) {
            return cached;
        }

        if (!client.isConfigured()) {
            final Division fallback =
                    DemoRankingsFactory.createDivision(weightClass);
            divisionCache.put(weightClass, fallback);
            return fallback;
        }

        try {
            final Division division =
                    loadDivision(weightClass);
            divisionCache.put(weightClass, division);
            return division;
        }
        catch (RuntimeException exception) {
            final Division fallback =
                    DemoRankingsFactory.createDivision(weightClass);
            divisionCache.put(weightClass, fallback);
            return fallback;
        }
    }

    public boolean isUsingApi() {
        return client.isConfigured();
    }

    private List<RealFighter> loadFighterDirectory() {
        final Map<String, RealFighter> fighters =
                new LinkedHashMap<>();

        for (int page = 1; page <= MAX_FIGHTER_PAGES; page++) {
            final JSONObject response = client.get(
                    "/ufc/fighters?page="
                            + page
                            + "&limit="
                            + FIGHTER_PAGE_LIMIT);

            final JSONArray rows =
                    CitoFighterMapper.findPrimaryArray(response);

            if (rows == null || rows.length() == 0) {
                break;
            }

            for (int index = 0; index < rows.length(); index++) {
                final JSONObject row = rows.optJSONObject(index);
                if (row == null) {
                    continue;
                }

                try {
                    final RealFighter fighter =
                            CitoFighterMapper.toFighter(row);
                    final String key =
                            normalizeName(fighter.getName());
                    fighters.putIfAbsent(key, fighter);
                    directoryJsonByName.putIfAbsent(key, row);

                    final String slug =
                            CitoFighterMapper.slug(row);
                    if (slug != null && !slug.isBlank()) {
                        slugByName.putIfAbsent(key, slug);
                    }
                }
                catch (IllegalArgumentException ignored) {
                    // Skip unsupported divisions and incomplete directory rows.
                }
            }

            if (!hasMore(response)
                    || rows.length() < FIGHTER_PAGE_LIMIT) {
                break;
            }
        }

        return new ArrayList<>(fighters.values());
    }

    private Division loadDivision(WeightClass weightClass) {
        final String divisionSlug =
                divisionSlug(weightClass);

        final JSONObject response = client.get(
                "/ufc/rankings/" + divisionSlug);

        final JSONArray rows =
                CitoFighterMapper.findPrimaryArray(response);

        if (rows == null) {
            throw new CitoApiException(
                    "Cito rankings did not contain a fighter list.");
        }

        final Map<Integer, RealFighter> byRank =
                new LinkedHashMap<>();

        int inferredRank = 1;
        for (int index = 0; index < rows.length(); index++) {
            final JSONObject row = rows.optJSONObject(index);
            if (row == null) {
                continue;
            }

            int rank = CitoFighterMapper.rank(row);
            final Object explicitRank =
                    row.has("rank")
                            ? row.opt("rank")
                            : row.opt("position");

            if (rank == 0 && explicitRank != null
                    && explicitRank != JSONObject.NULL) {
                // Champion rows commonly use "C" instead of a numeric rank.
                continue;
            }

            if (rank == 0) {
                rank = inferredRank;
            }

            if (rank < 1 || rank > 15 || byRank.containsKey(rank)) {
                continue;
            }

            try {
                final RealFighter fighter =
                        CitoFighterMapper.toFighter(
                                row,
                                weightClass,
                                rank,
                                UfcEra.MODERN);
                byRank.put(rank, fighter);
                inferredRank = Math.max(
                        inferredRank,
                        rank + 1);
            }
            catch (IllegalArgumentException ignored) {
                // An incomplete ranking row should not break the whole app.
            }
        }

        if (byRank.size() != 15) {
            throw new CitoApiException(
                    "Cito did not return complete ranks 1 through 15 for "
                            + weightClass.getDisplayName());
        }

        final List<RealFighter> ranked =
                new ArrayList<>();
        for (int rank = 1; rank <= 15; rank++) {
            final RealFighter fighter = byRank.get(rank);
            if (fighter == null) {
                throw new CitoApiException(
                        "Cito ranking is missing rank " + rank);
            }
            ranked.add(fighter);
        }

        return new Division(weightClass, ranked);
    }

    private List<RealFighter> mergeWithRequiredEraFallbacks(
            List<RealFighter> apiFighters,
            List<RealFighter> fallback) {
        final Map<String, RealFighter> merged =
                new LinkedHashMap<>();

        for (RealFighter fighter : apiFighters) {
            merged.put(
                    normalizeName(fighter.getName()),
                    fighter);
        }

        final Map<UfcEra, Integer> eraCounts =
                new EnumMap<>(UfcEra.class);
        for (RealFighter fighter : apiFighters) {
            eraCounts.merge(fighter.getEra(), 1, Integer::sum);
        }

        for (RealFighter fighter : fallback) {
            final int count =
                    eraCounts.getOrDefault(fighter.getEra(), 0);

            // US2's reroll flow needs at least two eligible fighters
            // for any era the user can choose.
            if (count < 2) {
                merged.putIfAbsent(
                        normalizeName(fighter.getName()),
                        fighter);
                eraCounts.merge(
                        fighter.getEra(),
                        1,
                        Integer::sum);
            }
        }

        return new ArrayList<>(merged.values());
    }

    private boolean hasMore(JSONObject response) {
        final JSONObject pagination =
                response.optJSONObject("pagination");

        if (pagination != null) {
            return pagination.optBoolean("hasMore", false);
        }

        final JSONObject data =
                response.optJSONObject("data");
        if (data != null) {
            final JSONObject nestedPagination =
                    data.optJSONObject("pagination");
            if (nestedPagination != null) {
                return nestedPagination.optBoolean(
                        "hasMore",
                        false);
            }
        }

        return false;
    }

    private String divisionSlug(WeightClass weightClass) {
        switch (weightClass) {
            case FLYWEIGHT:
                return "flyweight";
            case BANTAMWEIGHT:
                return "bantamweight";
            case FEATHERWEIGHT:
                return "featherweight";
            case LIGHTWEIGHT:
                return "lightweight";
            case WELTERWEIGHT:
                return "welterweight";
            case MIDDLEWEIGHT:
                return "middleweight";
            case LIGHT_HEAVYWEIGHT:
                return "light-heavyweight";
            case HEAVYWEIGHT:
                return "heavyweight";
            default:
                throw new IllegalArgumentException(
                        "Unsupported weight class: " + weightClass);
        }
    }

    private JSONObject primaryObject(JSONObject response) {
        final Object data = response.opt("data");
        if (data instanceof JSONObject) {
            return (JSONObject) data;
        }
        return response;
    }

    private String slugify(String name) {
        return name.trim()
                .toLowerCase(java.util.Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }

    private String normalizeName(String name) {
        return name.trim().toLowerCase(java.util.Locale.ROOT);
    }
}
