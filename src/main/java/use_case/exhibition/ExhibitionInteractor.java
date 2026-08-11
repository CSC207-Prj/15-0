package use_case.exhibition;

import entity.CustomFighter;
import entity.Difficulty;
import entity.FightResult;
import entity.FightSimulator;
import entity.RealFighter;
import entity.UfcEra;

/**
 * Interactor for the Exhibition Match use case: loads the two chosen saved
 * fighters and runs a one-off match between them, reusing the shared
 * FightSimulator from the simulation use case. Exhibition results are for
 * bragging rights only, so neither fighter's saved record is changed.
 */
public class ExhibitionInteractor implements ExhibitionInputBoundary {
    private static final int EXHIBITION_ROUNDS = 3;

    private final ExhibitionDataAccessInterface rosterDataAccess;
    private final FightSimulator fightSimulator;
    private final ExhibitionOutputBoundary presenter;

    public ExhibitionInteractor(ExhibitionDataAccessInterface rosterDataAccess,
                                FightSimulator fightSimulator,
                                ExhibitionOutputBoundary presenter) {
        this.rosterDataAccess = rosterDataAccess;
        this.fightSimulator = fightSimulator;
        this.presenter = presenter;
    }

    @Override
    public void execute(ExhibitionInputData inputData) {
        final String firstName = inputData.getFirstFighterName();
        final String secondName = inputData.getSecondFighterName();

        if (isBlank(firstName) || isBlank(secondName)) {
            presenter.prepareFailView("Choose two saved fighters for the exhibition match.");
        }
        else if (firstName.trim().equalsIgnoreCase(secondName.trim())) {
            presenter.prepareFailView("Choose two different fighters for the exhibition match.");
        }
        else {
            findAndRun(firstName, secondName);
        }
    }

    private void findAndRun(String firstName, String secondName) {
        final CustomFighter firstFighter = rosterDataAccess.getByName(firstName);
        final CustomFighter secondFighter = rosterDataAccess.getByName(secondName);

        if (firstFighter == null) {
            presenter.prepareFailView("No saved fighter named \"" + firstName + "\" was found.");
        }
        else if (secondFighter == null) {
            presenter.prepareFailView("No saved fighter named \"" + secondName + "\" was found.");
        }
        else {
            runMatch(firstFighter, secondFighter);
        }
    }

    private void runMatch(CustomFighter firstFighter, CustomFighter secondFighter) {
        final FightResult result = fightSimulator.simulate(
                firstFighter, asOpponent(secondFighter), EXHIBITION_ROUNDS, Difficulty.NORMAL);

        final String winnerName;
        final String loserName;
        if (result.isPlayerWon()) {
            winnerName = firstFighter.getName();
            loserName = secondFighter.getName();
        }
        else {
            winnerName = secondFighter.getName();
            loserName = firstFighter.getName();
        }

        presenter.prepareSuccessView(new ExhibitionOutputData(
                winnerName,
                loserName,
                result.getMethod().getDisplayName(),
                result.getRound(),
                result.getSecondsInRound(),
                false));
    }

    /**
     * Adapts the second custom fighter into the RealFighter opponent slot of
     * the shared FightSimulator, so exhibitions reuse the exact simulation
     * algorithm without modifying it. Rank and era are display-only
     * placeholders here; the simulation only reads the attributes. NORMAL
     * difficulty keeps the match neutral, since its score adjustment is zero.
     * @param fighter the second custom fighter in the matchup
     * @return the same fighter's data in RealFighter form
     */
    private static RealFighter asOpponent(CustomFighter fighter) {
        return new RealFighter(
                fighter.getName(),
                fighter.getWeightClass(),
                0,
                UfcEra.ALL_TIME,
                fighter.getRecord().toString(),
                fighter.getAttributes());
    }

    private static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}
