package use_case.simulation;

import entity.Attribute;
import entity.FightMethod;
import entity.WeightClass;

import java.util.List;
import java.util.Map;

/**
 * Raw use-case output. Formatting for Swing belongs in the presenter.
 */
public final class SimulationOutputData {
    public enum OpponentStatus {
        WIN,
        LOSS,
        NEXT,
        PENDING
    }

    public record OpponentData(
            int rank,
            String name,
            Map<Attribute, Double> attributes,
            OpponentStatus status) {
        public OpponentData {
            attributes = Map.copyOf(attributes);
        }
    }

    public record ResultData(
            int rank,
            String opponentName,
            boolean playerWon,
            FightMethod method,
            int round,
            int secondsInRound) {
    }

    private final String playerName;
    private final WeightClass weightClass;
    private final int wins;
    private final int losses;
    private final boolean hideOpponentStats;
    private final boolean complete;
    private final OpponentData currentOpponent;
    private final List<OpponentData> opponents;
    private final List<ResultData> fightHistory;
    private final String message;

    public SimulationOutputData(String playerName,
                                WeightClass weightClass,
                                int wins,
                                int losses,
                                boolean hideOpponentStats,
                                boolean complete,
                                OpponentData currentOpponent,
                                List<OpponentData> opponents,
                                List<ResultData> fightHistory,
                                String message) {
        this.playerName = playerName;
        this.weightClass = weightClass;
        this.wins = wins;
        this.losses = losses;
        this.hideOpponentStats = hideOpponentStats;
        this.complete = complete;
        this.currentOpponent = currentOpponent;
        this.opponents = List.copyOf(opponents);
        this.fightHistory = List.copyOf(fightHistory);
        this.message = message;
    }

    public String getPlayerName() {
        return playerName;
    }

    public WeightClass getWeightClass() {
        return weightClass;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public boolean isHideOpponentStats() {
        return hideOpponentStats;
    }

    public boolean isComplete() {
        return complete;
    }

    public OpponentData getCurrentOpponent() {
        return currentOpponent;
    }

    public List<OpponentData> getOpponents() {
        return opponents;
    }

    public List<ResultData> getFightHistory() {
        return fightHistory;
    }

    public String getMessage() {
        return message;
    }
}
