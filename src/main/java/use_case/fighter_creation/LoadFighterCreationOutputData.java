package use_case.fighter_creation;

import entity.CustomFighter;
import entity.GameSettings;

/**
 * Data needed when Fighter Creation is entered after US1.
 */
public class LoadFighterCreationOutputData {
    private final GameSettings settings;
    private final CustomFighter customFighter;

    public LoadFighterCreationOutputData(GameSettings settings,
                                         CustomFighter customFighter) {
        this.settings = settings;
        this.customFighter = customFighter;
    }

    public GameSettings getSettings() {
        return settings;
    }

    public CustomFighter getCustomFighter() {
        return customFighter;
    }
}
