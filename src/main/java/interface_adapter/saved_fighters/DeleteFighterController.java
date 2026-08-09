package interface_adapter.saved_fighters;

import use_case.delete_fighter.DeleteFighterInputBoundary;
import use_case.delete_fighter.DeleteFighterInputData;

/**
 * Controller for the Delete Fighter use case.
 */
public class DeleteFighterController {
    private final DeleteFighterInputBoundary interactor;

    public DeleteFighterController(DeleteFighterInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Deletes the named fighter from the roster.
     * @param fighterName the name of the fighter to delete
     */
    public void execute(String fighterName) {
        interactor.execute(new DeleteFighterInputData(fighterName));
    }
}
