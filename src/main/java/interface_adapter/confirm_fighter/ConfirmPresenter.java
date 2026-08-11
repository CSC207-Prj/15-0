package interface_adapter.confirm_fighter;

import use_case.confirm.ConfirmOutputBoundary;
import use_case.confirm.ConfirmOutputData;

/**
 * Updates confirm-screen state from the results of the confirm use case.
 */
public class ConfirmPresenter implements ConfirmOutputBoundary {
    private final ConfirmViewModel confirmViewModel;

    /**
     * Creates a presenter for the supplied view model.
     *
     * @param confirmViewModel the view model to update
     */
    public ConfirmPresenter(ConfirmViewModel confirmViewModel) {
        this.confirmViewModel = confirmViewModel;
    }

    /**
     * Copies successful output data into the current screen state.
     *
     * @param outputData the successful use-case result
     * @param confirm whether the fighter has been confirmed
     */
    private void updateState(ConfirmOutputData outputData, boolean confirm) {
        final ConfirmState state = confirmViewModel.getState();
        state.setFighterName(outputData.getFighterName());
        state.setAttributePoints(outputData.getAttributePoints());
        state.setWeightClass(outputData.getWeightClass());
        state.setOverall(Integer.toString(outputData.getOverall()));
        state.setWeightClassLocked(true);
        state.setConfirmed(confirm);
        state.setErrorMessage(null);
        confirmViewModel.firePropertyChanged();
    }

    /**
     * Updates the screen after a successful weight-class spin.
     *
     * @param outputData the fighter data produced by the spin
     */
    @Override
    public void prepareSpinSuccessView(ConfirmOutputData outputData) {
        final ConfirmState state = confirmViewModel.getState();
        state.setWeightClassLocked(true);
        updateState(outputData, false);
    }

    /**
     * Updates the screen after the fighter is successfully confirmed.
     *
     * @param outputData the confirmed fighter data
     */
    @Override
    public void prepareConfirmSuccessView(ConfirmOutputData outputData) {
        updateState(outputData, true);
    }

    /**
     * Updates the screen with an error from the confirm use case.
     *
     * @param message the error message to display
     */
    @Override
    public void prepareFailureView(String message) {
        final ConfirmState state = confirmViewModel.getState();
        state.setErrorMessage(message);
        confirmViewModel.firePropertyChanged();
    }
}
