package use_case.save_fighter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import entity.Attribute;
import entity.CustomFighter;

public class SaveFighterInteractorTest {

    /**
     * Minimal fake roster so the interactor is tested purely through its
     * data access interface.
     */
    private static class FakeRoster implements SaveFighterDataAccessInterface {
        private final Map<String, CustomFighter> fighters = new HashMap<>();

        @Override
        public boolean existsByName(String fighterName) {
            return fighters.containsKey(fighterName.trim().toLowerCase(Locale.ROOT));
        }

        @Override
        public void save(CustomFighter fighter) {
            fighters.put(fighter.getName().trim().toLowerCase(Locale.ROOT), fighter);
        }
    }

    private static CustomFighter completeFighter(String name) {
        final CustomFighter fighter = new CustomFighter(name, "Lightweight");
        for (Attribute attribute : Attribute.values()) {
            fighter.setAttribute(attribute, 80.0);
        }
        return fighter;
    }

    @Test
    public void successSavesFighterToRoster() {
        final FakeRoster roster = new FakeRoster();

        final SaveFighterOutputBoundary presenter = new SaveFighterOutputBoundary() {
            @Override
            public void prepareSuccessView(SaveFighterOutputData outputData) {
                assertEquals("Iron Mohit", outputData.getFighterName());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected: " + errorMessage);
            }
        };

        final SaveFighterInteractor interactor = new SaveFighterInteractor(roster, presenter);
        interactor.execute(new SaveFighterInputData(completeFighter("Iron Mohit")));

        assertTrue(roster.existsByName("Iron Mohit"));
    }

    @Test
    public void failsWhenNameIsBlank() {
        final FakeRoster roster = new FakeRoster();

        final SaveFighterOutputBoundary presenter = new SaveFighterOutputBoundary() {
            @Override
            public void prepareSuccessView(SaveFighterOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Your fighter needs a name before it can be saved.", errorMessage);
            }
        };

        final SaveFighterInteractor interactor = new SaveFighterInteractor(roster, presenter);
        interactor.execute(new SaveFighterInputData(completeFighter("   ")));
    }

    @Test
    public void failsWhenAttributesAreIncomplete() {
        final FakeRoster roster = new FakeRoster();
        final CustomFighter unfinished = new CustomFighter("Halfway", "Welterweight");
        unfinished.setAttribute(Attribute.STRIKING, 90.0);

        final SaveFighterOutputBoundary presenter = new SaveFighterOutputBoundary() {
            @Override
            public void prepareSuccessView(SaveFighterOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("All six attributes must be assigned before saving.", errorMessage);
            }
        };

        final SaveFighterInteractor interactor = new SaveFighterInteractor(roster, presenter);
        interactor.execute(new SaveFighterInputData(unfinished));
    }

    @Test
    public void failsWhenNameIsAlreadyTakenIgnoringCase() {
        final FakeRoster roster = new FakeRoster();
        roster.save(completeFighter("Iron Mohit"));

        final SaveFighterOutputBoundary presenter = new SaveFighterOutputBoundary() {
            @Override
            public void prepareSuccessView(SaveFighterOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("A fighter named \"IRON MOHIT\" is already in your roster.", errorMessage);
            }
        };

        final SaveFighterInteractor interactor = new SaveFighterInteractor(roster, presenter);
        interactor.execute(new SaveFighterInputData(completeFighter("IRON MOHIT")));
    }
}
