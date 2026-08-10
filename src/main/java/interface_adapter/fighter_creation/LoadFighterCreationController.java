package interface_adapter.fighter_creation;

import use_case.fighter_creation.LoadFighterCreationInputBoundary;

/**
 * Controller used when navigating from US1 into US2.
 */
public class LoadFighterCreationController {
    private final LoadFighterCreationInputBoundary interactor;

    public LoadFighterCreationController(
            LoadFighterCreationInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute() {
        interactor.execute();
    }
}
