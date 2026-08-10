package app;

import interface_adapter.game_setting.GameSettingController;
import interface_adapter.game_setting.GameSettingPresenter;
import interface_adapter.game_setting.GameSettingViewModel;
import use_case.fighter_creation.FighterDataAccessInterface;
import use_case.game_setting.GameSettingBoundary;
import use_case.game_setting.GameSettingInteractor;
import use_case.game_setting.GameSettingOutputBoundary;
import view.GameSettingsView;

/**
 * Wires together the Configure a New Run use case.
 */
public final class GameSettingUseCaseFactory {

    private GameSettingUseCaseFactory() {
    }

    public static GameSettingsView create(
            FighterDataAccessInterface fighterDataAccess,
            GameSettingViewModel viewModel,
            Runnable backAction,
            Runnable continueAction) {

        final GameSettingOutputBoundary presenter =
                new GameSettingPresenter(viewModel);

        final GameSettingBoundary interactor =
                new GameSettingInteractor(
                        fighterDataAccess,
                        presenter
                );

        final GameSettingController controller =
                new GameSettingController(interactor);

        return new GameSettingsView(
                controller,
                backAction,
                continueAction
        );
    }
}