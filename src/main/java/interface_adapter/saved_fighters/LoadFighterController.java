package interface_adapter.saved_fighters;

import use_case.load_fighter.LoadFighterInputBoundary;
import use_case.load_fighter.LoadFighterInputData;

/**
 * Controller for the Load Fighter use case.
 */
public class LoadFighterController {
    private final LoadFighterInputBoundary interactor;

    public LoadFighterController(LoadFighterInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Loads the named fighter's full details.
     * @param fighterName the name of the fighter to load
     */
    public void execute(String fighterName) {
        interactor.execute(new LoadFighterInputData(fighterName));
    }
}
