package interface_adapter.fighter_creation;

import interface_adapter.ViewModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores the data displayed by the fighter creation view.
 */
public class FighterCreationViewModel extends ViewModel {

    public FighterCreationViewModel() {
        super("Fighter Creation");
    }

    private String fighterName = "";
    private String fighterDetails = "";
    private int overall;

    private final Map<String, Integer> fighterStats = new HashMap<>();
    private final Map<String, Integer> assignedValues = new HashMap<>();
    private final Map<String, String> assignedFighters = new HashMap<>();

    private int rerollsLeft;
    private boolean fighterRevealed;

    public void setRolledFighter(String name, String details,
                                 int overall, Map<String, Integer> stats) {
        this.fighterName = name;
        this.fighterDetails = details;
        this.overall = overall;

        fighterStats.clear();
        fighterStats.putAll(stats);

        fighterRevealed = true;
        firePropertyChanged(null, this);
    }

    public void setAssignedAttribute(String attribute, int value,
                                     String fighterName) {
        assignedValues.put(attribute, value);
        assignedFighters.put(attribute, fighterName);
        firePropertyChanged(null, this);
    }

    public void setRerollsLeft(int rerollsLeft) {
        this.rerollsLeft = rerollsLeft;
        firePropertyChanged(null, this);
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
}