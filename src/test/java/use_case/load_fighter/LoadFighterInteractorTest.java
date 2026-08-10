package use_case.load_fighter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import entity.Attribute;
import entity.CustomFighter;
import entity.FighterRecord;
import entity.WeightClass;

public class LoadFighterInteractorTest {

    /**
     * Minimal fake roster so the interactor is tested purely through its
     * data access interface.
     */
    private static class FakeRoster implements LoadFighterDataAccessInterface {
        private final Map<String, CustomFighter> fighters = new HashMap<>();

        void add(CustomFighter fighter) {
            fighters.put(fighter.getName().trim().toLowerCase(Locale.ROOT), fighter);
        }

        @Override
        public CustomFighter getByName(String fighterName) {
            return fighters.get(fighterName.trim().toLowerCase(Locale.ROOT));
        }
    }

    @Test
    public void successPresentsFullFighterDetails() {
        final FakeRoster roster = new FakeRoster();
        final Map<Attribute, Double> attributes = new java.util.EnumMap<>(Attribute.class);
        for (Attribute attribute : Attribute.values()) {
            attributes.put(attribute, 77.0);
        }
        roster.add(new CustomFighter("Iron Mohit", WeightClass.LIGHTWEIGHT,
                new FighterRecord(12, 1, 9), attributes));

        final LoadFighterOutputBoundary presenter = new LoadFighterOutputBoundary() {
            @Override
            public void prepareSuccessView(LoadFighterOutputData outputData) {
                assertEquals("Iron Mohit", outputData.getFighterName());
                assertEquals("Lightweight", outputData.getWeightClassName());
                assertEquals("12-1", outputData.getRecordText());
                assertEquals(9, outputData.getFinishes());
                assertEquals(Attribute.values().length, outputData.getAttributeValues().size());
                assertEquals(Double.valueOf(77.0),
                        outputData.getAttributeValues().get(Attribute.STRIKING.getDisplayName()));
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected: " + errorMessage);
            }
        };

        new LoadFighterInteractor(roster, presenter)
                .execute(new LoadFighterInputData("iron mohit"));
    }

    @Test
    public void failsWhenFighterDoesNotExist() {
        final FakeRoster roster = new FakeRoster();

        final LoadFighterOutputBoundary presenter = new LoadFighterOutputBoundary() {
            @Override
            public void prepareSuccessView(LoadFighterOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("No saved fighter named \"Ghost\" was found.", errorMessage);
            }
        };

        new LoadFighterInteractor(roster, presenter).execute(new LoadFighterInputData("Ghost"));
    }
}
