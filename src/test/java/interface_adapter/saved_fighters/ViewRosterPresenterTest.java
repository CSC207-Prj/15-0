package interface_adapter.saved_fighters;

import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import use_case.view_roster.RosterEntryData;
import use_case.view_roster.ViewRosterOutputData;

public class ViewRosterPresenterTest {

    @Test
    public void mapsRankedEntriesIntoViewState() {
        final SavedFightersViewModel viewModel = new SavedFightersViewModel();
        final boolean[] fired = {false};
        viewModel.addPropertyChangeListener(event -> fired[0] = true);

        final RosterEntryData first = new RosterEntryData("Adams", "Featherweight", 3, 0, 2, "3-0");
        final RosterEntryData second = new RosterEntryData("Bones", "Lightweight", 3, 0, 1, "3-0");

        new ViewRosterPresenter(viewModel).prepareSuccessView(
                new ViewRosterOutputData(List.of(first, second), List.of(first, second)));

        final SavedFightersState state = viewModel.getState();
        assertEquals(2, state.getRows().size());
        assertEquals("Adams", state.getRows().get(0).getName());
        assertEquals("Featherweight", state.getRows().get(0).getWeightClassName());
        assertEquals("3-0", state.getRows().get(0).getRecordText());
        assertEquals(2, state.getRows().get(0).getFinishes());
        assertEquals(2, state.getTopThree().size());
        assertEquals("", state.getError());
        assertTrue(fired[0]);
    }
}
