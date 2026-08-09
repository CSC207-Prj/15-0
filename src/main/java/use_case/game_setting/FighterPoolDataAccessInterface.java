package use_case.game_setting;

import entity.RealFighter;
import entity.UfcEra;

import java.util.List;

/**
 * Data access boundary used by the game-setting use case.

 */
public interface FighterPoolDataAccessInterface {

    /**
     * Returns the fighters that can appear on the attribute wheel
     * for the selected era.
     */
    List<RealFighter> getEligibleFighters(UfcEra era);
}
