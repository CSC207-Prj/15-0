package use_case.fighter_creation;

import entity.RealFighter;

import java.util.List;

/**
 * Provides fighter data needed by the fighter creation use cases.
 */
public interface FighterDataAccessInterface {

    List<RealFighter> getFighters();
}