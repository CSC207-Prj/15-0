package use_case.game_setting;

import entity.Difficulty;
import entity.UfcEra;

/**
 * Values sent from the controller to the game-setting interactor.
 */
public class GameSettingInputData {
    private final Difficulty difficulty;
    private final Integer roundsPerFight;
    private final UfcEra era;
    private final boolean hideOpponentStats;

    public GameSettingInputData(Difficulty difficulty,
                                Integer roundsPerFight,
                                UfcEra era,
                                boolean hideOpponentStats) {
        this.difficulty = difficulty;
        this.roundsPerFight = roundsPerFight;
        this.era = era;
        this.hideOpponentStats = hideOpponentStats;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Integer getRoundsPerFight() {
        return roundsPerFight;
    }

    public UfcEra getEra() {
        return era;
    }

    public boolean isHideOpponentStats() {
        return hideOpponentStats;
    }
}
