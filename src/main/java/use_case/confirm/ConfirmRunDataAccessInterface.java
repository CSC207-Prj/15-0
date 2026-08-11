package use_case.confirm;

import entity.CustomFighter;
import entity.Division;
import entity.GameRun;
import entity.GameSettings;
import entity.WeightClass;

/**
 * Data-access boundary used by US3 to finalize a fighter into a gauntlet run.
 */
public interface ConfirmRunDataAccessInterface {

    /**
     * Gets the custom fighter being confirmed.
     *
     * @return the custom fighter
     */
    CustomFighter getCustomFighter();

    /**
     * Gets the settings for the current game.
     *
     * @return the current game settings
     */
    GameSettings getGameSettings();

    /**
     * Gets the division for a weight class.
     *
     * @param weightClass the requested weight class
     * @return the matching division
     */
    Division getDivision(WeightClass weightClass);

    /**
     * Saves the newly created gauntlet run.
     *
     * @param gameRun the gauntlet run to save
     */
    void saveGameRun(GameRun gameRun);
}
