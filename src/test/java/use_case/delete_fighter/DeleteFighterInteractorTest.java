package use_case.delete_fighter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import entity.CustomFighter;
import entity.FighterRecord;
import entity.WeightClass;

public class DeleteFighterInteractorTest {

    /**
     * Minimal fake roster so the interactor is tested purely through its
     * data access interface.
     */
    private static class FakeRoster implements DeleteFighterDataAccessInterface {
        private final Map<String, CustomFighter> fighters = new HashMap<>();

        void add(CustomFighter fighter) {
            fighters.put(key(fighter.getName()), fighter);
        }

        @Override
        public boolean existsByName(String fighterName) {
            return fighters.containsKey(key(fighterName));
        }

        @Override
        public void deleteByName(String fighterName) {
            fighters.remove(key(fighterName));
        }

        private static String key(String name) {
            return name.trim().toLowerCase(Locale.ROOT);
        }
    }

    private static CustomFighter fighter(String name) {
        return new CustomFighter(name, WeightClass.MIDDLEWEIGHT, new FighterRecord(), Map.of());
    }

    @Test
    public void successRemovesFighterFromRoster() {
        final FakeRoster roster = new FakeRoster();
        roster.add(fighter("Keeper"));
        roster.add(fighter("Goner"));

        final DeleteFighterOutputBoundary presenter = new DeleteFighterOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteFighterOutputData outputData) {
                assertEquals("Goner", outputData.getFighterName());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected: " + errorMessage);
            }
        };

        new DeleteFighterInteractor(roster, presenter).execute(new DeleteFighterInputData("Goner"));

        assertFalse(roster.existsByName("Goner"));
        assertTrue(roster.existsByName("Keeper"));
    }

    @Test
    public void failsWhenFighterDoesNotExist() {
        final FakeRoster roster = new FakeRoster();

        final DeleteFighterOutputBoundary presenter = new DeleteFighterOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteFighterOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("No saved fighter named \"Ghost\" was found.", errorMessage);
            }
        };

        new DeleteFighterInteractor(roster, presenter).execute(new DeleteFighterInputData("Ghost"));
    }

    @Test
    public void failsWhenNoFighterIsChosen() {
        final FakeRoster roster = new FakeRoster();

        final DeleteFighterOutputBoundary presenter = new DeleteFighterOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteFighterOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Choose a fighter to delete.", errorMessage);
            }
        };

        new DeleteFighterInteractor(roster, presenter).execute(new DeleteFighterInputData("   "));
    }
}
