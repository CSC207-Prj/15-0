package entity;

import java.util.Objects;

/**
 * An immutable win-loss record for a fighter, including how many of the wins
 * were finishes (KO or submission). Used by the roster ranking tie-breaker.
 */
public final class FighterRecord {
    private final int wins;
    private final int losses;
    private final int finishes;

    /**
     * Creates a record.
     * @param wins number of wins
     * @param losses number of losses
     * @param finishes number of wins that were finishes (KO or submission); at most wins
     */
    public FighterRecord(int wins, int losses, int finishes) {
        this.wins = wins;
        this.losses = losses;
        this.finishes = finishes;
    }

    /**
     * Returns a fresh 0-0 record.
     * @return an empty record
     */
    public static FighterRecord empty() {
        return new FighterRecord(0, 0, 0);
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

    /**
     * Returns a new record with one more win added.
     * @param wasFinish true if the win came by KO or submission
     * @return the updated record
     */
    public FighterRecord withWin(boolean wasFinish) {
        return new FighterRecord(wins + 1, losses, wasFinish ? finishes + 1 : finishes);
    }

    /**
     * Returns a new record with one more loss added.
     * @return the updated record
     */
    public FighterRecord withLoss() {
        return new FighterRecord(wins, losses + 1, finishes);
    }

    /**
     * Formats this record the way it is shown on screen, for example "15-0".
     * @return the record as W-L text
     */
    public String asText() {
        return wins + "-" + losses;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof FighterRecord)) {
            return false;
        }
        final FighterRecord that = (FighterRecord) other;
        return wins == that.wins && losses == that.losses && finishes == that.finishes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(wins, losses, finishes);
    }

    @Override
    public String toString() {
        return asText() + " (" + finishes + " finishes)";
    }
}
