package use_case.game_setting;

import entity.CustomFighter;
import entity.GameSettings;
import entity.RealFighter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Output produced after a new run has been configured successfully.
 */
public class GameSettingOutputData {
    private final GameSettings settings;
    private final CustomFighter customFighter;
    private final List<RealFighter> eligibleFighters;

    public GameSettingOutputData(GameSettings settings,
                                 CustomFighter customFighter,
                                 List<RealFighter> eligibleFighters) {
        this.settings = settings;
        this.customFighter = customFighter;
        this.eligibleFighters = new ArrayList<>(eligibleFighters);
    }

    public GameSettings getSettings() {
        return settings;
    }

    public CustomFighter getCustomFighter() {
        return customFighter;
    }

    public List<RealFighter> getEligibleFighters() {
        return Collections.unmodifiableList(eligibleFighters);
    }
}
