package data_access;

import entity.Attribute;
import entity.RealFighter;
import entity.UfcEra;
import entity.WeightClass;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converts Cito UFC JSON into the project's RealFighter entity.
 *
 * Cito's fighter/profile/stat endpoints can expose the same concepts at
 * different nesting levels. The mapper therefore accepts common field-name
 * variants and falls back to deterministic gameplay ratings when a stat is
 * unavailable.
 */
public final class CitoFighterMapper {
    private static final Pattern NUMBER =
            Pattern.compile("-?\\d+(?:\\.\\d+)?");
    private static final Pattern FEET_INCHES =
            Pattern.compile("(\\d+)\\s*['′]\\s*(\\d+)");
    private static final Pattern YEAR =
            Pattern.compile("(19\\d{2}|20\\d{2})");

    private CitoFighterMapper() {
    }

    public static RealFighter toFighter(JSONObject source) {
        return toFighter(source, null, null, null);
    }

    public static RealFighter toFighter(JSONObject source,
                                        WeightClass forcedWeightClass,
                                        Integer forcedRank) {
        return toFighter(source, forcedWeightClass, forcedRank, null);
    }

    public static RealFighter toFighter(JSONObject source,
                                        WeightClass forcedWeightClass,
                                        Integer forcedRank,
                                        UfcEra forcedEra) {
        final String name = fighterName(source);
        if (name == null) {
            throw new IllegalArgumentException(
                    "Cito fighter is missing a name.");
        }

        final WeightClass weightClass = forcedWeightClass == null
                ? weightClass(source)
                : forcedWeightClass;
        if (weightClass == null) {
            throw new IllegalArgumentException(
                    "Unsupported or missing UFC weight class for " + name);
        }

        final int rank = forcedRank == null
                ? clampRank(integer(source,
                        "rank",
                        "ranking",
                        "position",
                        "rankNumber"))
                : clampRank(forcedRank);

        final String record = record(source);
        final UfcEra era = forcedEra == null
                ? era(source)
                : forcedEra;
        final Map<Attribute, Double> attributes =
                attributes(source, rank, record);

        return new RealFighter(
                name,
                weightClass,
                rank,
                era,
                record,
                attributes);
    }

    public static String fighterName(JSONObject source) {
        String result = directString(
                source,
                "fullName",
                "fighterName",
                "displayName");

        if (result != null && !result.isBlank()) {
            return result.trim();
        }

        final JSONObject fighterObject =
                directObject(source, "fighter", "athlete", "competitor");
        if (fighterObject != null) {
            final String nested = fighterName(fighterObject);
            if (nested != null && !nested.isBlank()) {
                return nested.trim();
            }
        }

        result = directString(source, "name");
        if (result != null && !result.isBlank()) {
            return result.trim();
        }

        final String first = string(
                source,
                "firstName",
                "givenName");
        final String last = string(
                source,
                "lastName",
                "familyName");

        if (first != null || last != null) {
            return ((first == null ? "" : first)
                    + " "
                    + (last == null ? "" : last)).trim();
        }

        return null;
    }

    public static String slug(JSONObject source) {
        String slug = directString(
                source,
                "slug",
                "fighterSlug");

        if (slug != null && !slug.isBlank()) {
            return slug.trim();
        }

        final JSONObject fighterObject =
                directObject(source, "fighter", "athlete", "competitor");
        if (fighterObject != null) {
            slug = directString(
                    fighterObject,
                    "slug",
                    "fighterSlug");
        }

        return slug == null ? null : slug.trim();
    }

    public static WeightClass weightClass(JSONObject source) {
        final String text = string(
                source,
                "weightClass",
                "division",
                "divisionName",
                "weight_class");

        if (text == null) {
            return null;
        }

        final String normalized = normalize(text);

        if (normalized.contains("lightheavy")) {
            return WeightClass.LIGHT_HEAVYWEIGHT;
        }
        if (normalized.contains("flyweight")) {
            return WeightClass.FLYWEIGHT;
        }
        if (normalized.contains("bantamweight")) {
            return WeightClass.BANTAMWEIGHT;
        }
        if (normalized.contains("featherweight")) {
            return WeightClass.FEATHERWEIGHT;
        }
        if (normalized.contains("lightweight")) {
            return WeightClass.LIGHTWEIGHT;
        }
        if (normalized.contains("welterweight")) {
            return WeightClass.WELTERWEIGHT;
        }
        if (normalized.contains("middleweight")) {
            return WeightClass.MIDDLEWEIGHT;
        }
        if (normalized.contains("heavyweight")) {
            return WeightClass.HEAVYWEIGHT;
        }

        return null;
    }

