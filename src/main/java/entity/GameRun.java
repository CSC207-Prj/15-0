package entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Entity representing one complete 15-fight gauntlet.
 *
 * A loss never ends the run. Completion occurs only after all 15 opponents
 * have been fought.
 */
public final class GameRun {
    public static final int GAUNTLET_SIZE = 15;

    private final CustomFighter player;
    private final Division division;
    private final Difficulty difficulty;
    private final int roundsPerFight;
    private final boolean hideOpponentStats;
    private final List<RealFighter> opponents;
    private final List<FightResult> fightHistory = new ArrayList<>();

    private int currentOpponentIndex;

    public GameRun(CustomFighter player,
                   Division division,
                   Difficulty difficulty,
                   int roundsPerFight,
                   boolean hideOpponentStats) {
        this.player = Objects.requireNonNull(player, "player");
        this.division = Objects.requireNonNull(division, "division");
        this.difficulty = Objects.requireNonNull(difficulty, "difficulty");

        if (roundsPerFight < 1 || roundsPerFight > 5) {
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

    public CustomFighter getPlayer() {
        return player;
    }

    public Division getDivision() {
        return division;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getRoundsPerFight() {
        return roundsPerFight;
    }

    public boolean isHideOpponentStats() {
        return hideOpponentStats;
    }

    /**
     * Returns opponents in gauntlet order: rank 15, 14, ..., 1.
     */
    public List<RealFighter> getOpponents() {
        return opponents;
    }

    public List<FightResult> getFightHistory() {
        return List.copyOf(fightHistory);
    }

    public RealFighter getCurrentOpponent() {
        return isComplete() ? null : opponents.get(currentOpponentIndex);
    }

    public int getFightsCompleted() {
        return fightHistory.size();
    }

    public boolean isComplete() {
        return currentOpponentIndex >= opponents.size();
    }

    /**
     * Records the result of the current matchup and moves to the next rank.
     * Losses deliberately do not terminate the gauntlet.
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

    private static List<RealFighter> buildGauntletOrder(List<RealFighter> rankedFighters) {
        Objects.requireNonNull(rankedFighters, "rankedFighters");

        final List<RealFighter> topFifteen = rankedFighters.stream()
                .filter(fighter -> fighter.getRank() >= 1 && fighter.getRank() <= GAUNTLET_SIZE)
                .sorted(Comparator.comparingInt(RealFighter::getRank).reversed())
                .toList();

        if (topFifteen.size() != GAUNTLET_SIZE) {
            throw new IllegalArgumentException("A gauntlet requires exactly ranks 1 through 15.");
        }

        final Set<Integer> ranks = new HashSet<>();
        for (RealFighter fighter : topFifteen) {
            ranks.add(fighter.getRank());
        }

        if (ranks.size() != GAUNTLET_SIZE
                || !ranks.contains(1)
                || !ranks.contains(GAUNTLET_SIZE)) {
            throw new IllegalArgumentException("Division rankings must contain unique ranks 1 through 15.");
        }

        return List.copyOf(topFifteen);
    }
}
