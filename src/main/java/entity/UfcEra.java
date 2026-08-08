package entity;

/** UFC era categories shared by configuration and real-fighter catalogue features. */
public enum UfcEra {
    ALL_TIME("All Time"),
    EARLY_UFC("Early UFC (1993-2004)"),
    ZUFFA_ERA("Zuffa Era (2005-2015)"),
    MODERN("Modern Era (2016-present)");

    private final String displayName;

    UfcEra(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
