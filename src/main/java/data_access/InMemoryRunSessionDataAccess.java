package data_access;

import entity.CustomFighter;
import entity.GameSettings;
import entity.RealFighter;
import use_case.fighter_creation.FighterCreationSessionDataAccessInterface;
import use_case.game_setting.GameSettingSessionDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared in-memory session for one active run.
 *
 * US1 writes the configured settings, draft fighter, and eligible fighter pool.
 * US2 reads that same state through its own use-case boundary.
 */
public class InMemoryRunSessionDataAccess
        implements GameSettingSessionDataAccessInterface,
        FighterCreationSessionDataAccessInterface {

    private GameSettings gameSettings;
    private CustomFighter customFighter;
    private List<RealFighter> eligibleFighters = new ArrayList<>();

    @Override
    public void saveConfiguredRun(GameSettings settings,
                                  CustomFighter customFighter,
                                  List<RealFighter> eligibleFighters) {
        this.gameSettings = settings;
        this.customFighter = customFighter;
        this.eligibleFighters = new ArrayList<>(eligibleFighters);
    }

    @Override
    public GameSettings getGameSettings() {
        return gameSettings;
    }

    @Override
    public CustomFighter getCustomFighter() {
        return customFighter;
    }

    @Override
    public List<RealFighter> getFighters() {
        return new ArrayList<>(eligibleFighters);
    }

    @Override
    public boolean hasConfiguredRun() {
        return gameSettings != null
                && customFighter != null
                && !eligibleFighters.isEmpty();
    }
}
