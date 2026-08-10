package interface_adapter.saved_fighters;

import use_case.save_fighter.SaveFighterOutputBoundary;
import use_case.save_fighter.SaveFighterOutputData;

/**
 * Presenter for the Save Fighter use case: turns the outcome into
 * Saved Fighters view state.
 */
public class SaveFighterPresenter implements SaveFighterOutputBoundary {
    private final SavedFightersViewModel viewModel;

    public SaveFighterPresenter(SavedFightersViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SaveFighterOutputData outputData) {
        final SavedFightersState state = viewModel.getState();
        state.setMessage("Saved \"" + outputData.getFighterName() + "\" to your roster.");
        state.setError("");
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final SavedFightersState state = viewModel.getState();
        state.setError(errorMessage);
        state.setMessage("");
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareDuplicateNameView(SaveFighterOutputData outputData) {
        final SavedFightersState state = viewModel.getState();
        state.setDuplicatePending(outputData.getFighter());
        state.setError("A fighter named \"" + outputData.getFighterName()
                + "\" is already in your roster — choose a different name.");
        state.setMessage("");
        viewModel.firePropertyChanged();
    }
}
