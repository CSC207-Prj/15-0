package use_case.delete_fighter;

/**
 * Interactor for the Delete Fighter use case: checks the fighter exists and
 * removes it from the roster through the data access interface.
 */
public class DeleteFighterInteractor implements DeleteFighterInputBoundary {
    private final DeleteFighterDataAccessInterface rosterDataAccess;
    private final DeleteFighterOutputBoundary presenter;

    public DeleteFighterInteractor(DeleteFighterDataAccessInterface rosterDataAccess,
                                   DeleteFighterOutputBoundary presenter) {
        this.rosterDataAccess = rosterDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(DeleteFighterInputData inputData) {
        final String fighterName = inputData.getFighterName();

        if (fighterName == null || fighterName.trim().isEmpty()) {
            presenter.prepareFailView("Choose a fighter to delete.");
        }
        else if (!rosterDataAccess.existsByName(fighterName)) {
            presenter.prepareFailView("No saved fighter named \"" + fighterName + "\" was found.");
        }
        else {
            rosterDataAccess.deleteByName(fighterName);
            presenter.prepareSuccessView(new DeleteFighterOutputData(fighterName, false));
        }
    }
}
