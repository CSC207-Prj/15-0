package data_access;

import entity.Attribute;
import entity.RealFighter;
import entity.UfcEra;
import entity.WeightClass;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CitoFighterMapperTest {
    private static final double TOLERANCE = 0.0001;

    @Test
    void mapsCompleteDirectoryAndStatFields() {
        final JSONObject json = new JSONObject()
                .put("fullName", "  Complete Fighter  ")
                .put("weightClass", "UFC Lightweight")
                .put("rank", 3)
                .put("record", "20-2-1")
                .put("era", "early UFC")
                .put("strikingAccuracy", 0.5)
                .put("slpm", 4.0)
                .put("strikingDefense", "60%")
                .put("takedownAccuracy", 55)
                .put("tdAvg", 2.0)
                .put("height", "5'11")
                .put("reach", "74 in")
                .put("cardio", 0.9);

        final RealFighter fighter = CitoFighterMapper.toFighter(json);

        assertEquals("Complete Fighter", fighter.getName());
        assertEquals(WeightClass.LIGHTWEIGHT, fighter.getWeightClass());
        assertEquals(3, fighter.getRank());
        assertEquals(UfcEra.EARLY_UFC, fighter.getEra());
        assertEquals("20-2-1", fighter.getProfessionalRecord());
        assertEquals(92.4, fighter.getAttributes().get(Attribute.STRIKING),
                TOLERANCE);
        assertEquals(88.2, fighter.getAttributes().get(Attribute.DEFENSE),
                TOLERANCE);
        assertEquals(84.4, fighter.getAttributes().get(Attribute.TAKEDOWN),
                TOLERANCE);
        assertEquals(74.8, fighter.getAttributes().get(Attribute.HEIGHT),
                TOLERANCE);
        assertEquals(76.0, fighter.getAttributes().get(Attribute.REACH),
                TOLERANCE);
        assertEquals(90.0, fighter.getAttributes().get(Attribute.CARDIO),
                TOLERANCE);
    }

    @Test
    void mapsNestedNamesObjectsRecordsDatesAndMetricMeasurements() {
        final JSONObject json = new JSONObject()
                .put("fighter", new JSONObject()
                        .put("givenName", "Nested")
                        .put("familyName", "Athlete")
                        .put("fighterSlug", "nested-athlete"))
                .put("division", new JSONObject().put("name", "Welterweight"))
                .put("ranking", "#7")
                .put("career", new JSONObject()
                        .put("wins", "12")
                        .put("loss", 3)
                        .put("draws", 1)
                        .put("debutFightDate", "2010-03-01"))
                .put("statistics", new JSONObject()
                        .put("sigStrikeAccuracy",
                                new JSONObject().put("percentage", "48.5%"))
                        .put("sigStrLandedPerMin", "3.2")
                        .put("sigStrDefense",
                                new JSONObject().put("value", 0.61))
                        .put("tdAccuracy", new JSONObject().put("rate", 0.4))
                        .put("takedownAverage",
                                new JSONObject().put("avg", "1.75"))
                        .put("heightInches", 180)
                        .put("reach_inches", "190 cm"));

        final RealFighter fighter = CitoFighterMapper.toFighter(json);

        assertEquals("Nested Athlete", fighter.getName());
        assertEquals("nested-athlete", CitoFighterMapper.slug(json));
        assertEquals(WeightClass.WELTERWEIGHT, fighter.getWeightClass());
        assertEquals(7, fighter.getRank());
        assertEquals("12-3-1", fighter.getProfessionalRecord());
        assertEquals(UfcEra.ZUFFA_ERA, fighter.getEra());
        assertTrue(fighter.hasAllAttributes());
        assertTrue(fighter.getAttributes().get(Attribute.HEIGHT) > 70.0);
        assertTrue(fighter.getAttributes().get(Attribute.REACH) > 75.0);
    }

    @Test
    void supportsAllWeightClassSpellingsAndNameVariants() {
        final Map<String, WeightClass> cases = new LinkedHashMap<>();
        cases.put("Flyweight", WeightClass.FLYWEIGHT);
        cases.put("Bantam Weight", WeightClass.BANTAMWEIGHT);
        cases.put("Featherweight", WeightClass.FEATHERWEIGHT);
        cases.put("Lightweight", WeightClass.LIGHTWEIGHT);
        cases.put("Welterweight", WeightClass.WELTERWEIGHT);
        cases.put("Middleweight", WeightClass.MIDDLEWEIGHT);
        cases.put("Light-Heavyweight", WeightClass.LIGHT_HEAVYWEIGHT);
        cases.put("Heavyweight", WeightClass.HEAVYWEIGHT);

        for (Map.Entry<String, WeightClass> entry : cases.entrySet()) {
            assertEquals(entry.getValue(), CitoFighterMapper.weightClass(
                    new JSONObject().put("divisionName", entry.getKey())));
        }

        assertEquals("Display", CitoFighterMapper.fighterName(
                new JSONObject().put("displayName", " Display ")));
        assertEquals("Nested Name", CitoFighterMapper.fighterName(
                new JSONObject().put("athlete",
                        new JSONObject().put("name", "Nested Name"))));
        assertEquals("Plain Name", CitoFighterMapper.fighterName(
                new JSONObject().put("name", "Plain Name")));
        assertEquals("First", CitoFighterMapper.fighterName(
                new JSONObject().put("firstName", "First")));
        assertEquals("Last", CitoFighterMapper.fighterName(
                new JSONObject().put("lastName", "Last")));
        assertNull(CitoFighterMapper.fighterName(new JSONObject()));
        assertNull(CitoFighterMapper.weightClass(
                new JSONObject().put("division", "Women's Strawweight")));
    }

    @Test
    void derivesEraFromNestedFightHistoryAndDefaultsToModern() {
        assertEquals(UfcEra.EARLY_UFC, mapEra(new JSONObject()
                .put("bouts", new JSONArray()
                        .put(new JSONObject().put("fightDate", "1998-01-01"))
                        .put(new JSONObject().put("fightDate", "2002-01-01")))));
        assertEquals(UfcEra.ZUFFA_ERA, mapEra(new JSONObject()
                .put("lastBout", "2014-01-01")));
        assertEquals(UfcEra.MODERN, mapEra(new JSONObject()
                .put("lastFight", "2023-01-01")));
        assertEquals(UfcEra.ZUFFA_ERA, mapEra(new JSONObject()
                .put("ufcEra", "Zuffa ownership")));
        assertEquals(UfcEra.MODERN, mapEra(new JSONObject()
                .put("era", "modern roster")));
        assertEquals(UfcEra.MODERN, mapEra(new JSONObject()));
    }

    @Test
    void forcedValuesOverrideMissingSourceValuesAndRanksAreClamped() {
        final RealFighter forced = CitoFighterMapper.toFighter(
                new JSONObject().put("fighterName", "Forced"),
                WeightClass.HEAVYWEIGHT,
                15,
                UfcEra.EARLY_UFC);

        assertEquals(WeightClass.HEAVYWEIGHT, forced.getWeightClass());
        assertEquals(15, forced.getRank());
        assertEquals(UfcEra.EARLY_UFC, forced.getEra());
        assertEquals("0-0", forced.getProfessionalRecord());
        assertTrue(forced.hasAllAttributes());
        assertEquals(0, CitoFighterMapper.rank(
                new JSONObject().put("position", 99)));
        assertEquals(0, CitoFighterMapper.rank(new JSONObject()));

        final RealFighter overloaded = CitoFighterMapper.toFighter(
                new JSONObject().put("name", "Overload"),
                WeightClass.FLYWEIGHT,
                1);
        assertEquals(1, overloaded.getRank());
    }

    @Test
    void rejectsIncompleteRows() {
        assertThrows(IllegalArgumentException.class,
                () -> CitoFighterMapper.toFighter(new JSONObject()
                        .put("division", "Lightweight")));
        assertThrows(IllegalArgumentException.class,
                () -> CitoFighterMapper.toFighter(new JSONObject()
                        .put("name", "No Division")));
    }

    @Test
    void findsArraysAcrossSupportedResponseShapes() {
        final JSONArray direct = new JSONArray().put(1);
        assertSame(direct, CitoFighterMapper.findPrimaryArray(
                new JSONObject().put("data", direct)));

        final JSONArray rankings = new JSONArray().put(2);
        assertSame(rankings, CitoFighterMapper.findPrimaryArray(
                new JSONObject().put("rankings", rankings)));

        final JSONArray deep = new JSONArray().put(3);
        assertSame(deep, CitoFighterMapper.findPrimaryArray(
                new JSONObject().put("payload",
                        new JSONObject().put("unknown", deep))));
        assertNull(CitoFighterMapper.findPrimaryArray(new JSONObject()));
    }

    @Test
    void mergeSkipsNullsAndLaterObjectsWin() {
        final JSONObject first = new JSONObject()
                .put("name", "First")
                .put("record", "1-0");
        final JSONObject second = new JSONObject()
                .put("name", "Second")
                .put("rank", 2);

        final JSONObject merged = CitoFighterMapper.merge(
                first, null, second);
        assertEquals("Second", merged.getString("name"));
        assertEquals("1-0", merged.getString("record"));
        assertEquals(2, merged.getInt("rank"));
        assertEquals(0, CitoFighterMapper.merge((JSONObject[]) null).length());
    }

    @Test
    void unusualStatsUseSafeDefaultsAndClampRatings() {
        final RealFighter fighter = CitoFighterMapper.toFighter(
                new JSONObject()
                        .put("name", "Edge Stats")
                        .put("division", "Middleweight")
                        .put("rankNumber", 1)
                        .put("professionalRecord", "many fights")
                        .put("strAcc", -20)
                        .put("strDefense", 200)
                        .put("tdAcc", "unknown")
                        .put("height", "unknown")
                        .put("reach", JSONObject.NULL)
                        .put("cardioRating", 500));

        assertEquals("0-0", fighter.getProfessionalRecord());
        assertEquals(67.0,
                fighter.getAttributes().get(Attribute.STRIKING), TOLERANCE);
        assertEquals(100.0,
                fighter.getAttributes().get(Attribute.DEFENSE), TOLERANCE);
        assertEquals(100.0,
                fighter.getAttributes().get(Attribute.CARDIO), TOLERANCE);
        assertTrue(fighter.getAttributes().get(Attribute.HEIGHT) > 70.0);
        assertTrue(fighter.getAttributes().get(Attribute.REACH) > 72.0);
    }

    private static UfcEra mapEra(JSONObject fields) {
        fields.put("name", "Era Fighter");
        fields.put("division", "Lightweight");
        return CitoFighterMapper.toFighter(fields).getEra();
    }
}
