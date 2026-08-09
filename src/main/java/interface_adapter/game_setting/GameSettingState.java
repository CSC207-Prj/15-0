package interface_adapter.game_setting;

import entity.CustomFighter;
import entity.GameSettings;
import entity.RealFighter;

import java.util.ArrayList;
import java.util.List;

/**
 * State produced by the Game Setting presenter.
 */
public class GameSettingState {

    private String errorMessage = "";
    private boolean configured;
    private GameSettings settings;
    private CustomFighter customFighter;
    private List<RealFighter> eligibleFighters = new ArrayList<>();

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isConfigured() {
        return configured;
    }

    public void setConfigured(boolean configured) {
        this.configured = configured;
    }

    public GameSettings getSettings() {
        return settings;
    }

    public void setSettings(GameSettings settings) {
        this.settings = settings;
    }

    public CustomFighter getCustomFighter() {
        return customFighter;
    }

    public void setCustomFighter(CustomFighter customFighter) {
        this.customFighter = customFighter;
    }

    public List<RealFighter> getEligibleFighters() {
        return new ArrayList<>(eligibleFighters);
    }

    public void setEligibleFighters(List<RealFighter> eligibleFighters) {
        this.eligibleFighters = new ArrayList<>(eligibleFighters);
    }
}
