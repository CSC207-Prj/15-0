package entity;

import java.util.Objects;

/** Immutable result of one gauntlet fight. */
public final class FightResult {
    private final RealFighter opponent;
    private final boolean playerWon;
    private final FightMethod method;
    private final int round;
    private final int secondsInRound;

    public FightResult(RealFighter opponent,
                       boolean playerWon,
                       FightMethod method,
                       int round,
                       int secondsInRound) {
        this.opponent = Objects.requireNonNull(opponent, "opponent");
        this.method = Objects.requireNonNull(method, "method");

        if (round < 1 || round > 5) {
            throw new IllegalArgumentException("Round must be between 1 and 5.");
        }
        if (secondsInRound < 0 || secondsInRound > 300) {
            throw new IllegalArgumentException("Fight time must be between 0 and 300 seconds.");
        }

        this.playerWon = playerWon;
        this.round = round;
        this.secondsInRound = secondsInRound;
    }

    public RealFighter getOpponent() {
        return opponent;
    }

    public boolean isPlayerWon() {
        return playerWon;
    }

    public FightMethod getMethod() {
        return method;
    }

    public int getRound() {
        return round;
    }

    public int getSecondsInRound() {
        return secondsInRound;
    }
}
