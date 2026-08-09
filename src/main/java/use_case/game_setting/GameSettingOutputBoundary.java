package use_case.game_setting;

/**
 * The interactor uses this interface instead of talking directly to Swing.
 */
public interface GameSettingOutputBoundary {
    void prepareSuccessView(GameSettingOutputData outputData);

    void prepareFailView(String errorMessage);
}
