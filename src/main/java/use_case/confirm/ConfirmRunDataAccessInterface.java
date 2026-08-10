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

    CustomFighter getCustomFighter();

    GameSettings getGameSettings();

    Division getDivision(WeightClass weightClass);

    void saveGameRun(GameRun gameRun);
}
