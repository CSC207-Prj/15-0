package interface_adapter.reroll_fighter;

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

    /**
     * Sends a reroll request to the interactor.
     *
     * @param era selected fighter-pool era
     * @param rerollsLeft rerolls available before this request
     * @param currentFighter fighter that must be replaced
     */
    public void execute(UfcEra era, int rerollsLeft, RealFighter currentFighter) {
        final RerollFighterInputData inputData =
                new RerollFighterInputData(era, rerollsLeft, currentFighter);

        interactor.execute(inputData);
    }
}
