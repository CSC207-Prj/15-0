package entity;

/**
 * Stores the settings for one run.
 *
 * It deliberately does not contain Swing code. This object should still make
 * sense even if the project later changes the UI.
 */
public class GameSettings {
    private final Difficulty difficulty;
    private final Integer roundsPerFight;
    private final UfcEra era;
    private final boolean hideOpponentStats;

    public GameSettings(Difficulty difficulty,
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


    public boolean isValid() {
        if (difficulty == null || roundsPerFight == null || era == null) {
            return false;
        }

        return roundsPerFight == 1 || roundsPerFight == 3 || roundsPerFight == 5;
    }
}
