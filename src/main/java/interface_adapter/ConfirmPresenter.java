package interface_adapter;

import use_case.confirm.ConfirmOutputBoundary;
import use_case.confirm.ConfirmOutputData;

public class ConfirmPresenter implements ConfirmOutputBoundary {
    private final ConfirmViewModel confirmViewModel;
    public ConfirmPresenter(ConfirmViewModel confirmViewModel) {
        this.confirmViewModel = confirmViewModel;
    }

    private void updateState(ConfirmOutputData outputData, boolean confirm) {
        final ConfirmState state = confirmViewModel.getState();
        state.setFighterName(outputData.getFighterName());
        state.setAttributePoints(outputData.getAttributePoints());
        state.setWeightClass(outputData.getWeightClass());
        state.setOverall(Integer.toString(outputData.getOverall()));
        state.setConfirmed(confirm);
        state.setErrorMessage(null);
        confirmViewModel.firePropertyChanged();


    }
    @Override
    public void prepareSpinSuccessView(ConfirmOutputData outputData) {
        updateState(outputData, false);
    }

    @Override
    public void prepareConfirmSuccessView(ConfirmOutputData outputData) {
        updateState(outputData, true);
    }

    @Override
    public void prepareFailureView(String message) {
        final ConfirmState state = confirmViewModel.getState();
        state.setErrorMessage(message);
        confirmViewModel.firePropertyChanged();
    }
}
