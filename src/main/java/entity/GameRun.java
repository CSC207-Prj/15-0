package entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Represents one complete fifteen-fight gauntlet as a domain entity.
 *
 * The entity owns the invariants that define a valid run: the player and
 * division have to agree on the weight class, the opponent list has to contain unique
 * ranks one through fifteen, results must match the current opponent, and a loss
 * advances the run just like a win. Completion only happens after every opponent
 * has been processed.
 */
public final class GameRun {
    /** Number of ranked opponents in a complete gauntlet. */
    public static final int GAUNTLET_SIZE = 15;
    private static final int MIN_RANK = 1;
    private static final int MAX_ROUNDS_PER_FIGHT = 5;

    private final CustomFighter player;
    private final Division division;
    private final Difficulty difficulty;
    private final int roundsPerFight;
    private final boolean hideOpponentStats;
    private final List<RealFighter> opponents;
    private final List<FightResult> fightHistory = new ArrayList<>();

    private int currentOpponentIndex;

    /**
     * Creates a gauntlet from a finalized player and a ranked division.
     *
     * @param player finalized custom fighter entering the gauntlet
     * @param division division whose ranked fighters form the opponent list
     * @param difficulty difficulty used by the fight-simulation strategy
     * @param roundsPerFight maximum rounds for each fight
     * @param hideOpponentStats whether the presentation should hide opponent stats
     * @throws NullPointerException if player, division, or difficulty is {@code null}
     * @throws IllegalArgumentException if rounds are invalid, the player's weight
     * class does not match the division, or rankings do not contain unique ranks one through fifteen
     */
    public GameRun(CustomFighter player,
                   Division division, Difficulty difficulty,
                   int roundsPerFight, boolean hideOpponentStats) {
        this.player = Objects.requireNonNull(player, "player");
        this.division = Objects.requireNonNull(division, "division");
        this.difficulty = Objects.requireNonNull(difficulty, "difficulty");

        if (roundsPerFight < MIN_RANK || roundsPerFight > MAX_ROUNDS_PER_FIGHT) {
            throw new IllegalArgumentException("Rounds per fight must be between 1 and 5.");
        }

        if (player.getWeightClass() == null
                || player.getWeightClass() != division.getWeightClass()) {
            throw new IllegalArgumentException(
                    "The player's weight class must match the selected division.");
        }

        this.roundsPerFight = roundsPerFight;
        this.hideOpponentStats = hideOpponentStats;
        this.opponents = buildGauntletOrder(division.getRankedFighters());
    }

    /**
     * Returns the custom fighter controlled by the player.
     *
     * @return custom fighter
     */
    public CustomFighter getPlayer() {
        return player;
    }

    /**
     * Returns the division used for this run.
     *
     * @return active division
     */
    public Division getDivision() {
        return division;
    }

    /**
     * Returns the selected run difficulty.
     *
     * @return difficulty
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the maximum number of rounds per fight.
     *
     * @return maximum rounds per fight
     */
    public int getRoundsPerFight() {
        return roundsPerFight;
    }

    /**
     * Reports whether opponent statistics should be hidden.
     *
     * @return true when opponent statistics are hidden
     */
    public boolean isHideOpponentStats() {
        return hideOpponentStats;
    }

    /**
     * Returns opponents in gauntlet order from rank fifteen down to rank one.
     *
     * @return immutable opponent list in fight order
     */
    public List<RealFighter> getOpponents() {
        return opponents;
    }

    /**
     * Returns completed results in chronological order.
     *
     * @return immutable fight history
     */
    public List<FightResult> getFightHistory() {
        return List.copyOf(fightHistory);
    }

    /**
     * Returns the opponent scheduled for the next fight.
     *
     * @return current opponent, or null when complete
     */
    public RealFighter getCurrentOpponent() {
        final RealFighter currentOpponent;
        if (isComplete()) {
            currentOpponent = null;
        }
        else {
            currentOpponent = opponents.get(currentOpponentIndex);
        }
        return currentOpponent;
    }

    /**
     * Returns the number of fights already completed.
     *
     * @return number of completed fights
     */
    public int getFightsCompleted() {
        return fightHistory.size();
    }

    /**
     * Reports whether every ranked opponent has been processed.
     *
     * @return true after all fifteen fights are complete
     */
    public boolean isComplete() {
        return currentOpponentIndex >= opponents.size();
    }

    /**
     * Records the current matchup result and advances to the next ranked opponent.
     *
     * The player's FighterRecord is updated for either a win or a loss,
     * and the opponent index advances in both cases. This is the domain rule that
     * keeps the gauntlet running after a loss.
     *
     * @param result result produced for the current opponent
     * @throws NullPointerException if {@code result} is {@code null}
     * @throws IllegalStateException if the gauntlet is already complete
     * @throws IllegalArgumentException if the result does not correspond to the current opponent
     */
    public void recordResult(FightResult result) {
        Objects.requireNonNull(result, "result");

        if (isComplete()) {
            throw new IllegalStateException("The gauntlet is already complete.");
        }

        final RealFighter expectedOpponent = getCurrentOpponent();
        final RealFighter actualOpponent = result.getOpponent();
        if (expectedOpponent.getRank() != actualOpponent.getRank()
                || !expectedOpponent.getName().equals(actualOpponent.getName())) {
            throw new IllegalArgumentException("Result does not match the current opponent.");
        }

        fightHistory.add(result);
        if (result.isPlayerWon()) {
            player.getRecord().registerWin(result.getMethod().isFinish());
        }
        else {
            player.getRecord().registerLoss();
        }

        currentOpponentIndex++;
    }

    /**
     * Validates division rankings and creates the rank-fifteen-to-rank-one order.
     *
     * @param rankedFighters ranked fighters supplied by the division
     * @return immutable list ordered from rank fifteen down to rank one
     */
    private static List<RealFighter> buildGauntletOrder(List<RealFighter> rankedFighters) {
        Objects.requireNonNull(rankedFighters, "rankedFighters");

        final List<RealFighter> topFifteen = rankedFighters.stream()
                .filter(fighter -> fighter.getRank() >= MIN_RANK
                        && fighter.getRank() <= GAUNTLET_SIZE)
                .sorted(Comparator.comparingInt(RealFighter::getRank).reversed())
                .toList();

        if (topFifteen.size() != GAUNTLET_SIZE) {
            throw new IllegalArgumentException(
                    "A gauntlet requires exactly ranks 1 through 15.");
        }

        final boolean[] seenRanks = new boolean[GAUNTLET_SIZE + MIN_RANK];
        for (RealFighter fighter : topFifteen) {
            final int rank = fighter.getRank();
            if (seenRanks[rank]) {
                throw new IllegalArgumentException(
                        "Division rankings must contain unique ranks 1 through 15.");
            }
            seenRanks[rank] = true;
        }

        return List.copyOf(topFifteen);
    }
}
