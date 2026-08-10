package interface_adapter.fighter_creation;

import entity.Attribute;
import entity.CustomFighter;
import entity.GameSettings;
import entity.RealFighter;
import interface_adapter.ViewModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores the data displayed by the Fighter Creation view.
 */
public class FighterCreationViewModel extends ViewModel {

    private String fighterName = "";
    private String fighterDetails = "";
    private RealFighter currentFighter;
    private String errorMessage = "";

    private final Map<String, Integer> fighterStats =
            new HashMap<>();
    private final Map<String, Integer> assignedValues =
            new HashMap<>();
    private final Map<String, String> assignedFighters =
            new HashMap<>();

    private int rerollsLeft;
    private boolean fighterRevealed;

    private GameSettings gameSettings;
    private CustomFighter customFighter;

    public FighterCreationViewModel() {
        super("Fighter Creation");
    }

    /**
     * Initializes US2 from the configured state created by US1.
     */
    public void initialize(GameSettings settings,
                           CustomFighter fighter) {
        this.gameSettings = settings;
        this.customFighter = fighter;
        this.rerollsLeft =
                settings.getDifficulty().getRerollLimit();

        assignedValues.clear();
        assignedFighters.clear();

        for (Attribute attribute : Attribute.values()) {
            if (fighter.hasAttribute(attribute)) {
                assignedValues.put(
                        attribute.getDisplayName(),
                        (int) Math.round(
                                fighter.getAttribute(attribute)
                        )
                );

                final String source =
                        fighter.getAttributeSource(attribute);
                if (source != null) {
                    assignedFighters.put(
                            attribute.getDisplayName(),
                            source
                    );
                }
            }
        }

        clearCurrentFighterInternal();
        errorMessage = "";
        firePropertyChanged(null, this);
    }

    public void setRolledFighter(
            String name,
            String details,
            Map<String, Integer> stats) {
        fighterName = name;
        fighterDetails = details;

        fighterStats.clear();
        fighterStats.putAll(stats);

        fighterRevealed = true;
        firePropertyChanged(null, this);
    }

    public void setAssignedAttribute(
            String attribute,
            int value,
            String sourceFighterName) {
        assignedValues.put(attribute, value);
        assignedFighters.put(
                attribute,
                sourceFighterName
        );

        // A source fighter disappears as soon as one attribute is taken.
        clearCurrentFighterInternal();
        firePropertyChanged(null, this);
    }

    public void setRerollsLeft(int rerollsLeft) {
        this.rerollsLeft = rerollsLeft;
        firePropertyChanged(null, this);
    }

    public RealFighter getCurrentFighter() {
        return currentFighter;
    }

    public void setCurrentFighter(
            RealFighter currentFighter) {
        this.currentFighter = currentFighter;
    }

    public void clearCurrentFighter() {
        clearCurrentFighterInternal();
        firePropertyChanged(null, this);
    }

    private void clearCurrentFighterInternal() {
        currentFighter = null;
        fighterName = "";
        fighterDetails = "";
        fighterStats.clear();
        fighterRevealed = false;
    }

    public String getFighterName() {
        return fighterName;
    }

    public String getFighterDetails() {
        return fighterDetails;
    }

    public Map<String, Integer> getFighterStats() {
        return Collections.unmodifiableMap(
                fighterStats
        );
    }

    public Map<String, Integer> getAssignedValues() {
        return Collections.unmodifiableMap(
                assignedValues
        );
    }

    public Map<String, String> getAssignedFighters() {
        return Collections.unmodifiableMap(
                assignedFighters
        );
    }

    public int getAttributesFilled() {
        return assignedValues.size();
    }

    public int getRerollsLeft() {
        return rerollsLeft;
    }

    public boolean isFighterRevealed() {
        return fighterRevealed;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage =
                errorMessage == null ? "" : errorMessage;
        firePropertyChanged(null, this);
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public CustomFighter getCustomFighter() {
        return customFighter;
    }
}
