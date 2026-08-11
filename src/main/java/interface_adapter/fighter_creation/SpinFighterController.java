package interface_adapter.fighter_creation;

import entity.UfcEra;
import use_case.spin_fighter.SpinFighterInputBoundary;
import use_case.spin_fighter.SpinFighterInputData;

/**
 * Controller for the Spin Fighter use case.
 */
public class SpinFighterController {

    private final SpinFighterInputBoundary interactor;

    public SpinFighterController(SpinFighterInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Sends a spin request for the selected era to the interactor.
     *
     * @param era selected fighter-pool era
     */
    public void execute(UfcEra era) {
        final SpinFighterInputData inputData = new SpinFighterInputData(era);

        interactor.execute(inputData);
    }
}
