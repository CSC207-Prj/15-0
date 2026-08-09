package interface_adapter.game_setting;

import use_case.game_setting.GameSettingOutputBoundary;
import use_case.game_setting.GameSettingOutputData;

/**
 * Converts use-case output into state used by the Swing view.
 */
public class GameSettingPresenter implements GameSettingOutputBoundary {
    private final GameSettingViewModel viewModel;

    public GameSettingPresenter(GameSettingViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(GameSettingOutputData outputData) {
        GameSettingState state = viewModel.getState();

        state.setConfigured(true);
        state.setErrorMessage("");
        state.setSettings(outputData.getSettings());
        state.setCustomFighter(outputData.getCustomFighter());
        state.setEligibleFighters(outputData.getEligibleFighters());

        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        GameSettingState state = viewModel.getState();

        state.setConfigured(false);
        state.setErrorMessage(errorMessage);

        viewModel.firePropertyChanged();
    }
}
