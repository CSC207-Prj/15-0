package entity;

/** Methods by which a simulated UFC fight can end. */
public enum FightMethod {
    KO_TKO("KO/TKO"),
    SUBMISSION("Submission"),
    DECISION("Decision");

    private final String displayName;

    FightMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isFinish() {
        return this != DECISION;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
