package entity;

import java.util.Objects;

/**
 * Implements the production fight-resolution strategy using six gameplay attributes.
 *
 * The algorithm computes weighted fighter scores, applies the selected difficulty
 * to the player score, clamps the win probability, chooses a fight-ending method,
 * and generates a valid round and time. The game-design weights can change without
 * modifying the simulation interactor because the use case depends on FightSimulator.
 */
public final class WeightedFightSimulator implements FightSimulator {
    private static final double MIN_WIN_PROBABILITY = 0.10;
    private static final double MAX_WIN_PROBABILITY = 0.90;
    private static final double BASE_WIN_PROBABILITY = 0.50;
    private static final double SCORE_PROBABILITY_SCALE = 120.0;
    private static final double STRIKING_WEIGHT = 0.25;
    private static final double DEFENSE_WEIGHT = 0.20;
    private static final double TAKEDOWN_WEIGHT = 0.18;
    private static final double CARDIO_WEIGHT = 0.17;
    private static final double REACH_WEIGHT = 0.10;
    private static final double HEIGHT_WEIGHT = 0.10;
    private static final double EASY_DIFFICULTY_BONUS = 8.0;
    private static final double HARD_DIFFICULTY_PENALTY = -8.0;
    private static final double DECISION_BASE_CHANCE = 0.25;
    private static final double CARDIO_DECISION_SCALE = 250.0;
    private static final double MIN_DECISION_CHANCE = 0.30;
    private static final double MAX_DECISION_CHANCE = 0.60;
    private static final double MIN_SUBMISSION_CHANCE = 0.20;
    private static final double MAX_SUBMISSION_CHANCE = 0.65;
    private static final int MIN_ROUNDS = 1;
    private static final int MAX_ROUNDS = 5;
    private static final int DECISION_SECONDS = 300;
    private static final int FINISH_SECOND_BOUND = 299;

    private final RandomSource randomSource;

    /**
     * Creates the weighted strategy with an injected source of randomness.
     *
     * @param randomSource abstraction used for probabilistic decisions
     * @throws NullPointerException if randomSource is null
     */
    public WeightedFightSimulator(RandomSource randomSource) {
        this.randomSource = Objects.requireNonNull(randomSource, "randomSource");
    }

    /**
     * Resolves one fight using weighted attributes and injected randomness.
     *
     * @param player custom fighter controlled by the user
     * @param opponent real fighter being faced
     * @param maxRounds maximum rounds permitted for the matchup
     * @param difficulty selected game difficulty
     * @return immutable simulated fight result
     * @throws NullPointerException if player, opponent, or difficulty is null
     * @throws IllegalArgumentException if maxRounds is outside one to five
     */
    @Override
    public FightResult simulate(CustomFighter player,
                                RealFighter opponent,
                                int maxRounds,
                                Difficulty difficulty) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(opponent, "opponent");
        Objects.requireNonNull(difficulty, "difficulty");

        if (maxRounds < MIN_ROUNDS || maxRounds > MAX_ROUNDS) {
            throw new IllegalArgumentException(
                    "Max rounds must be between 1 and 5.");
        }

        final double playerScore =
                fighterScore(player) + difficultyAdjustment(difficulty);
        final double opponentScore = fighterScore(opponent);

        final double probability = clamp(
                BASE_WIN_PROBABILITY
                        + (playerScore - opponentScore)
                        / SCORE_PROBABILITY_SCALE,
                MIN_WIN_PROBABILITY,
                MAX_WIN_PROBABILITY);

        final boolean playerWon =
                randomSource.nextDouble() < probability;

        final FightMethod method =
                chooseMethod(player, opponent);

        final FightResult result;

        if (method == FightMethod.DECISION) {
            result = new FightResult(
                    opponent,
                    playerWon,
                    method,
                    maxRounds,
                    DECISION_SECONDS);
        }
        else {
            final int round =
                    MIN_ROUNDS + randomSource.nextInt(maxRounds);

            final int seconds =
                    MIN_ROUNDS
                            + randomSource.nextInt(FINISH_SECOND_BOUND);

            result = new FightResult(
                    opponent,
                    playerWon,
                    method,
                    round,
                    seconds);
        }

        return result;
    }

    /**
     * Chooses decision, submission, or knockout using cardio and style signals.
     *
     * @param player fighter whose offensive style is used for finish selection
     * @param opponent opposing fighter used in the cardio calculation
     * @return selected fight-ending method
     */
    private FightMethod chooseMethod(Fighter player,
                                     Fighter opponent) {
        final double averageCardio =
                (player.getAttribute(Attribute.CARDIO)
                        + opponent.getAttribute(Attribute.CARDIO))
                        / 2.0;

        final double decisionChance = clamp(
                DECISION_BASE_CHANCE
                        + averageCardio / CARDIO_DECISION_SCALE,
                MIN_DECISION_CHANCE,
                MAX_DECISION_CHANCE);

        final FightMethod method;

        if (randomSource.nextDouble() < decisionChance) {
            method = FightMethod.DECISION;
        }
        else {
            final double takedown =
                    player.getAttribute(Attribute.TAKEDOWN);

            final double striking =
                    player.getAttribute(Attribute.STRIKING);

            final double submissionChance = clamp(
                    takedown
                            / Math.max(
                            MIN_ROUNDS,
                            takedown + striking),
                    MIN_SUBMISSION_CHANCE,
                    MAX_SUBMISSION_CHANCE);

            if (randomSource.nextDouble()
                    < submissionChance) {
                method = FightMethod.SUBMISSION;
            }
            else {
                method = FightMethod.KO_TKO;
            }
        }

        return method;
    }

    /**
     * Computes the weighted overall score used by the win-probability model.
     *
     * @param fighter fighter whose six gameplay attributes are scored
     * @return weighted gameplay score
     */
    private double fighterScore(Fighter fighter) {
        return fighter.getAttribute(Attribute.STRIKING)
                * STRIKING_WEIGHT
                + fighter.getAttribute(Attribute.DEFENSE)
                * DEFENSE_WEIGHT
                + fighter.getAttribute(Attribute.TAKEDOWN)
                * TAKEDOWN_WEIGHT
                + fighter.getAttribute(Attribute.CARDIO)
                * CARDIO_WEIGHT
                + fighter.getAttribute(Attribute.REACH)
                * REACH_WEIGHT
                + fighter.getAttribute(Attribute.HEIGHT)
                * HEIGHT_WEIGHT;
    }

    /**
     * Returns the player-score modifier associated with the selected difficulty.
     *
     * @param difficulty selected game difficulty
     * @return additive player-score adjustment
     */
    private double difficultyAdjustment(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> EASY_DIFFICULTY_BONUS;
            case NORMAL -> 0.0;
            case HARD -> HARD_DIFFICULTY_PENALTY;
            default ->
                    throw new IllegalStateException(
                            "Unsupported difficulty.");
        };
    }

    /**
     * Restricts a value to an inclusive numeric range.
     *
     * @param value value to restrict
     * @param minimum lower bound
     * @param maximum upper bound
     * @return bounded value
     */
    private static double clamp(double value,
                                double minimum,
                                double maximum) {
        return Math.max(
                minimum,
                Math.min(maximum, value));
    }
}
