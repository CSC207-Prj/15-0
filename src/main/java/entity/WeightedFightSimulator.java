package entity;

import java.util.Objects;

/**
 * Fight simulation strategy using all six current gameplay attributes.
 *
 * The exact weights are game-design values and can be tuned without changing
 * the Simulation use case.
 */
public final class WeightedFightSimulator implements FightSimulator {
    private static final double MIN_WIN_PROBABILITY = 0.10;
    private static final double MAX_WIN_PROBABILITY = 0.90;

    private final RandomSource randomSource;

    public WeightedFightSimulator(RandomSource randomSource) {
        this.randomSource = Objects.requireNonNull(randomSource, "randomSource");
    }

    @Override
    public FightResult simulate(CustomFighter player,
                                RealFighter opponent,
                                int maxRounds,
                                Difficulty difficulty) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(opponent, "opponent");
        Objects.requireNonNull(difficulty, "difficulty");

        if (maxRounds < 1 || maxRounds > 5) {
            throw new IllegalArgumentException("Max rounds must be between 1 and 5.");
        }

        final double playerScore = fighterScore(player) + difficultyAdjustment(difficulty);
        final double opponentScore = fighterScore(opponent);
        final double probability = clamp(
                0.50 + (playerScore - opponentScore) / 120.0,
                MIN_WIN_PROBABILITY,
                MAX_WIN_PROBABILITY);

        final boolean playerWon = randomSource.nextDouble() < probability;
        final FightMethod method = chooseMethod(player, opponent);

        if (method == FightMethod.DECISION) {
            return new FightResult(opponent, playerWon, method, maxRounds, 300);
        }

        final int round = 1 + randomSource.nextInt(maxRounds);
        final int seconds = 1 + randomSource.nextInt(299);
        return new FightResult(opponent, playerWon, method, round, seconds);
    }

    private FightMethod chooseMethod(Fighter player, Fighter opponent) {
        final double averageCardio =
                (player.getAttribute(Attribute.CARDIO) + opponent.getAttribute(Attribute.CARDIO)) / 2.0;

        // Better cardio modestly increases the chance that a fight reaches a decision.
        final double decisionChance = clamp(0.25 + averageCardio / 250.0, 0.30, 0.60);
        if (randomSource.nextDouble() < decisionChance) {
            return FightMethod.DECISION;
        }

        // With the current six-stat model, TAKEDOWN represents the strongest
        // available signal for a submission-oriented finish.
        final double takedown = player.getAttribute(Attribute.TAKEDOWN);
        final double striking = player.getAttribute(Attribute.STRIKING);
        final double submissionChance = clamp(
                takedown / Math.max(1.0, takedown + striking),
                0.20,
                0.65);

        return randomSource.nextDouble() < submissionChance
                ? FightMethod.SUBMISSION
                : FightMethod.KO_TKO;
    }

    private double fighterScore(Fighter fighter) {
        return fighter.getAttribute(Attribute.STRIKING) * 0.25
                + fighter.getAttribute(Attribute.DEFENSE) * 0.20
                + fighter.getAttribute(Attribute.TAKEDOWN) * 0.18
                + fighter.getAttribute(Attribute.CARDIO) * 0.17
                + fighter.getAttribute(Attribute.REACH) * 0.10
                + fighter.getAttribute(Attribute.HEIGHT) * 0.10;
    }

    private double difficultyAdjustment(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 8.0;
            case NORMAL -> 0.0;
            case HARD -> -8.0;
        };
    }

    private static double clamp(double value, double minimum, double maximum) {
        return Math.max(minimum, Math.min(maximum, value));
    }
}
