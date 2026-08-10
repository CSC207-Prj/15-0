package use_case.fighter_creation;

import entity.CustomFighter;
import entity.GameSettings;

/**
 * Read boundary used by Fighter Creation to load the run configured in US1.
 */
public interface FighterCreationSessionDataAccessInterface
        extends FighterDataAccessInterface {

    GameSettings getGameSettings();

    CustomFighter getCustomFighter();

    boolean hasConfiguredRun();
}
