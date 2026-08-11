package use_case.fighter_creation;

import entity.RealFighter;

/**
 * Optional data-access boundary for loading the full profile/stat data of one
 * fighter after that fighter has been selected by the wheel.
 */
public interface FighterDetailsDataAccessInterface {
    RealFighter getFighterDetails(RealFighter fighter);
}
