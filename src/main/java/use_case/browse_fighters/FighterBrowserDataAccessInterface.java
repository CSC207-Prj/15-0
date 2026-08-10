package use_case.browse_fighters;

import entity.RealFighter;

import java.util.List;

/**
 * Provides the real-fighter catalogue used by the Fighter Browser.
 */
public interface FighterBrowserDataAccessInterface {
    List<RealFighter> getFighters();
}
