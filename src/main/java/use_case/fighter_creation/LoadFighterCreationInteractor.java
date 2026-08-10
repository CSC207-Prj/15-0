package use_case.fighter_creation;

import entity.CustomFighter;
import entity.GameSettings;

/**
 * Loads the run that US1 stored in the shared session.
 */
public class LoadFighterCreationInteractor
        implements LoadFighterCreationInputBoundary {

    private final FighterCreationSessionDataAccessInterface sessionDataAccess;
    private final LoadFighterCreationOutputBoundary presenter;

    public LoadFighterCreationInteractor(
            FighterCreationSessionDataAccessInterface sessionDataAccess,
            LoadFighterCreationOutputBoundary presenter) {
        this.sessionDataAccess = sessionDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        if (!sessionDataAccess.hasConfiguredRun()) {
            presenter.prepareFailView(
                    "Configure the run before building a fighter."
            );
            return;
        }

        final GameSettings settings =
                sessionDataAccess.getGameSettings();
        final CustomFighter customFighter =
                sessionDataAccess.getCustomFighter();

        presenter.prepareSuccessView(
                new LoadFighterCreationOutputData(
                        settings,
                        customFighter
                )
        );
    }
}
