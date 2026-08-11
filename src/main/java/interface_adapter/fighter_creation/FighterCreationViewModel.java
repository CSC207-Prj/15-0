package interface_adapter.fighter_creation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import entity.CustomFighter;
import entity.GameSettings;
import entity.RealFighter;
import interface_adapter.ViewModel;

/**
 * Stores the data displayed by the fighter creation view.
 */
public class FighterCreationViewModel extends ViewModel {

    private String fighterName = "";
    private String fighterDetails = "";
    private int overall;
    private RealFighter currentFighter;
    private String errorMessage = "";

    private final Map<String, Integer> fighterStats = new HashMap<>();
    private final Map<String, Integer> assignedValues = new HashMap<>();
    private final Map<String, String> assignedFighters = new HashMap<>();

    private int rerollsLeft;
    private boolean fighterRevealed;

    private GameSettings gameSettings;
    private CustomFighter customFighter;

    public FighterCreationViewModel() {
        super("Fighter Creation");
    }

    /**
     * Initializes the view model with the settings and blank fighter for a run.
     *
     * @param settings settings selected for the run
     * @param fighter blank custom fighter being built
     */
    public void initializeRun(GameSettings settings,
                              CustomFighter fighter) {
        this.gameSettings = settings;
        this.customFighter = fighter;

        startNewRun(
                settings.getDifficulty().getRerollLimit()
        );
    }

    /**
     * Clears drafting state and installs the run's initial reroll allowance.
     *
     * @param initialRerolls rerolls available at the start of the run
     */
    public void startNewRun(int initialRerolls) {
        fighterName = "";
        fighterDetails = "";
        overall = 0;
        currentFighter = null;
        errorMessage = "";

        fighterStats.clear();
        assignedValues.clear();
        assignedFighters.clear();

        rerollsLeft = initialRerolls;
        fighterRevealed = false;

        firePropertyChanged(null, this);
    }

    /**
     * Stores all display data for a newly rolled fighter.
     *
     * @param name fighter name
     * @param details fighter record and weight-class text
     * @param rating fighter's displayed overall rating
     * @param stats fighter's displayed attribute values
     */
    public void setRolledFighter(String name,
                                 String details,
                                 int rating,
                                 Map<String, Integer> stats) {
        this.fighterName = name;
        this.fighterDetails = details;
        this.overall = rating;

        fighterStats.clear();
        fighterStats.putAll(stats);

        fighterRevealed = true;
        firePropertyChanged(null, this);
    }

    /**
     * Stores name, details, and attributes for a newly rolled fighter.
     *
     * @param name fighter name
     * @param details fighter record and weight-class text
     * @param stats fighter's displayed attribute values
     */
    public void setRolledFighter(String name,
                                 String details,
                                 Map<String, Integer> stats) {
        this.fighterName = name;
        this.fighterDetails = details;

        fighterStats.clear();
        fighterStats.putAll(stats);

        fighterRevealed = true;
        firePropertyChanged(null, this);
    }

    /**
     * Records an assigned value and the real fighter that supplied it.
     *
     * @param attribute assigned attribute's display name
     * @param value rounded value displayed by the view
     * @param sourceFighterName real fighter that supplied the value
     */
    public void setAssignedAttribute(String attribute,
                                     int value,
                                     String sourceFighterName) {
        assignedValues.put(attribute, value);
        assignedFighters.put(attribute, sourceFighterName);
        firePropertyChanged(null, this);
    }

    /**
     * Updates the remaining reroll allowance.
     *
     * @param rerollsLeft number of rerolls still available
     */
    public void setRerollsLeft(int rerollsLeft) {
        this.rerollsLeft = rerollsLeft;
        firePropertyChanged(null, this);
    }

    public RealFighter getCurrentFighter() {
        return currentFighter;
    }

    public void setCurrentFighter(RealFighter currentFighter) {
        this.currentFighter = currentFighter;
    }

    public String getFighterName() {
        return fighterName;
    }

    public String getFighterDetails() {
        return fighterDetails;
    }

    public int getOverall() {
        return overall;
    }

    public Map<String, Integer> getFighterStats() {
        return Collections.unmodifiableMap(fighterStats);
    }

    public Map<String, Integer> getAssignedValues() {
        return Collections.unmodifiableMap(assignedValues);
    }

    public Map<String, String> getAssignedFighters() {
        return Collections.unmodifiableMap(assignedFighters);
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

    /**
     * Updates the validation message shown by the fighter-creation view.
     *
     * @param errorMessage message to display, or an empty string to clear it
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        firePropertyChanged(null, this);
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public CustomFighter getCustomFighter() {
        return customFighter;
    }
}
