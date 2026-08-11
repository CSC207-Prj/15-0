package entity;

/**
 * Stores a fighter's domain-level win, loss, and finish totals.
 *
 * User Story 4 updates this entity through GameRun.recordResult(FightResult)
 * after every simulated fight.
 */
public final class FighterRecord {
    private int wins;
    private int losses;
    private int finishes;

    /**
     * Creates an empty fighter record.
     */
    public FighterRecord() {
        this(0, 0, 0);
    }

    /**
     * Creates a validated fighter record.
     *
     * @param wins number of wins
     * @param losses number of losses
     * @param finishes number of wins by finish
     * @throws IllegalArgumentException if any count is negative or finishes exceed wins
     */
    public FighterRecord(int wins, int losses, int finishes) {
        if (wins < 0 || losses < 0 || finishes < 0 || finishes > wins) {
            throw new IllegalArgumentException("Invalid fighter record.");
        }
        this.wins = wins;
        this.losses = losses;
        this.finishes = finishes;
    }

    /**
     * Returns the number of wins.
     *
     * @return number of wins
     */
    public int getWins() {
        return wins;
    }

    /**
     * Returns the number of losses.
     *
     * @return number of losses
     */
    public int getLosses() {
        return losses;
    }

    /**
     * Returns the number of wins by finish.
     *
     * @return number of finishes
     */
    public int getFinishes() {
        return finishes;
    }

    /**
     * Returns the total number of completed fights.
     *
     * @return wins plus losses
     */
    public int getTotalFights() {
        return wins + losses;
    }

    /**
     * Records a win and optionally increments the finish total.
     *
     * @param finish true when the win was by knockout or submission
     */
    public void registerWin(boolean finish) {
        wins++;
        if (finish) {
            finishes++;
        }
    }

    /**
     * Records one loss for this fighter.
     */
    public void registerLoss() {
        losses++;
    }

    /**
     * Creates an independent copy of this record.
     *
     * @return copied fighter record
     */
    public FighterRecord copy() {
        return new FighterRecord(wins, losses, finishes);
    }

    /**
     * Formats the record as wins followed by losses.
     *
     * @return record in wins-losses form
     */
    @Override
    public String toString() {
        return wins + "-" + losses;
    }
}
