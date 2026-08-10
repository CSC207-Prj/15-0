package use_case.game_setting;

import entity.CustomFighter;
import entity.GameSettings;
import entity.RealFighter;

import java.util.List;

/**
 * Stores the successfully configured run so the next user story can use it.
 */
public interface GameSettingSessionDataAccessInterface {
    void saveConfiguredRun(GameSettings settings,
                           CustomFighter customFighter,
                           List<RealFighter> eligibleFighters);
}
