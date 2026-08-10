package interface_adapter.confirm_fighter;

import org.junit.jupiter.api.Test;
import use_case.confirm.ConfirmInputBoundary;
import use_case.confirm.ConfirmInputData;
import use_case.confirm.ConfirmOutputData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfirmAdaptersTest {

    @Test
    void controllerForwardsSpinAndConfirmInputs() {
        final ConfirmInputData[] spin = {null};
        final ConfirmInputData[] confirm = {null};
        final ConfirmInputBoundary boundary = new ConfirmInputBoundary() {
            @Override
            public void spin(ConfirmInputData inputData) {
                spin[0] = inputData;
            }

            @Override
            public void confirm(ConfirmInputData inputData) {
                confirm[0] = inputData;
            }
        };
        final ConfirmController controller = new ConfirmController(boundary);

        controller.spin("Alpha", List.of("1", "2"), "Lightweight");
        controller.confirm("Bravo", List.of("3", "4"), "Welterweight");

        assertEquals("Alpha", spin[0].getFighterName());
        assertEquals("Lightweight", spin[0].getWeightClass());
        assertEquals("Bravo", confirm[0].getFighterName());
        assertEquals(List.of("3", "4"), confirm[0].getAttributePoints());
    }

    @Test
    void presenterMapsSpinConfirmAndFailureStates() {
        final ConfirmViewModel viewModel = new ConfirmViewModel();
        final int[] events = {0};
        viewModel.addPropertyChangeListener(event -> events[0]++);
        final ConfirmPresenter presenter = new ConfirmPresenter(viewModel);
        final ConfirmOutputData output = new ConfirmOutputData(
                "Alpha", List.of("1", "2", "3", "4", "5", "6"),
                "Lightweight", 77);

        presenter.prepareSpinSuccessView(output);
        assertEquals("Alpha", viewModel.getState().getFighterName());
        assertEquals("77", viewModel.getState().getOverall());
        assertTrue(viewModel.getState().isWeightClassLocked());
        assertFalse(viewModel.getState().isConfirmed());
        assertNull(viewModel.getState().getErrorMessage());

        presenter.prepareConfirmSuccessView(output);
        assertTrue(viewModel.getState().isConfirmed());

        presenter.prepareFailureView("bad input");
        assertEquals("bad input", viewModel.getState().getErrorMessage());
        assertEquals(3, events[0]);
    }

    @Test
    void stateAndViewModelApplyDefaultsAndDefensiveCopies() {
        final ConfirmState state = new ConfirmState();
        assertEquals(List.of("TBD", "TBD", "TBD", "TBD", "TBD", "TBD"),
                state.getAttributePoints());

        final java.util.ArrayList<String> values =
                new java.util.ArrayList<>(List.of("1", "2"));
        state.setAttributePoints(values);
        values.clear();
        assertEquals(List.of("1", "2"), state.getAttributePoints());
        state.setAttributePoints(null);
        assertEquals(6, state.getAttributePoints().size());

        state.setFighterName(null);
        assertEquals("", state.getFighterName());
        state.setWeightClass("Heavyweight");
        state.setOverall("91");
        state.setWeightClassLocked(true);
        state.setConfirmed(true);
        assertEquals("Heavyweight", state.getWeightClass());
        assertEquals("91", state.getOverall());
        assertTrue(state.isWeightClassLocked());
        assertTrue(state.isConfirmed());

        final ConfirmViewModel viewModel = new ConfirmViewModel();
        viewModel.setState(state);
        assertEquals(state, viewModel.getState());
        viewModel.setState(null);
        assertEquals("", viewModel.getState().getFighterName());
        assertEquals(ConfirmViewModel.VIEW_NAME, viewModel.getViewName());
    }
}
