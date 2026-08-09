package interface_adapter.simulation;

import java.util.List;

/** Display-ready state consumed by SimulationView. */
public final class SimulationState {
    private final String divisionText;
    private final String recordText;
    private final String matchupText;
    private final String opponentStatsText;
    private final List<String> opponentRows;
    private final List<String> historyRows;
    private final boolean simulationEnabled;
    private final String statusMessage;

    public SimulationState(String divisionText,
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
        this.opponentStatsText = opponentStatsText;
        this.opponentRows = List.copyOf(opponentRows);
        this.historyRows = List.copyOf(historyRows);
        this.simulationEnabled = simulationEnabled;
        this.statusMessage = statusMessage;
    }

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

    public SimulationState withStatusMessage(String message) {
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

    public String getDivisionText() {
        return divisionText;
    }

    public String getRecordText() {
        return recordText;
    }

    public String getMatchupText() {
        return matchupText;
    }

    public String getOpponentStatsText() {
        return opponentStatsText;
    }

    public List<String> getOpponentRows() {
        return opponentRows;
    }

    public List<String> getHistoryRows() {
        return historyRows;
    }

    public boolean isSimulationEnabled() {
        return simulationEnabled;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
