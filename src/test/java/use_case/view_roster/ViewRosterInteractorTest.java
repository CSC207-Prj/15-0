package use_case.view_roster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import entity.CustomFighter;
import entity.FighterRecord;
import entity.WeightClass;

public class ViewRosterInteractorTest {

    /**
     * Minimal fake roster so the interactor is tested purely through its
     * data access interface.
     */
    private static class FakeRoster implements ViewRosterDataAccessInterface {
        private final List<CustomFighter> fighters = new ArrayList<>();

        void add(CustomFighter fighter) {
            fighters.add(fighter);
        }

        @Override
        public List<CustomFighter> getAllFighters() {
            return new ArrayList<>(fighters);
        }
    }

    private static CustomFighter fighter(String name, WeightClass weightClass,
                                         int wins, int losses, int finishes) {
        return new CustomFighter(name, weightClass,
                new FighterRecord(wins, losses, finishes), Map.of());
    }

    @Test
    public void ranksByWinsThenLossesThenFinishesThenName() {
        final FakeRoster roster = new FakeRoster();
        roster.add(fighter("Bones", WeightClass.LIGHTWEIGHT, 3, 0, 1));
        roster.add(fighter("Carter", null, 2, 1, 2));
        roster.add(fighter("Askren", WeightClass.WELTERWEIGHT, 3, 0, 2));
        roster.add(fighter("Adams", WeightClass.FEATHERWEIGHT, 3, 0, 2));

        final ViewRosterOutputBoundary presenter = new ViewRosterOutputBoundary() {
            @Override
            public void prepareSuccessView(ViewRosterOutputData outputData) {
                final List<RosterEntryData> ranked = outputData.getRankedFighters();
                assertEquals(4, ranked.size());

                // same 3-0 record and same finishes: alphabetical tie-break
                assertEquals("Adams", ranked.get(0).getName());
                assertEquals("Askren", ranked.get(1).getName());
                // same 3-0 record but fewer finishes ranks below
                assertEquals("Bones", ranked.get(2).getName());
                // fewer wins ranks last
                assertEquals("Carter", ranked.get(3).getName());

                assertEquals(3, outputData.getTopThree().size());
                assertEquals("Adams", outputData.getTopThree().get(0).getName());
                assertEquals("Askren", outputData.getTopThree().get(1).getName());
                assertEquals("Bones", outputData.getTopThree().get(2).getName());

                assertEquals("3-0", ranked.get(0).getRecordText());
                assertEquals(2, ranked.get(0).getFinishes());
                assertEquals("Featherweight", ranked.get(0).getWeightClassName());
                // a fighter whose weight class wheel has not been spun yet
                assertEquals("TBD", ranked.get(3).getWeightClassName());
            }
        };

        new ViewRosterInteractor(roster, presenter).execute();
    }

    @Test
    public void emptyRosterPresentsEmptyLists() {
        final FakeRoster roster = new FakeRoster();

        final ViewRosterOutputBoundary presenter = new ViewRosterOutputBoundary() {
            @Override
            public void prepareSuccessView(ViewRosterOutputData outputData) {
                assertTrue(outputData.getRankedFighters().isEmpty());
                assertTrue(outputData.getTopThree().isEmpty());
            }
        };

        new ViewRosterInteractor(roster, presenter).execute();
    }
}
