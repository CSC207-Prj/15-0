package use_case.simulation;

import java.util.List;
import java.util.Map;

import entity.Attribute;
import entity.FightMethod;
import entity.WeightClass;

/**
 * Carries raw gauntlet data from the simulation interactor to the output boundary.
 *
 * The values are intentionally presentation-neutral. Formatting such as
 * record labels, matchup text, time strings, and hidden-stat messages belongs
 * to the interface-adapter presenter rather than to the use case.
 */
public final class SimulationOutputData {
    /**
     * Describes the player's progress relative to one ranked opponent.
     */
    public enum OpponentStatus {
        /** The player already defeated this opponent. */
        WIN,

        /** The player already lost to this opponent. */
        LOSS,

        /** This opponent is the next scheduled matchup. */
        NEXT,

        /** This opponent has not yet been reached. */
        PENDING
    }

    /**
     * Immutable output representation of one ranked opponent.
     *
     * @param rank UFC ranking used by the gauntlet
     * @param name opponent display name
     * @param attributes opponent gameplay attributes
     * @param status player's current progress relative to the opponent
     */
    public record OpponentData(
            int rank,
            String name,
            Map<Attribute, Double> attributes,
            OpponentStatus status) {

        /**
         * Defensively copies the attribute map supplied to the record.
         */
        public OpponentData {
            attributes =
                    Map.copyOf(attributes);
        }
    }

    /**
     * Immutable output representation of one completed fight.
     *
     * @param rank opponent rank
     * @param opponentName opponent display name
     * @param playerWon whether the custom fighter won
     * @param method method by which the fight ended
     * @param round round in which the fight ended
     * @param secondsInRound elapsed seconds in the ending round
     */
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

    /**
     * Creates a complete raw snapshot of the simulation use case.
     *
     * @param playerName custom fighter name
     * @param weightClass active division weight class
     * @param wins current number of player wins
     * @param losses current number of player losses
     * @param hideOpponentStats whether the run hides current-opponent statistics
     * @param complete whether all fifteen opponents have been processed
     * @param currentOpponent next opponent, or {@code null} when complete
     * @param opponents all gauntlet opponents with progress statuses
     * @param fightHistory completed fight results
     * @param message status message for the most recent action
     */
    public SimulationOutputData(
            String playerName,
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
        this.hideOpponentStats =
                hideOpponentStats;
        this.complete = complete;
        this.currentOpponent =
                currentOpponent;

        this.opponents =
                List.copyOf(opponents);

        this.fightHistory =
                List.copyOf(fightHistory);

        this.message = message;
    }

    /**
     * Returns the custom fighter name.
     *
     * @return player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Returns the active division weight class.
     *
     * @return weight class
     */
    public WeightClass getWeightClass() {
        return weightClass;
    }

    /**
     * Returns the player's current win total.
     *
     * @return number of wins
     */
    public int getWins() {
        return wins;
    }

    /**
     * Returns the player's current loss total.
     *
     * @return number of losses
     */
    public int getLosses() {
        return losses;
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
     * Reports whether all fifteen fights are complete.
     *
     * @return true when the gauntlet is complete
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Returns the next opponent to fight.
     *
     * @return next opponent, or null when the run is complete
     */
    public OpponentData getCurrentOpponent() {
        return currentOpponent;
    }

    /**
     * Returns all ranked opponents and their progress statuses.
     *
     * @return immutable list of opponent output data
     */
    public List<OpponentData> getOpponents() {
        return opponents;
    }

    /**
     * Returns every completed fight in chronological order.
     *
     * @return immutable list of completed fight output data
     */
    public List<ResultData> getFightHistory() {
        return fightHistory;
    }

    /**
     * Returns the status message associated with this snapshot.
     *
     * @return status message
     */
    public String getMessage() {
        return message;
    }
}
