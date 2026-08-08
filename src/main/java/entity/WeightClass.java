package entity;

/** UFC weight classes supported by the game. */
public enum WeightClass {
    FLYWEIGHT("Flyweight"),
    BANTAMWEIGHT("Bantamweight"),
    FEATHERWEIGHT("Featherweight"),
    LIGHTWEIGHT("Lightweight"),
    WELTERWEIGHT("Welterweight"),
    MIDDLEWEIGHT("Middleweight"),
    LIGHT_HEAVYWEIGHT("Light Heavyweight"),
    HEAVYWEIGHT("Heavyweight");

    private final String displayName;

    WeightClass(String displayName) {
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