    public static int rank(JSONObject source) {
        return clampRank(integer(
                source,
                "rank",
                "ranking",
                "position",
                "rankNumber"));
    }

    public static JSONArray findPrimaryArray(JSONObject source) {
        final Object directData = source.opt("data");
        if (directData instanceof JSONArray) {
            return (JSONArray) directData;
        }

        final JSONArray preferred = array(
                source,
                "fighters",
                "rankings",
                "entries",
                "results",
                "athletes");
        if (preferred != null) {
            return preferred;
        }

        return firstArray(source, 0);
    }

    public static JSONObject merge(JSONObject... objects) {
        final JSONObject merged = new JSONObject();

        if (objects != null) {
            for (JSONObject object : objects) {
                if (object != null) {
                    copyInto(object, merged);
                }
            }
        }

        return merged;
    }

    private static void copyInto(JSONObject source,
                                 JSONObject target) {
        for (String key : source.keySet()) {
            target.put(key, source.opt(key));
        }
    }

    private static String record(JSONObject source) {
        final String direct = string(
                source,
                "record",
                "professionalRecord",
                "proRecord");

        if (direct != null && direct.matches(".*\\d.*")) {
            return direct;
        }

        final Integer wins = integerNullable(
                source,
                "wins",
                "win");
        final Integer losses = integerNullable(
                source,
                "losses",
                "loss");
        final Integer draws = integerNullable(
                source,
                "draws",
                "draw");

        if (wins != null || losses != null || draws != null) {
            return (wins == null ? 0 : wins)
                    + "-"
                    + (losses == null ? 0 : losses)
                    + "-"
                    + (draws == null ? 0 : draws);
        }

        return "0-0";
    }

    private static UfcEra era(JSONObject source) {
        final String direct = string(
                source,
                "era",
                "ufcEra");

        if (direct != null) {
            final String normalized = normalize(direct);
            if (normalized.contains("early")) {
                return UfcEra.EARLY_UFC;
            }
            if (normalized.contains("zuffa")) {
                return UfcEra.ZUFFA_ERA;
            }
            if (normalized.contains("modern")) {
                return UfcEra.MODERN;
            }
        }

        final List<Integer> years = new ArrayList<>();
        collectFightYears(source, years, 0);

        if (!years.isEmpty()) {
            int earliest = years.get(0);
            int latest = years.get(0);
            for (Integer year : years) {
                earliest = Math.min(earliest, year);
                latest = Math.max(latest, year);
            }

            final int midpoint = earliest + (latest - earliest) / 2;
            if (midpoint <= 2004) {
                return UfcEra.EARLY_UFC;
            }
            if (midpoint <= 2015) {
                return UfcEra.ZUFFA_ERA;
            }
            return UfcEra.MODERN;
        }

        return UfcEra.MODERN;
    }

    private static Map<Attribute, Double> attributes(
            JSONObject source,
            int rank,
            String record) {
        final Map<Attribute, Double> result =
                new EnumMap<>(Attribute.class);

        final double strikingAccuracy = percentage(
                numberNullable(source,
                        "strikingAccuracy",
                        "sigStrikingAccuracy",
                        "sigStrikeAccuracy",
                        "sigStrAccuracy",
                        "strAcc"));

        final double strikesPerMinute = defaultValue(
                numberNullable(source,
                        "sigStrikesLandedPerMinute",
                        "significantStrikesLandedPerMinute",
                        "slpm",
                        "sigStrLandedPerMin"),
                2.5);

        final double strikingDefense = percentage(
                numberNullable(source,
                        "strikingDefense",
                        "sigStrikeDefense",
                        "sigStrDefense",
                        "strDefense"));

        final double takedownAccuracy = percentage(
                numberNullable(source,
                        "takedownAccuracy",
                        "tdAccuracy",
                        "tdAcc"));

        final double takedownsPerFifteen = defaultValue(
                numberNullable(source,
                        "takedownsPer15Min",
                        "takedownsPer15Minutes",
                        "takedownAverage",
                        "tdAvg"),
                1.5);

        final double heightInches = physicalInches(
                value(source,
                        "height",
                        "heightInches",
                        "height_inches"));

        final double reachInches = physicalInches(
                value(source,
                        "reach",
                        "reachInches",
                        "reach_inches"));

        final int fights = fightCount(record);
        final double rankBonus = rank <= 0
                ? 0.0
                : (16 - rank) * 0.8;

        final double striking = clamp(
                45.0
                        + strikingAccuracy * 0.42
                        + strikesPerMinute * 4.0
                        + rankBonus);

        final double defense = clamp(
                52.0
                        + strikingDefense * 0.43
                        + rankBonus);

        final double takedown = clamp(
                42.0
                        + takedownAccuracy * 0.40
                        + takedownsPerFifteen * 5.0
                        + rankBonus);

        final double height = heightInches <= 0
                ? clamp(70.0 + rankBonus)
                : clamp(55.0 + (heightInches - 62.0) * 2.2);

        final double reach = reachInches <= 0
                ? clamp(72.0 + rankBonus)
                : clamp(55.0 + (reachInches - 64.0) * 2.1);

        final Double explicitCardio = numberNullable(
                source,
                "cardio",
                "cardioRating");

        final double cardio = explicitCardio == null
                ? clamp(68.0 + Math.min(17.0, fights * 0.65) + rankBonus)
                : clamp(toRating(explicitCardio));

        result.put(Attribute.STRIKING, striking);
        result.put(Attribute.DEFENSE, defense);
        result.put(Attribute.TAKEDOWN, takedown);
        result.put(Attribute.HEIGHT, height);
        result.put(Attribute.REACH, reach);
        result.put(Attribute.CARDIO, cardio);

        return result;
    }

