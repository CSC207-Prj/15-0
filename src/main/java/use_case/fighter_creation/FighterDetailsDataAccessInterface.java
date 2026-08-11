package use_case.fighter_creation;

import entity.RealFighter;

/**
 * Optional data-access boundary for loading the full profile/stat data of one
 * fighter after that fighter has been selected by the wheel.
 */
public interface FighterDetailsDataAccessInterface {
    /**
     * Loads the complete details for a selected fighter.
     *
     * @param fighter fighter selected from the catalogue
     * @return the selected fighter with its available details populated
     */
    RealFighter getFighterDetails(RealFighter fighter);
}
