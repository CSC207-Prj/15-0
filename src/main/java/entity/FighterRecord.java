package entity;

/** Basic win/loss/finish data shared by fighters. */
public final class FighterRecord {
    private int wins;
    private int losses;
    private int finishes;

    public FighterRecord() {
        this(0, 0, 0);
    }

    public FighterRecord(int wins, int losses, int finishes) {
        if (wins < 0 || losses < 0 || finishes < 0 || finishes > wins) {
            throw new IllegalArgumentException("Invalid fighter record.");
        }
        this.wins = wins;
        this.losses = losses;
        this.finishes = finishes;
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

    public int getTotalFights() {
        return wins + losses;
    }

    /**
     * Records a win for this fighter.
     *
     * @param finish true when the win was by KO/TKO or submission
     */
    public void registerWin(boolean finish) {
        wins++;
        if (finish) {
            finishes++;
        }
    }

    /** Records a loss for this fighter. */
    public void registerLoss() {
        losses++;
    }

    public FighterRecord copy() {
        return new FighterRecord(wins, losses, finishes);
    }

    @Override
    public String toString() {
        return wins + "-" + losses;
    }
}