    private static double percentage(Double value) {
        if (value == null) {
            return 50.0;
        }
        if (value >= 0.0 && value <= 1.0) {
            return value * 100.0;
        }
        return Math.max(0.0, Math.min(100.0, value));
    }

    private static double toRating(double value) {
        if (value >= 0.0 && value <= 1.0) {
            return value * 100.0;
        }
        return value;
    }

    private static double defaultValue(Double value,
                                       double fallback) {
        return value == null ? fallback : value;
    }

    private static int fightCount(String record) {
        if (record == null) {
            return 0;
        }

        final Matcher matcher = NUMBER.matcher(record);
        int count = 0;
        int pieces = 0;

        while (matcher.find() && pieces < 3) {
            try {
                count += (int) Double.parseDouble(matcher.group());
                pieces++;
            }
            catch (NumberFormatException ignored) {
                // Ignore malformed record pieces.
            }
        }

        return count;
    }

    private static double physicalInches(Object value) {
        if (value == null || value == JSONObject.NULL) {
            return -1.0;
        }

        if (value instanceof Number) {
            final double numeric = ((Number) value).doubleValue();
            return numeric > 100.0 ? numeric / 2.54 : numeric;
        }

        final String text = value.toString().trim().toLowerCase(Locale.ROOT);
        final Matcher feetInches = FEET_INCHES.matcher(text);
        if (feetInches.find()) {
            return Integer.parseInt(feetInches.group(1)) * 12.0
                    + Integer.parseInt(feetInches.group(2));
        }

        final Matcher matcher = NUMBER.matcher(text);
        if (!matcher.find()) {
            return -1.0;
        }

        final double numeric = Double.parseDouble(matcher.group());
        if (text.contains("cm") || numeric > 100.0) {
            return numeric / 2.54;
        }

        return numeric;
    }

    private static void collectFightYears(Object node,
                                          List<Integer> years,
                                          int depth) {
        if (node == null || depth > 7) {
            return;
        }

        if (node instanceof JSONObject) {
            final JSONObject object = (JSONObject) node;
            for (String key : object.keySet()) {
                final Object child = object.opt(key);
                final String normalizedKey = normalize(key);

                if ((normalizedKey.contains("fight")
                        || normalizedKey.contains("bout")
                        || normalizedKey.contains("debut"))
                        && child instanceof String) {
                    final Matcher matcher =
                            YEAR.matcher((String) child);
                    while (matcher.find()) {
                        years.add(Integer.parseInt(matcher.group()));
                    }
                }

                collectFightYears(child, years, depth + 1);
            }
        }
        else if (node instanceof JSONArray) {
            final JSONArray array = (JSONArray) node;
            for (int index = 0; index < array.length(); index++) {
                collectFightYears(
                        array.opt(index),
                        years,
                        depth + 1);
            }
        }
    }

    private static String directString(JSONObject source,
                                       String... keys) {
        for (String actualKey : source.keySet()) {
            for (String wantedKey : keys) {
                if (normalize(actualKey).equals(normalize(wantedKey))) {
                    final Object value = source.opt(actualKey);
                    if (value != null
                            && value != JSONObject.NULL
                            && !(value instanceof JSONObject)
                            && !(value instanceof JSONArray)) {
                        return value.toString();
                    }
                }
            }
        }
        return null;
    }

