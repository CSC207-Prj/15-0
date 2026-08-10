package use_case.save_fighter;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
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

    /** Presenter base whose methods all fail; tests override what they expect. */
    private abstract static class ExpectingPresenter implements SaveFighterOutputBoundary {
        @Override
        public void prepareSuccessView(SaveFighterOutputData outputData) {
            fail("Unexpected success: " + outputData.getFighterName());
        }

        @Override
        public void prepareFailView(String errorMessage) {
            fail("Unexpected failure: " + errorMessage);
        }

        @Override
        public void prepareDuplicateNameView(SaveFighterOutputData outputData) {
            fail("Unexpected duplicate-name outcome: " + outputData.getFighterName());
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

        final SaveFighterOutputBoundary presenter = new ExpectingPresenter() {
            @Override
            public void prepareSuccessView(SaveFighterOutputData outputData) {
                assertEquals("Iron Mohit", outputData.getFighterName());
            }
        };

        final SaveFighterInteractor interactor = new SaveFighterInteractor(roster, presenter);
        interactor.execute(new SaveFighterInputData(completeFighter("Iron Mohit")));

        assertTrue(roster.existsByName("Iron Mohit"));
    }

    @Test
    public void failsWhenThereIsNoFighter() {
        final FakeRoster roster = new FakeRoster();

        final SaveFighterOutputBoundary presenter = new ExpectingPresenter() {
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

        final SaveFighterOutputBoundary presenter = new ExpectingPresenter() {
            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("All six attributes must be assigned before saving.", errorMessage);
            }
        };

        final SaveFighterInteractor interactor = new SaveFighterInteractor(roster, presenter);
        interactor.execute(new SaveFighterInputData(unfinished));
    }

    @Test
    public void duplicateNamePresentsRenameOutcomeWithTheUnsavedFighter() {
        final FakeRoster roster = new FakeRoster();
        roster.save(completeFighter("Iron Mohit"));
        final CustomFighter duplicate = completeFighter("IRON MOHIT");

        final boolean[] presented = {false};
        final SaveFighterOutputBoundary presenter = new ExpectingPresenter() {
            @Override
            public void prepareDuplicateNameView(SaveFighterOutputData outputData) {
                presented[0] = true;
                assertEquals("IRON MOHIT", outputData.getFighterName());
                assertTrue(outputData.isUseCaseFailed());
                assertNotNull(outputData.getFighter());
                assertSame(duplicate, outputData.getFighter());
            }
        };

        final SaveFighterInteractor interactor = new SaveFighterInteractor(roster, presenter);
        interactor.execute(new SaveFighterInputData(duplicate));

        assertTrue(presented[0]);
        // the original fighter is untouched and the duplicate was not saved over it
        assertEquals("Iron Mohit", roster.fighters.get("iron mohit").getName());
    }

    @Test
    public void renamedFighterSavesAfterDuplicateOutcome() {
        final FakeRoster roster = new FakeRoster();
        roster.save(completeFighter("Iron Mohit"));
        final CustomFighter duplicate = completeFighter("Iron Mohit");

        final CustomFighter[] pending = {null};
        final SaveFighterOutputBoundary presenter = new ExpectingPresenter() {
            @Override
            public void prepareDuplicateNameView(SaveFighterOutputData outputData) {
                pending[0] = outputData.getFighter();
            }

            @Override
            public void prepareSuccessView(SaveFighterOutputData outputData) {
                assertEquals("Iron Mohit II", outputData.getFighterName());
            }
        };

        final SaveFighterInteractor interactor = new SaveFighterInteractor(roster, presenter);
        interactor.execute(new SaveFighterInputData(duplicate));
        assertNotNull(pending[0]);

        // the view's rename dialog does exactly this: rename the pending fighter and retry
        pending[0].setName("Iron Mohit II");
        interactor.execute(new SaveFighterInputData(pending[0]));

        assertTrue(roster.existsByName("Iron Mohit II"));
        assertTrue(roster.existsByName("Iron Mohit"));
    }
}
