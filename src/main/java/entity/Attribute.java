package entity;

/** The six core gameplay attributes shared by real and custom fighters. */
public enum Attribute {
    STRIKING("Striking"),
    DEFENSE("Defense"),
    TAKEDOWNS("Takedowns"),
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
}