package interface_adapter.fighter_creation;

import entity.CustomFighter;
import entity.UfcEra;
import use_case.spin_fighter.SpinFighterInputBoundary;
import use_case.spin_fighter.SpinFighterInputData;

/**
 * Controller for the Spin Fighter use case.
 */
public class SpinFighterController {

    private final SpinFighterInputBoundary interactor;

    public SpinFighterController(
            SpinFighterInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(UfcEra era) {
        interactor.execute(new SpinFighterInputData(era));
    }

    public void execute(UfcEra era,
                        CustomFighter customFighter) {
        interactor.execute(
                new SpinFighterInputData(
                        era,
                        customFighter
                )
        );
    }
}
