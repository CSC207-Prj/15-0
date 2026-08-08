package entity;

/** The six core gameplay attributes shared by real and custom fighters. */
public enum Attribute {
    STRIKING("Striking"),
    GRAPPLING("Grappling"),
    TAKEDOWNS("Takedowns"),
    TAKEDOWN_DEFENSE("Takedown Defense"),
    CARDIO("Cardio"),
    DURABILITY("Durability");

    private final String displayName;

    Attribute(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
