package interface_adapter.fighter_creation;

import use_case.fighter_creation.LoadFighterCreationOutputBoundary;
import use_case.fighter_creation.LoadFighterCreationOutputData;

/**
 * Initializes the Fighter Creation view model from the configured run.
 */
public class LoadFighterCreationPresenter
        implements LoadFighterCreationOutputBoundary {

    private final FighterCreationViewModel viewModel;

    public LoadFighterCreationPresenter(
            FighterCreationViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(
            LoadFighterCreationOutputData outputData) {
        viewModel.initialize(
                outputData.getSettings(),
                outputData.getCustomFighter()
        );
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
    }
}
