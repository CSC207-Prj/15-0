package entity;

/**
 * How a fight was won, as defined in the project blueprint.
 */
public enum FightMethod {
    KO("KO"),
    SUBMISSION("Submission"),
    DECISION("Decision");

    private final String displayName;

    FightMethod(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the human-readable form of this method for result text.
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
