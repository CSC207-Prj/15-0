package use_case.fighter_creation;

import java.util.List;

import entity.RealFighter;

/**
 * Provides fighter data needed by the fighter creation use cases.
 */
public interface FighterDataAccessInterface {

    /**
     * Retrieves the fighter catalogue available to the drafting use cases.
     *
     * @return all available real fighters
     */
    List<RealFighter> getFighters();
}
