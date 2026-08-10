package interface_adapter.simulation;

import entity.Attribute;
import entity.FightMethod;
import entity.WeightClass;
import org.junit.jupiter.api.Test;
import use_case.simulation.SimulationInputData;
import use_case.simulation.SimulationOutputData;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimulationAdaptersTest {

    @Test
    void controllerForwardsAllThreeActionsAndRejectsNullBoundary() {
        final java.util.ArrayList<SimulationInputData.Action> actions =
                new java.util.ArrayList<>();
        final SimulationController controller =
                new SimulationController(input -> actions.add(input.getAction()));
        controller.loadRun();
        controller.simulateNextFight();
        controller.autoSimulateRun();

        assertEquals(List.of(
                SimulationInputData.Action.LOAD,
                SimulationInputData.Action.SIMULATE_NEXT,
                SimulationInputData.Action.AUTO_SIMULATE), actions);
        assertThrows(NullPointerException.class,
                () -> new SimulationController(null));
    }

    @Test
    void presenterFormatsVisibleMatchupOpponentsAndHistory() {
        final SimulationViewModel viewModel = new SimulationViewModel();
        final Map<Attribute, Double> attributes = attributes();
        final SimulationOutputData.OpponentData current =
                new SimulationOutputData.OpponentData(
                        15, "Opponent", attributes,
                        SimulationOutputData.OpponentStatus.NEXT);
        final SimulationOutputData output = new SimulationOutputData(
                "Player", WeightClass.LIGHTWEIGHT, 2, 1,
                false, false, current,
                List.of(current),
                List.of(
                        new SimulationOutputData.ResultData(
                                15, "First", true, FightMethod.KO_TKO, 2, 65),
                        new SimulationOutputData.ResultData(
                                14, "Second", false, FightMethod.DECISION, 3, 300)),
                "Ready");

        new SimulationPresenter(viewModel).prepareSuccessView(output);

        final SimulationState state = viewModel.getState();
        assertEquals("Lightweight • 15 ranked opponents",
                state.getDivisionText());
        assertEquals("RECORD  2-1", state.getRecordText());
        assertEquals("PLAYER  vs  OPPONENT", state.getMatchupText());
        assertTrue(state.getOpponentStatsText().contains("Striking 81"));
        assertEquals("#15  Opponent  •  NEXT",
                state.getOpponentRows().get(0));
        assertTrue(state.getHistoryRows().get(0).contains("WIN"));
        assertTrue(state.getHistoryRows().get(0).contains("1:05"));
        assertTrue(state.getHistoryRows().get(1).contains("LOSS"));
        assertTrue(state.isSimulationEnabled());
        assertEquals("Ready", state.getStatusMessage());
    }

    @Test
    void presenterFormatsHiddenStatsCompletionAndFailure() {
        final SimulationViewModel viewModel = new SimulationViewModel();
        final SimulationPresenter presenter = new SimulationPresenter(viewModel);
        final SimulationOutputData.OpponentData current =
                new SimulationOutputData.OpponentData(
                        15, "Opponent", attributes(),
                        SimulationOutputData.OpponentStatus.NEXT);

        presenter.prepareSuccessView(new SimulationOutputData(
                "Player", WeightClass.WELTERWEIGHT, 0, 0,
                true, false, current, List.of(), List.of(), "Hidden"));
        assertEquals("Opponent stats hidden by run settings",
                viewModel.getState().getOpponentStatsText());

        presenter.prepareSuccessView(new SimulationOutputData(
                "Player", WeightClass.WELTERWEIGHT, 10, 5,
                false, true, null, List.of(), List.of(), "Done"));
        assertEquals("GAUNTLET COMPLETE", viewModel.getState().getMatchupText());
        assertEquals("All 15 ranked opponents have been fought.",
                viewModel.getState().getOpponentStatsText());
        assertFalse(viewModel.getState().isSimulationEnabled());

        presenter.prepareFailView("No active run");
        assertEquals("No active run", viewModel.getState().getStatusMessage());
        assertThrows(NullPointerException.class,
                () -> new SimulationPresenter(null));
    }

    @Test
    void stateAndViewModelPreserveImmutableState() {
        final SimulationState empty = SimulationState.empty();
        assertEquals("No active division", empty.getDivisionText());
        assertEquals("RECORD  0-0", empty.getRecordText());
        assertEquals("No active matchup", empty.getMatchupText());
        assertEquals("", empty.getOpponentStatsText());
        assertTrue(empty.getOpponentRows().isEmpty());
        assertTrue(empty.getHistoryRows().isEmpty());
        assertFalse(empty.isSimulationEnabled());

        final SimulationState updated = empty.withStatusMessage("updated");
        assertEquals("updated", updated.getStatusMessage());
        assertEquals(empty.getDivisionText(), updated.getDivisionText());

        final SimulationViewModel viewModel = new SimulationViewModel();
        final int[] events = {0};
        viewModel.addPropertyChangeListener(event -> events[0]++);
        viewModel.setState(updated);
        assertEquals(updated, viewModel.getState());
        assertEquals(1, events[0]);
        assertThrows(UnsupportedOperationException.class,
                () -> updated.getOpponentRows().add("bad"));
    }

    private static Map<Attribute, Double> attributes() {
        final Map<Attribute, Double> values =
                new EnumMap<>(Attribute.class);
        values.put(Attribute.STRIKING, 80.6);
        values.put(Attribute.DEFENSE, 79.4);
        return values;
    }
}
