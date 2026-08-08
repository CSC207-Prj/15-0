package entity;

/** Basic win/loss/finish data shared by fighters. Fight-update behaviour is added later. */
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

    public FighterRecord copy() {
        return new FighterRecord(wins, losses, finishes);
    }

    @Override
    public String toString() {
        return wins + "-" + losses;
    }
}
