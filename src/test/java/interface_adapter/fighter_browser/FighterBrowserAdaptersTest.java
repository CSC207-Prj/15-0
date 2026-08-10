package interface_adapter.fighter_browser;

import entity.Attribute;
import entity.UfcEra;
import entity.WeightClass;
import org.junit.jupiter.api.Test;
import use_case.browse_fighters.BrowseFightersInputData;
import use_case.browse_fighters.BrowseFightersOutputData;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FighterBrowserAdaptersTest {

    @Test
    void controllerBuildsInputsForLoadFilterAndSelection() {
        final BrowseFightersInputData[] captured = {null};
        final FighterBrowserController controller =
                new FighterBrowserController(input -> captured[0] = input);

        controller.load();
        assertEquals("", captured[0].getSearchText());
        assertEquals(UfcEra.ALL_TIME, captured[0].getEra());

        controller.filter("max", WeightClass.FEATHERWEIGHT, UfcEra.MODERN);
        assertEquals("max", captured[0].getSearchText());
        assertEquals(WeightClass.FEATHERWEIGHT, captured[0].getWeightClass());

        controller.selectFighter(
                "max", WeightClass.FEATHERWEIGHT,
                UfcEra.MODERN, "Max Holloway");
        assertEquals("Max Holloway", captured[0].getSelectedFighterName());
    }

    @Test
    void presenterMapsRowsSelectedProfileAndRoundedAttributes() {
        final FighterBrowserViewModel viewModel = new FighterBrowserViewModel();
        final int[] events = {0};
        viewModel.addPropertyChangeListener(event -> events[0]++);
        final Map<Attribute, Double> attributes =
                new EnumMap<>(Attribute.class);
        attributes.put(Attribute.STRIKING, 89.6);
        final BrowseFightersOutputData output = new BrowseFightersOutputData(
                List.of(
                        new BrowseFightersOutputData.FighterSummary(
                                "Alpha", "Lightweight", "Modern"),
                        new BrowseFightersOutputData.FighterSummary(
                                "Bravo", "Welterweight", "Zuffa")),
                new BrowseFightersOutputData.FighterProfile(
                        "Alpha", "Lightweight", "Modern",
                        "20-1", 2, attributes));

        new FighterBrowserPresenter(viewModel).prepareSuccessView(output);

        final FighterBrowserState state = viewModel.getState();
        assertEquals(2, state.getRows().size());
        assertEquals("Alpha — Lightweight • Modern",
                state.getRows().get(0).toString());
        assertEquals("2 fighters found", state.getResultText());
        assertEquals("Alpha", state.getSelectedName());
        assertEquals("Lightweight • Modern • Record: 20-1",
                state.getSelectedDetails());
        assertEquals("Rank #2", state.getRankText());
        assertEquals(90, state.getAttributes().get(Attribute.STRIKING));
        assertEquals("", state.getErrorMessage());
        assertEquals(1, events[0]);
    }

    @Test
    void presenterHandlesSingleUnrankedResultAndNoSelection() {
        final FighterBrowserViewModel viewModel = new FighterBrowserViewModel();
        final FighterBrowserPresenter presenter =
                new FighterBrowserPresenter(viewModel);
        final Map<Attribute, Double> attributes =
                Map.of(Attribute.CARDIO, 70.0);

        presenter.prepareSuccessView(new BrowseFightersOutputData(
                List.of(new BrowseFightersOutputData.FighterSummary(
                        "History", "Welterweight", "Early")),
                new BrowseFightersOutputData.FighterProfile(
                        "History", "Welterweight", "Early",
                        "10-2", 0, attributes)));
        assertEquals("1 fighter found", viewModel.getState().getResultText());
        assertEquals("Unranked / historical catalogue entry",
                viewModel.getState().getRankText());

        presenter.prepareSuccessView(
                new BrowseFightersOutputData(List.of(), null));
        assertEquals("0 fighters found", viewModel.getState().getResultText());
        assertEquals("No fighter found", viewModel.getState().getSelectedName());
        assertEquals("Try changing the search text or filters.",
                viewModel.getState().getSelectedDetails());
    }

    @Test
    void failureAndStateCopiesAreSafe() {
        final FighterBrowserViewModel viewModel = new FighterBrowserViewModel();
        new FighterBrowserPresenter(viewModel).prepareFailView("offline");

        final FighterBrowserState state = viewModel.getState();
        assertEquals("Fighter catalogue unavailable", state.getSelectedName());
        assertEquals("0 fighters found", state.getResultText());
        assertEquals("offline", state.getErrorMessage());

        final java.util.ArrayList<FighterBrowserRow> rows =
                new java.util.ArrayList<>(List.of(
                        new FighterBrowserRow("A", "details")));
        state.setRows(rows);
        rows.clear();
        assertEquals(1, state.getRows().size());
        assertThrows(UnsupportedOperationException.class,
                () -> state.getRows().clear());

        final Map<Attribute, Integer> attributes =
                new EnumMap<>(Attribute.class);
        attributes.put(Attribute.REACH, 77);
        state.setAttributes(attributes);
        attributes.clear();
        assertEquals(77, state.getAttributes().get(Attribute.REACH));
        assertThrows(UnsupportedOperationException.class,
                () -> state.getAttributes().clear());

        assertEquals("A", state.getRows().get(0).getName());
        assertEquals("details", state.getRows().get(0).getDetails());
        assertFalse(state.getRows().isEmpty());
    }

    @Test
    void viewModelResetsNullStateAndFiresEvents() {
        final FighterBrowserViewModel viewModel = new FighterBrowserViewModel();
        final int[] events = {0};
        viewModel.addPropertyChangeListener(event -> events[0]++);
        viewModel.setState(null);
        viewModel.firePropertyChanged();

        assertEquals(FighterBrowserViewModel.VIEW_NAME,
                viewModel.getViewName());
        assertTrue(viewModel.getState().getRows().isEmpty());
        assertEquals(1, events[0]);
    }
}
