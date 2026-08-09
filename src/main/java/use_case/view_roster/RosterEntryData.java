package use_case.view_roster;

/**
 * One saved fighter's summary as it crosses the output boundary. Plain values
 * only, so the presenter never needs to touch entity objects.
 */
public class RosterEntryData {
    private final String name;
    private final String weightClassName;
    private final int wins;
    private final int losses;
    private final int finishes;
    private final String recordText;

    public RosterEntryData(String name, String weightClassName, int wins, int losses,
                           int finishes, String recordText) {
        this.name = name;
        this.weightClassName = weightClassName;
        this.wins = wins;
        this.losses = losses;
        this.finishes = finishes;
        this.recordText = recordText;
    }

    public String getName() {
        return name;
    }

    public String getWeightClassName() {
        return weightClassName;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getFinishes() {
        return finishes;
    }

    public String getRecordText() {
        return recordText;
    }
}
