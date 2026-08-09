package use_case.save_fighter;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import entity.Attribute;
import entity.CustomFighter;
import entity.FighterRecord;
import entity.WeightClass;

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

    private static Map<Attribute, Double> allAttributes(double value) {
        final Map<Attribute, Double> attributes = new EnumMap<>(Attribute.class);
        for (Attribute attribute : Attribute.values()) {
            attributes.put(attribute, value);
        }
        return attributes;
    }

    private static CustomFighter completeFighter(String name) {
        return new CustomFighter(name, WeightClass.LIGHTWEIGHT, new FighterRecord(), allAttributes(80.0));
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
    public void failsWhenThereIsNoFighter() {
        final FakeRoster roster = new FakeRoster();

        final SaveFighterOutputBoundary presenter = new SaveFighterOutputBoundary() {
            @Override
            public void prepareSuccessView(SaveFighterOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("There is no fighter to save.", errorMessage);
            }
        };

        final SaveFighterInteractor interactor = new SaveFighterInteractor(roster, presenter);
        interactor.execute(new SaveFighterInputData(null));
    }

    @Test
    public void failsWhenAttributesAreIncomplete() {
        final FakeRoster roster = new FakeRoster();
        final CustomFighter unfinished = new CustomFighter("Halfway", WeightClass.WELTERWEIGHT,
                new FighterRecord(), Map.of(Attribute.STRIKING, 90.0));

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
