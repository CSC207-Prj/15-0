package entity;

/** Difficulty choices available to a game run. */
public enum Difficulty {
    EASY(3),
    NORMAL(1),
    HARD(0);

    private final int rerollLimit;

    Difficulty(int rerollLimit) {
        this.rerollLimit = rerollLimit;
    }

    public int getRerollLimit() {
        return rerollLimit;
    }
}