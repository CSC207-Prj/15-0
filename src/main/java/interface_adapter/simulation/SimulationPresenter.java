package interface_adapter.simulation;

import entity.Attribute;
import use_case.simulation.SimulationOutputBoundary;
import use_case.simulation.SimulationOutputData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Converts raw Simulation output into strings and values suitable for Swing.
 */
public final class SimulationPresenter implements SimulationOutputBoundary {
    private final SimulationViewModel viewModel;

    public SimulationPresenter(SimulationViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
    }

    @Override
    public void prepareSuccessView(SimulationOutputData outputData) {
        final List<String> opponentRows = outputData.getOpponents().stream()
                .map(opponent -> "#" + opponent.rank()
                        + "  " + opponent.name()
                        + "  •  " + opponent.status())
                .toList();

        final List<String> historyRows = outputData.getFightHistory().stream()
                .map(this::formatResult)
                .toList();

        final String divisionText =
                outputData.getWeightClass().getDisplayName() + " • 15 ranked opponents";
        final String recordText = "RECORD  " + outputData.getWins() + "-" + outputData.getLosses();

        final String matchupText;
        final String statsText;
        if (outputData.isComplete() || outputData.getCurrentOpponent() == null) {
            matchupText = "GAUNTLET COMPLETE";
            statsText = "All 15 ranked opponents have been fought.";
        }
        else {
            final SimulationOutputData.OpponentData current = outputData.getCurrentOpponent();
            matchupText = outputData.getPlayerName().toUpperCase(Locale.ROOT)
                    + "  vs  "
                    + current.name().toUpperCase(Locale.ROOT);

            statsText = outputData.isHideOpponentStats()
                    ? "Opponent stats hidden by run settings"
                    : formatAttributes(current.attributes());
        }

        viewModel.setState(new SimulationState(
                divisionText,
                recordText,
                matchupText,
                statsText,
                opponentRows,
                historyRows,
                !outputData.isComplete(),
                outputData.getMessage()));
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setState(viewModel.getState().withStatusMessage(errorMessage));
    }

    private String formatResult(SimulationOutputData.ResultData result) {
        final String outcome = result.playerWon() ? "WIN" : "LOSS";
        return "#" + result.rank()
                + " " + result.opponentName()
                + "  •  " + outcome
                + "  •  " + result.method().getDisplayName()
                + "  •  Round " + result.round()
                + "  •  " + formatTime(result.secondsInRound());
    }

    private String formatAttributes(Map<Attribute, Double> attributes) {
        final List<String> pieces = new ArrayList<>();
        for (Attribute attribute : Attribute.values()) {
            final Double value = attributes.get(attribute);
            if (value != null) {
                pieces.add(attribute.getDisplayName() + " "
                        + Math.round(value));
            }
        }
        return String.join("   |   ", pieces);
    }

    private String formatTime(int seconds) {
        return String.format(Locale.ROOT, "%d:%02d", seconds / 60, seconds % 60);
    }
}
