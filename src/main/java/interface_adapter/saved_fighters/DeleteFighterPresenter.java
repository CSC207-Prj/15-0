package interface_adapter.saved_fighters;

import use_case.delete_fighter.DeleteFighterOutputBoundary;
import use_case.delete_fighter.DeleteFighterOutputData;

/**
 * Presenter for the Delete Fighter use case: turns the outcome into
 * Saved Fighters view state.
 */
public class DeleteFighterPresenter implements DeleteFighterOutputBoundary {
    private final SavedFightersViewModel viewModel;

    public DeleteFighterPresenter(SavedFightersViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(DeleteFighterOutputData outputData) {
        final SavedFightersState state = viewModel.getState();
        state.setMessage("Deleted \"" + outputData.getFighterName() + "\" from your roster.");
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
}
