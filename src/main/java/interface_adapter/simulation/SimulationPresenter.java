package interface_adapter.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import entity.Attribute;
import use_case.simulation.SimulationOutputBoundary;
import use_case.simulation.SimulationOutputData;

/**
 * Adapts raw simulation output into display-ready state for the Swing view.
 *
 * The presenter owns presentation policy such as record labels,
 * opponent-status rows, matchup capitalization, elapsed-time formatting,
 * and whether opponent statistics are displayed.
 */
public final class SimulationPresenter
        implements SimulationOutputBoundary {

    private static final int SECONDS_PER_MINUTE = 60;

    private final SimulationViewModel viewModel;

    /**
     * Creates a presenter that publishes state through the supplied view model.
     *
     * @param viewModel observable view model updated by this presenter
     * @throws NullPointerException if viewModel is null
     */
    public SimulationPresenter(
            SimulationViewModel viewModel) {

        this.viewModel =
                Objects.requireNonNull(
                        viewModel,
                        "viewModel");
    }

    /**
     * Formats successful use-case output and publishes a new simulation state.
     *
     * @param outputData raw output produced by the simulation interactor
     */
    @Override
    public void prepareSuccessView(
            SimulationOutputData outputData) {

        final List<String> opponentRows =
                outputData.getOpponents()
                        .stream()
                        .map(opponent ->
                                "#"
                                        + opponent.rank()
                                        + "  "
                                        + opponent.name()
                                        + "  •  "
                                        + opponent.status())
                        .toList();

        final List<String> historyRows =
                outputData.getFightHistory()
                        .stream()
                        .map(this::formatResult)
                        .toList();

        final String divisionText =
                outputData.getWeightClass().getDisplayName()
                        + " • 15 ranked opponents";

        final String recordText =
                "RECORD  "
                        + outputData.getWins()
                        + "-"
                        + outputData.getLosses();

        final String matchupText;
        final String statsText;

        if (outputData.isComplete()
                || outputData.getCurrentOpponent() == null) {

            matchupText = "GAUNTLET COMPLETE";
            statsText =
                    "All 15 ranked opponents have been fought.";
        }
        else {
            final SimulationOutputData.OpponentData current =
                    outputData.getCurrentOpponent();

            matchupText =
                    outputData.getPlayerName()
                            .toUpperCase(Locale.ROOT)
                            + "  vs  "
                            + current.name()
                            .toUpperCase(Locale.ROOT);

            if (outputData.isHideOpponentStats()) {
                statsText =
                        "Opponent stats hidden by run settings";
            }
            else {
                statsText =
                        formatAttributes(
                                current.attributes());
            }
        }

        viewModel.setState(
                new SimulationState(
                        divisionText,
                        recordText,
                        matchupText,
                        statsText,
                        opponentRows,
                        historyRows,
                        !outputData.isComplete(),
                        outputData.getMessage()));
    }

    /**
     * Publishes an error message while preserving the rest of the current state.
     *
     * @param errorMessage message describing the failed action
     */
    @Override
    public void prepareFailView(
            String errorMessage) {

        viewModel.setState(
                viewModel.getState()
                        .withStatusMessage(errorMessage));
    }

    /**
     * Formats one completed fight for the fight-history list.
     *
     * @param result raw completed-fight output
     * @return display string for the history row
     */
    private String formatResult(
            SimulationOutputData.ResultData result) {

        final String outcome;

        if (result.playerWon()) {
            outcome = "WIN";
        }
        else {
            outcome = "LOSS";
        }

        return "#"
                + result.rank()
                + " "
                + result.opponentName()
                + "  •  "
                + outcome
                + "  •  "
                + result.method().getDisplayName()
                + "  •  Round "
                + result.round()
                + "  •  "
                + formatTime(
                result.secondsInRound());
    }

    /**
     * Formats all available gameplay attributes for the current opponent.
     *
     * @param attributes opponent attributes keyed by gameplay attribute
     * @return display string containing the available attribute values
     */
    private String formatAttributes(
            Map<Attribute, Double> attributes) {

        final List<String> pieces =
                new ArrayList<>();

        for (Attribute attribute :
                Attribute.values()) {

            final Double value =
                    attributes.get(attribute);

            if (value != null) {
                pieces.add(
                        attribute.getDisplayName()
                                + " "
                                + Math.round(value));
            }
        }

        return String.join(
                "   |   ",
                pieces);
    }

    /**
     * Formats elapsed seconds as minutes and zero-padded seconds.
     *
     * @param seconds elapsed seconds in the ending round
     * @return time in m:ss form
     */
    private String formatTime(int seconds) {
        return String.format(
                Locale.ROOT,
                "%d:%02d",
                seconds / SECONDS_PER_MINUTE,
                seconds % SECONDS_PER_MINUTE);
    }
}
