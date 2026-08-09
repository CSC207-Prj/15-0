package interface_adapter.saved_fighters;

import entity.CustomFighter;
import use_case.save_fighter.SaveFighterInputBoundary;
import use_case.save_fighter.SaveFighterInputData;

/**
 * Controller for the Save Fighter use case. Called by whichever screen holds
 * the finished fighter (for example after a gauntlet run ends).
 */
public class SaveFighterController {
    private final SaveFighterInputBoundary interactor;

    public SaveFighterController(SaveFighterInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Saves the given fighter to the roster.
     * @param fighter the finished fighter to save
     */
    public void execute(CustomFighter fighter) {
        interactor.execute(new SaveFighterInputData(fighter));
    }
}
