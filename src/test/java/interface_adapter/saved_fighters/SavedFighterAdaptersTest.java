package interface_adapter.saved_fighters;

import entity.CustomFighter;
import org.junit.jupiter.api.Test;
import use_case.delete_fighter.DeleteFighterInputData;
import use_case.delete_fighter.DeleteFighterOutputData;
import use_case.exhibition.ExhibitionInputData;
import use_case.load_fighter.LoadFighterInputData;
import use_case.load_fighter.LoadFighterOutputData;
import use_case.save_fighter.SaveFighterInputData;
import use_case.save_fighter.SaveFighterOutputData;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SavedFighterAdaptersTest {

    @Test
    void saveDeleteAndLoadPresentersMapSuccessFailureAndDuplicate() {
        final SavedFightersViewModel viewModel = new SavedFightersViewModel();
        final int[] events = {0};
        viewModel.addPropertyChangeListener(event -> events[0]++);

        final SaveFighterPresenter save = new SaveFighterPresenter(viewModel);
        save.prepareSuccessView(new SaveFighterOutputData("Alpha", false));
        assertEquals("Saved \"Alpha\" to your roster.",
                viewModel.getState().getMessage());
        assertEquals("", viewModel.getState().getError());
        save.prepareFailView("cannot save");
        assertEquals("", viewModel.getState().getMessage());
        assertEquals("cannot save", viewModel.getState().getError());
        final CustomFighter pending = new CustomFighter("Alpha");
        save.prepareDuplicateNameView(
                new SaveFighterOutputData("Alpha", true, pending));
        assertSame(pending, viewModel.getState().getDuplicatePending());
        assertTrue(viewModel.getState().getError().contains("different name"));

        final DeleteFighterPresenter delete =
                new DeleteFighterPresenter(viewModel);
        delete.prepareSuccessView(new DeleteFighterOutputData("Alpha", false));
        assertEquals("Deleted \"Alpha\" from your roster.",
                viewModel.getState().getMessage());
        delete.prepareFailView("cannot delete");
        assertEquals("cannot delete", viewModel.getState().getError());
        assertEquals("", viewModel.getState().getMessage());

        final LinkedHashMap<String, Double> values = new LinkedHashMap<>();
        values.put("Striking", 89.6);
        values.put("Cardio", 80.2);
        final LoadFighterPresenter load = new LoadFighterPresenter(viewModel);
        load.prepareSuccessView(new LoadFighterOutputData(
                "Alpha", "Lightweight", "2-1", 1, values, false));
        assertEquals(
                "Alpha (Lightweight, 2-1, 1 finishes) — Striking 90, Cardio 80",
                viewModel.getState().getLoadedFighterDetails());
        load.prepareFailView("cannot load");
        assertEquals("cannot load", viewModel.getState().getError());
        assertEquals("", viewModel.getState().getLoadedFighterDetails());
        assertEquals(7, events[0]);
    }

    @Test
    void allSavedFighterControllersForwardInputData() {
        final SaveFighterInputData[] save = {null};
        final CustomFighter fighter = new CustomFighter("Alpha");
        new SaveFighterController(input -> save[0] = input).execute(fighter);
        assertSame(fighter, save[0].getFighter());

        final DeleteFighterInputData[] delete = {null};
        new DeleteFighterController(input -> delete[0] = input).execute("Alpha");
        assertEquals("Alpha", delete[0].getFighterName());

        final LoadFighterInputData[] load = {null};
        new LoadFighterController(input -> load[0] = input).execute("Bravo");
        assertEquals("Bravo", load[0].getFighterName());

        final ExhibitionInputData[] exhibition = {null};
        new ExhibitionController(input -> exhibition[0] = input)
                .execute("Alpha", "Bravo");
        assertEquals("Alpha", exhibition[0].getFighterAName());
        assertEquals("Bravo", exhibition[0].getFighterBName());

        final boolean[] roster = {false};
        new ViewRosterController(() -> roster[0] = true).execute();
        assertTrue(roster[0]);
    }

    @Test
    void stateAndViewModelUseDefensiveCopiesAndNullDefaults() {
        final SavedFightersState state = new SavedFightersState();
        final SavedFighterRow row =
                new SavedFighterRow("Alpha", "Lightweight", "1-0", 1);
        final java.util.ArrayList<SavedFighterRow> rows =
                new java.util.ArrayList<>(List.of(row));
        state.setRows(rows);
        state.setTopThree(rows);
        rows.clear();
        assertEquals(1, state.getRows().size());
        assertEquals(1, state.getTopThree().size());
        state.getRows().clear();
        assertEquals(1, state.getRows().size());

        state.setLoadedFighterDetails(null);
        state.setExhibitionResult(null);
        state.setMessage(null);
        state.setError(null);
        assertEquals("", state.getLoadedFighterDetails());
        assertEquals("", state.getExhibitionResult());
        assertEquals("", state.getMessage());
        assertEquals("", state.getError());
        assertNull(state.getDuplicatePending());

        assertEquals("Alpha", row.getName());
        assertEquals("Lightweight", row.getWeightClassName());
        assertEquals("1-0", row.getRecordText());
        assertEquals(1, row.getFinishes());

        final SavedFightersViewModel viewModel = new SavedFightersViewModel();
        viewModel.setState(state);
        assertSame(state, viewModel.getState());
        viewModel.setState(null);
        assertTrue(viewModel.getState().getRows().isEmpty());
        assertEquals(SavedFightersViewModel.VIEW_NAME, viewModel.getViewName());
    }
}
