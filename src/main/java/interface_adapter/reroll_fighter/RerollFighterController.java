package interface_adapter.fighter_creation;

import entity.RealFighter;
import entity.UfcEra;
import use_case.reroll_fighter.RerollFighterInputBoundary;
import use_case.reroll_fighter.RerollFighterInputData;

/**
 * Controller for the Reroll Fighter use case.
 */
public class RerollFighterController {

    private final RerollFighterInputBoundary interactor;

    public RerollFighterController(RerollFighterInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(UfcEra era, int rerollsLeft, RealFighter currentFighter) {
        final RerollFighterInputData inputData = new RerollFighterInputData(era, rerollsLeft, currentFighter);

        interactor.execute(inputData);
    }
}