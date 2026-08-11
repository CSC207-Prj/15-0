package entity;

import java.util.Objects;

/**
 * Represents the immutable domain result of one simulated gauntlet fight.
 *
 * The result records the opponent, winner, finish method, ending round, and
 * elapsed seconds in that round.
 */
public final class FightResult {
    private static final int MIN_ROUND = 1;
    private static final int MAX_ROUND = 5;
    private static final int MAX_SECONDS_IN_ROUND = 300;

    private final RealFighter opponent;
    private final boolean playerWon;
    private final FightMethod method;
    private final int round;
    private final int secondsInRound;

    /**
     * Creates a validated fight result.
     *
     * @param opponent real fighter faced in the matchup
     * @param playerWon whether the custom fighter won
     * @param method method by which the fight ended
     * @param round round in which the fight ended
     * @param secondsInRound elapsed seconds in the ending round
     * @throws NullPointerException if opponent or method is {@code null}
     * @throws IllegalArgumentException if round or elapsed time is outside the
     * supported fight bounds
     */
    public FightResult(RealFighter opponent,
                       boolean playerWon,
                       FightMethod method,
                       int round,
                       int secondsInRound) {
        this.opponent = Objects.requireNonNull(opponent, "opponent");
        this.method = Objects.requireNonNull(method, "method");

        if (round < MIN_ROUND || round > MAX_ROUND) {
            throw new IllegalArgumentException("Round must be between 1 and 5.");
        }
        if (secondsInRound < 0 || secondsInRound > MAX_SECONDS_IN_ROUND) {
            throw new IllegalArgumentException("Fight time must be between 0 and 300 seconds.");
        }

        this.playerWon = playerWon;
        this.round = round;
        this.secondsInRound = secondsInRound;
    }

    /**
     * Returns the opponent associated with this result.
     *
     * @return opponent
     */
    public RealFighter getOpponent() {
        return opponent;
    }

    /**
     * Reports whether the custom fighter won.
     *
     * @return true when the player won
     */
    public boolean isPlayerWon() {
        return playerWon;
    }

    /**
     * Returns the method by which the fight ended.
     *
     * @return fight-ending method
     */
    public FightMethod getMethod() {
        return method;
    }

    /**
     * Returns the round in which the fight ended.
     *
     * @return ending round
     */
    public int getRound() {
        return round;
    }

    /**
     * Returns elapsed seconds in the ending round.
     *
     * @return elapsed seconds in the ending round
     */
    public int getSecondsInRound() {
        return secondsInRound;
    }
}
