package data_access;

import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import entity.Attribute;
import entity.CustomFighter;
import entity.FighterRecord;
import entity.WeightClass;

public class JsonFighterRosterDataAccessTest {

    @TempDir
    Path folder;

    private static CustomFighter fighter(String name, WeightClass weightClass,
                                         int wins, int losses, int finishes, double statValue) {
        final Map<Attribute, Double> attributes = new EnumMap<>(Attribute.class);
        for (Attribute attribute : Attribute.values()) {
            attributes.put(attribute, statValue);
        }
        return new CustomFighter(name, weightClass,
                new FighterRecord(wins, losses, finishes), attributes);
    }

    @Test
    public void savedFightersSurviveARestart() throws Exception {
        final Path file = folder.resolve("saved_fighters.json");

        final JsonFighterRosterDataAccess store = new JsonFighterRosterDataAccess(file.toString());
        store.save(fighter("Iron Mohit", WeightClass.LIGHTWEIGHT, 12, 1, 9, 88.0));
        store.save(fighter("Sandman", null, 3, 0, 1, 71.5));

        // fresh instance on the same file, as if the program was reopened
        final JsonFighterRosterDataAccess reopened = new JsonFighterRosterDataAccess(file.toString());

        assertEquals(2, reopened.getAllFighters().size());
        assertTrue(reopened.existsByName("iron mohit"));

        final CustomFighter loaded = reopened.getByName("Iron Mohit");
        assertEquals("Iron Mohit", loaded.getName());
        assertEquals(WeightClass.LIGHTWEIGHT, loaded.getWeightClass());
        assertEquals(12, loaded.getRecord().getWins());
        assertEquals(1, loaded.getRecord().getLosses());
        assertEquals(9, loaded.getRecord().getFinishes());
        assertEquals(88.0, loaded.getAttribute(Attribute.STRIKING), 0.0001);
        assertTrue(loaded.hasAllAttributes());

        // a fighter saved before the weight-class wheel was spun stays unassigned
        assertNull(reopened.getByName("Sandman").getWeightClass());
    }

    @Test
    public void deleteIsPersistedAcrossRestarts() {
        final Path file = folder.resolve("saved_fighters.json");

        final JsonFighterRosterDataAccess store = new JsonFighterRosterDataAccess(file.toString());
        store.save(fighter("Keeper", WeightClass.WELTERWEIGHT, 2, 0, 1, 60.0));
        store.save(fighter("Goner", WeightClass.MIDDLEWEIGHT, 1, 1, 0, 55.0));
        store.deleteByName("Goner");

        final JsonFighterRosterDataAccess reopened = new JsonFighterRosterDataAccess(file.toString());

        assertEquals(1, reopened.getAllFighters().size());
        assertTrue(reopened.existsByName("Keeper"));
        assertFalse(reopened.existsByName("Goner"));
    }

    @Test
    public void missingFileMeansEmptyRoster() {
        final Path file = folder.resolve("does_not_exist_yet.json");

        final JsonFighterRosterDataAccess store = new JsonFighterRosterDataAccess(file.toString());

        assertTrue(store.getAllFighters().isEmpty());
        assertFalse(store.existsByName("Anyone"));
    }
}
