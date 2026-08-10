package use_case.browse_fighters;

import entity.RealFighter;

import java.util.List;

/**
 * Provides the real-fighter catalogue used by the Fighter Browser.
 */
public interface FighterBrowserDataAccessInterface {
    List<RealFighter> getFighters();

    /**
     * Loads the most complete available profile for one selected fighter.
     * Local/in-memory implementations can simply return the catalogue entry.
     */
    default RealFighter getFighterDetails(RealFighter fighter) {
        return fighter;
    }
}
