package interface_adapter.saved_fighters;

/**
 * One row of the saved-fighters roster, already formatted for display.
 */
public class SavedFighterRow {
    private final String name;
    private final String weightClassName;
    private final String recordText;
    private final int finishes;

    public SavedFighterRow(String name, String weightClassName, String recordText, int finishes) {
        this.name = name;
        this.weightClassName = weightClassName;
        this.recordText = recordText;
        this.finishes = finishes;
    }

    public String getName() {
        return name;
    }

    public String getWeightClassName() {
        return weightClassName;
    }

    public String getRecordText() {
        return recordText;
    }

    public int getFinishes() {
        return finishes;
    }
}
