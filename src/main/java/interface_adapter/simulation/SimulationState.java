package interface_adapter.simulation;

import java.util.List;

/**
 * Stores display-ready state consumed by the simulation Swing view.
 *
 * The state contains only values already adapted for presentation.
 * Keeping this separate from GameRun prevents the view from
 * becoming authoritative for domain state.
 */
public final class SimulationState {
    private final String divisionText;
    private final String recordText;
    private final String matchupText;
    private final String opponentStatsText;
    private final List<String> opponentRows;
    private final List<String> historyRows;
    private final boolean simulationEnabled;
    private final String statusMessage;

    /**
     * Creates an immutable snapshot of all values rendered by the simulation view.
     *
     * @param divisionText formatted division heading
     * @param recordText formatted player record
     * @param matchupText formatted current matchup
     * @param opponentStatsText formatted current-opponent statistics
     * @param opponentRows formatted ranked-opponent rows
     * @param historyRows formatted fight-history rows
     * @param simulationEnabled whether simulation actions are currently available
     * @param statusMessage feedback about the most recent action
     */
    public SimulationState(
            String divisionText,
            String recordText,
            String matchupText,
            String opponentStatsText,
            List<String> opponentRows,
            List<String> historyRows,
            boolean simulationEnabled,
            String statusMessage) {

        this.divisionText = divisionText;
        this.recordText = recordText;
        this.matchupText = matchupText;
        this.opponentStatsText =
                opponentStatsText;

        this.opponentRows =
                List.copyOf(opponentRows);

        this.historyRows =
                List.copyOf(historyRows);

        this.simulationEnabled =
                simulationEnabled;

        this.statusMessage =
                statusMessage;
    }

    /**
     * Creates the state shown when no active gauntlet has been loaded.
     *
     * @return empty simulation state
     */
    public static SimulationState empty() {
        return new SimulationState(
                "No active division",
                "RECORD  0-0",
                "No active matchup",
                "",
                List.of(),
                List.of(),
                false,
                "Finalize a fighter to start the gauntlet.");
    }

    /**
     * Copies this state while replacing only its status message.
     *
     * @param message replacement status message
     * @return state containing the replacement message
     */
    public SimulationState withStatusMessage(
            String message) {

        return new SimulationState(
                divisionText,
                recordText,
                matchupText,
                opponentStatsText,
                opponentRows,
                historyRows,
                simulationEnabled,
                message);
    }

    /**
     * Returns the formatted division heading.
     *
     * @return formatted division heading
     */
    public String getDivisionText() {
        return divisionText;
    }

    /**
     * Returns the formatted player record.
     *
     * @return formatted player record
     */
    public String getRecordText() {
        return recordText;
    }

    /**
     * Returns the formatted current matchup.
     *
     * @return formatted matchup text
     */
    public String getMatchupText() {
        return matchupText;
    }

    /**
     * Returns the formatted current-opponent statistics.
     *
     * @return formatted opponent-statistics text
     */
    public String getOpponentStatsText() {
        return opponentStatsText;
    }

    /**
     * Returns the formatted ranked-opponent rows.
     *
     * @return immutable list of opponent rows
     */
    public List<String> getOpponentRows() {
        return opponentRows;
    }

    /**
     * Returns the formatted fight-history rows.
     *
     * @return immutable list of history rows
     */
    public List<String> getHistoryRows() {
        return historyRows;
    }

    /**
     * Reports whether simulation controls should be enabled.
     *
     * @return true when simulation is available
     */
    public boolean isSimulationEnabled() {
        return simulationEnabled;
    }

    /**
     * Returns the latest user-facing status message.
     *
     * @return status message
     */
    public String getStatusMessage() {
        return statusMessage;
    }
}
