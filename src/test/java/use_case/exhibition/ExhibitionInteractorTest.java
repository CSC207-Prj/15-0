package use_case.exhibition;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import entity.CustomFighter;
import entity.Difficulty;
import entity.FightMethod;
import entity.FightResult;
import entity.FightSimulator;
import entity.FighterRecord;
import entity.WeightClass;

public class ExhibitionInteractorTest {

    /**
     * Minimal fake roster so the interactor is tested purely through its
     * data access interface.
     */
    private static class FakeRoster implements ExhibitionDataAccessInterface {
        private final Map<String, CustomFighter> fighters = new HashMap<>();

        void add(CustomFighter fighter) {
            fighters.put(fighter.getName().trim().toLowerCase(Locale.ROOT), fighter);
        }

        @Override
        public CustomFighter getByName(String fighterName) {
            return fighters.get(fighterName.trim().toLowerCase(Locale.ROOT));
        }
    }

    private static CustomFighter fighter(String name, int wins) {
        return new CustomFighter(name, WeightClass.LIGHTWEIGHT,
                new FighterRecord(wins, 0, 0), Map.of());
    }

    @Test
    public void successWhenFighterAWins() {
        final FakeRoster roster = new FakeRoster();
        roster.add(fighter("Alpha", 3));
        roster.add(fighter("Bravo", 2));

        // stub of the shared simulator: fighter A wins by KO/TKO in round 2 at 1:37
        final FightSimulator stub = (player, opponent, maxRounds, difficulty) -> {
            assertEquals("Alpha", player.getName());
            assertEquals("Bravo", opponent.getName());
            assertEquals(3, maxRounds);
            assertEquals(Difficulty.NORMAL, difficulty);
            return new FightResult(opponent, true, FightMethod.KO_TKO, 2, 97);
        };

        final ExhibitionOutputBoundary presenter = new ExhibitionOutputBoundary() {
            @Override
            public void prepareSuccessView(ExhibitionOutputData outputData) {
                assertEquals("Alpha", outputData.getWinnerName());
                assertEquals("Bravo", outputData.getLoserName());
                assertEquals("KO/TKO", outputData.getMethod());
                assertEquals(2, outputData.getRound());
                assertEquals(97, outputData.getSecondsInRound());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected: " + errorMessage);
            }
        };

        new ExhibitionInteractor(roster, stub, presenter)
                .execute(new ExhibitionInputData("Alpha", "Bravo"));

        // exhibition matches are one-offs: saved records must not change
        assertEquals(3, roster.getByName("Alpha").getRecord().getWins());
        assertEquals(2, roster.getByName("Bravo").getRecord().getWins());
        assertEquals(0, roster.getByName("Alpha").getRecord().getLosses());
        assertEquals(0, roster.getByName("Bravo").getRecord().getLosses());
    }

    @Test
    public void successWhenFighterBWins() {
        final FakeRoster roster = new FakeRoster();
        roster.add(fighter("Alpha", 3));
        roster.add(fighter("Bravo", 2));

        final FightSimulator stub = (player, opponent, maxRounds, difficulty) ->
                new FightResult(opponent, false, FightMethod.DECISION, 3, 300);

        final ExhibitionOutputBoundary presenter = new ExhibitionOutputBoundary() {
            @Override
            public void prepareSuccessView(ExhibitionOutputData outputData) {
                assertEquals("Bravo", outputData.getWinnerName());
                assertEquals("Alpha", outputData.getLoserName());
                assertEquals("Decision", outputData.getMethod());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected: " + errorMessage);
            }
        };

        new ExhibitionInteractor(roster, stub, presenter)
                .execute(new ExhibitionInputData("Alpha", "Bravo"));
    }

    @Test
    public void failsWhenSameFighterChosenTwice() {
        final FakeRoster roster = new FakeRoster();
        roster.add(fighter("Alpha", 1));

        final ExhibitionOutputBoundary presenter = new ExhibitionOutputBoundary() {
            @Override
            public void prepareSuccessView(ExhibitionOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Choose two different fighters for the exhibition match.", errorMessage);
            }
        };

        final FightSimulator stub = (player, opponent, maxRounds, difficulty) -> {
            throw new AssertionError("Simulator must not run for an invalid matchup.");
        };

        new ExhibitionInteractor(roster, stub, presenter)
                .execute(new ExhibitionInputData("Alpha", "alpha"));
    }

    @Test
    public void failsWhenAFighterIsMissing() {
        final FakeRoster roster = new FakeRoster();
        roster.add(fighter("Alpha", 1));

        final ExhibitionOutputBoundary presenter = new ExhibitionOutputBoundary() {
            @Override
            public void prepareSuccessView(ExhibitionOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("No saved fighter named \"Ghost\" was found.", errorMessage);
            }
        };

        final FightSimulator stub = (player, opponent, maxRounds, difficulty) -> {
            throw new AssertionError("Simulator must not run when a fighter is missing.");
        };

        new ExhibitionInteractor(roster, stub, presenter)
                .execute(new ExhibitionInputData("Alpha", "Ghost"));
    }

    @Test
    public void failsWhenNothingIsChosen() {
        final FakeRoster roster = new FakeRoster();

        final ExhibitionOutputBoundary presenter = new ExhibitionOutputBoundary() {
            @Override
            public void prepareSuccessView(ExhibitionOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Choose two saved fighters for the exhibition match.", errorMessage);
            }
        };

        final FightSimulator stub = (player, opponent, maxRounds, difficulty) -> {
            throw new AssertionError("Simulator must not run for an empty selection.");
        };

        new ExhibitionInteractor(roster, stub, presenter)
                .execute(new ExhibitionInputData("  ", null));
    }
}
