package use_case.save_fighter;

import entity.CustomFighter;

/**
 * Interactor for the Save Fighter use case: validates the fighter and stores
 * it in the roster through the data access interface.
 */
public class SaveFighterInteractor implements SaveFighterInputBoundary {
    private final SaveFighterDataAccessInterface rosterDataAccess;
    private final SaveFighterOutputBoundary presenter;

    public SaveFighterInteractor(SaveFighterDataAccessInterface rosterDataAccess,
                                 SaveFighterOutputBoundary presenter) {
        this.rosterDataAccess = rosterDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SaveFighterInputData inputData) {
        final CustomFighter fighter = inputData.getFighter();

        if (fighter == null) {
            presenter.prepareFailView("There is no fighter to save.");
        }
        else if (fighter.getName() == null || fighter.getName().trim().isEmpty()) {
            presenter.prepareFailView("Your fighter needs a name before it can be saved.");
        }
        else if (!fighter.hasAllAttributes()) {
            presenter.prepareFailView("All six attributes must be assigned before saving.");
        }
        else if (rosterDataAccess.existsByName(fighter.getName())) {
            presenter.prepareFailView(
                    "A fighter named \"" + fighter.getName() + "\" is already in your roster.");
        }
        else {
            rosterDataAccess.save(fighter);
            presenter.prepareSuccessView(new SaveFighterOutputData(fighter.getName(), false));
        }
    }
}
