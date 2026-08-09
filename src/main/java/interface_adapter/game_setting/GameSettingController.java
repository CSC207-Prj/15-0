package interface_adapter.game_setting;

import entity.Difficulty;
import entity.UfcEra;
import use_case.game_setting.GameSettingBoundary;
import use_case.game_setting.GameSettingInputData;

/**
 * Converts values from GameSettingsView into input data for the use case.
 */
public class GameSettingController {

    private final GameSettingBoundary gameSettingInteractor;

    public GameSettingController(GameSettingBoundary gameSettingInteractor) {
        this.gameSettingInteractor = gameSettingInteractor;
    }

    public void execute(Difficulty difficulty,
                        Integer roundsPerFight,
                        UfcEra era,
                        boolean hideOpponentStats) {

        final GameSettingInputData inputData =
                new GameSettingInputData(
                        difficulty,
                        roundsPerFight,
                        era,
                        hideOpponentStats
                );

        gameSettingInteractor.execute(inputData);
    }
}
