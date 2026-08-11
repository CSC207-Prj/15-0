package entity;

/**
 * The six attributes used to build a fighter.
 */
public enum Attribute {
    STRIKING("Striking"),
    DEFENSE("Defense"),
    TAKEDOWN("Takedown"),
    HEIGHT("Height"),
    REACH("Reach"),
    CARDIO("Cardio");

    private final String displayName;

    Attribute(String displayName) {
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