    private static JSONObject directObject(JSONObject source,
                                           String... keys) {
        for (String actualKey : source.keySet()) {
            for (String wantedKey : keys) {
                if (normalize(actualKey).equals(normalize(wantedKey))) {
                    final Object value = source.opt(actualKey);
                    if (value instanceof JSONObject) {
                        return (JSONObject) value;
                    }
                }
            }
        }
        return null;
    }

    private static Object value(JSONObject source,
                                String... keys) {
        return findValue(source, keys, 0);
    }

    private static String string(JSONObject source,
                                 String... keys) {
        final Object value = findValue(source, keys, 0);
        if (value == null || value == JSONObject.NULL) {
            return null;
        }

        if (value instanceof JSONObject) {
            return fighterName((JSONObject) value);
        }

        return value.toString();
    }

    private static Double numberNullable(JSONObject source,
                                         String... keys) {
        final Object value = findValue(source, keys, 0);
        if (value == null || value == JSONObject.NULL) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }

        if (value instanceof JSONObject) {
            final Object nested = findValue(
                    value,
                    new String[] {
                        "percent",
                        "percentage",
                        "value",
                        "rate",
                        "average",
                        "avg"
                    },
                    0);
            if (nested != null && nested != JSONObject.NULL) {
                if (nested instanceof Number) {
                    return ((Number) nested).doubleValue();
                }
                final Matcher nestedMatcher =
                        NUMBER.matcher(nested.toString());
                if (nestedMatcher.find()) {
                    try {
                        return Double.parseDouble(
                                nestedMatcher.group());
                    }
                    catch (NumberFormatException ignored) {
                        return null;
                    }
                }
            }
        }

        final Matcher matcher = NUMBER.matcher(value.toString());
        if (!matcher.find()) {
            return null;
        }

        try {
            return Double.parseDouble(matcher.group());
        }
        catch (NumberFormatException exception) {
            return null;
        }
    }

    private static Integer integerNullable(JSONObject source,
                                           String... keys) {
        final Double value = numberNullable(source, keys);
        return value == null ? null : value.intValue();
    }

    private static int integer(JSONObject source,
                               String... keys) {
        final Integer value = integerNullable(source, keys);
        return value == null ? 0 : value;
    }

    private static JSONArray array(JSONObject source,
                                   String... keys) {
        final Object value = findValue(source, keys, 0);
        return value instanceof JSONArray
                ? (JSONArray) value
                : null;
    }

    private static Object findValue(Object node,
                                    String[] wantedKeys,
                                    int depth) {
        if (node == null || depth > 6) {
            return null;
        }

        if (node instanceof JSONObject) {
            final JSONObject object = (JSONObject) node;

            for (String actualKey : object.keySet()) {
                for (String wantedKey : wantedKeys) {
                    if (normalize(actualKey).equals(normalize(wantedKey))) {
                        final Object direct = object.opt(actualKey);
                        if (direct != null && direct != JSONObject.NULL) {
                            return direct;
                        }
                    }
                }
            }

            for (String actualKey : object.keySet()) {
                final Object nested = object.opt(actualKey);
                if (nested instanceof JSONObject
                        || nested instanceof JSONArray) {
                    final Object result =
                            findValue(nested, wantedKeys, depth + 1);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        else if (node instanceof JSONArray) {
            final JSONArray array = (JSONArray) node;
            for (int index = 0; index < array.length(); index++) {
                final Object result =
                        findValue(
                                array.opt(index),
                                wantedKeys,
                                depth + 1);
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }

    private static JSONArray firstArray(Object node,
                                        int depth) {
        if (node == null || depth > 6) {
            return null;
        }

        if (node instanceof JSONArray) {
            return (JSONArray) node;
        }

        if (node instanceof JSONObject) {
            final JSONObject object = (JSONObject) node;

            final Object data = object.opt("data");
            if (data instanceof JSONArray) {
                return (JSONArray) data;
            }

            for (String key : object.keySet()) {
                final Object value = object.opt(key);
                if (value instanceof JSONArray
                        && ((JSONArray) value).length() > 0) {
                    return (JSONArray) value;
                }
            }

            for (String key : object.keySet()) {
                final Object value = object.opt(key);
                if (value instanceof JSONObject
                        || value instanceof JSONArray) {
                    final JSONArray result =
                            firstArray(value, depth + 1);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }

        return null;
    }

    private static String normalize(String text) {
        return text == null
                ? ""
                : text.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]", "");
    }

    private static int clampRank(int rank) {
        return rank >= 1 && rank <= 15 ? rank : 0;
    }

    private static double clamp(double value) {
        return Math.max(0.0, Math.min(100.0, value));
    }
}
