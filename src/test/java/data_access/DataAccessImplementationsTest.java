package data_access;

import entity.CustomFighter;
import entity.GameRun;
import org.junit.jupiter.api.Test;
import use_case.fighter_creation.FighterDataAccessInterface;
import use_case.fighter_creation.FighterDetailsDataAccessInterface;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataAccessImplementationsTest {

    @Test
    void inMemoryRosterSupportsCaseInsensitiveCrudAndCopiesLists() {
        final InMemoryFighterRosterDataAccess roster =
                new InMemoryFighterRosterDataAccess();
        final CustomFighter alpha = new CustomFighter("Alpha");
        roster.save(alpha);

        assertTrue(roster.existsByName("  ALPHA "));
        assertSame(alpha, roster.getByName("alpha"));
        final List<CustomFighter> copy = roster.getAllFighters();
        copy.clear();
        assertEquals(1, roster.getAllFighters().size());
        roster.deleteByName("AlPhA");
        assertFalse(roster.existsByName("Alpha"));
        assertNull(roster.getByName("Alpha"));
    }

    @Test
    void inMemoryCatalogueReturnsItsSixFightersDefensively() {
        final InMemoryFighterDataAccessObject catalogue =
                new InMemoryFighterDataAccessObject();
        final List<entity.RealFighter> fighters = catalogue.getFighters();
        assertEquals(6, fighters.size());
        assertEquals("Royce Gracie", fighters.get(0).getName());
        assertTrue(fighters.get(0).hasAllAttributes());
        fighters.clear();
        assertEquals(6, catalogue.getFighters().size());
    }

    @Test
    void browserAdapterDelegatesToTheSharedCatalogue() {
        final List<entity.RealFighter> expected =
                new InMemoryFighterDataAccessObject().getFighters();
        final FighterDataAccessInterface delegate = () -> expected;

        assertSame(expected,
                new FighterBrowserDataAccessAdapter(delegate).getFighters());
    }

    @Test
    void browserAdapterDelegatesOptionalDetailLoading() {
        final entity.RealFighter basic =
                new InMemoryFighterDataAccessObject().getFighters().get(0);
        final entity.RealFighter detailed =
                new InMemoryFighterDataAccessObject().getFighters().get(1);
        final class DetailCatalogue implements FighterDataAccessInterface,
                FighterDetailsDataAccessInterface {
            @Override
            public List<entity.RealFighter> getFighters() {
                return List.of(basic);
            }

            @Override
            public entity.RealFighter getFighterDetails(
                    entity.RealFighter fighter) {
                assertSame(basic, fighter);
                return detailed;
            }
        }

        final FighterBrowserDataAccessAdapter adapter =
                new FighterBrowserDataAccessAdapter(new DetailCatalogue());
        assertSame(detailed, adapter.getFighterDetails(basic));

        final FighterBrowserDataAccessAdapter plainAdapter =
                new FighterBrowserDataAccessAdapter(() -> List.of(basic));
        assertSame(basic, plainAdapter.getFighterDetails(basic));
    }

    @Test
    void simulationStoreAndRandomSourceImplementTheirContracts() {
        final InMemorySimulationDataAccessObject store =
                new InMemorySimulationDataAccessObject();
        assertNull(store.getGameRun());
        final GameRun run = new GameRun(
                new entity.CustomFighter(
                        "Player", entity.WeightClass.LIGHTWEIGHT,
                        new entity.FighterRecord(),
                        new java.util.EnumMap<>(entity.Attribute.class) {{
                            for (entity.Attribute attribute
                                    : entity.Attribute.values()) {
                                put(attribute, 80.0);
                            }
                        }}),
                DemoRankingsFactory.createDivision(
                        entity.WeightClass.LIGHTWEIGHT),
                entity.Difficulty.NORMAL, 3, false);
        store.saveGameRun(run);
        assertSame(run, store.getGameRun());
        assertSame(run, new InMemorySimulationDataAccessObject(run).getGameRun());

        final JavaRandomSource random = new JavaRandomSource();
        final double value = random.nextDouble();
        assertTrue(value >= 0.0 && value < 1.0);
        final int integer = random.nextInt(5);
        assertTrue(integer >= 0 && integer < 5);
        assertThrows(IllegalArgumentException.class, () -> random.nextInt(0));
    }
}
