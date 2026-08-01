package entity;

/**
 * The outcome of one simulated fight, as defined in the project blueprint:
 * who won, how they won, which round it ended in, and the finish time.
 * Immutable value class.
 */
public final class FightResult {
    private final Fighter winner;
    private final FightMethod method;
    private final int round;
    private final String finishTime;

    /**
     * Creates a fight result.
     * @param winner the fighter who won
     * @param method how the fight was won
     * @param round the round the fight ended in
     * @param finishTime the time in the round the fight ended, for example "2:31";
     *                   may be empty for decisions
     */
    public FightResult(Fighter winner, FightMethod method, int round, String finishTime) {
        this.winner = winner;
        this.method = method;
        this.round = round;
        this.finishTime = finishTime;
    }

    public Fighter getWinner() {
        return winner;
    }

    public FightMethod getMethod() {
        return method;
    }

    public int getRound() {
        return round;
    }

    public String getFinishTime() {
        return finishTime;
    }
}
