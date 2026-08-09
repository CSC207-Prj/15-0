package interface_adapter.saved_fighters;

import java.util.ArrayList;
import java.util.List;

import use_case.view_roster.RosterEntryData;
import use_case.view_roster.ViewRosterOutputBoundary;
import use_case.view_roster.ViewRosterOutputData;

/**
 * Presenter for the View Roster use case: converts the ranked roster into
 * display rows for the Saved Fighters view.
 */
public class ViewRosterPresenter implements ViewRosterOutputBoundary {
    private final SavedFightersViewModel viewModel;

    public ViewRosterPresenter(SavedFightersViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ViewRosterOutputData outputData) {
        final SavedFightersState state = viewModel.getState();
        state.setRows(toRows(outputData.getRankedFighters()));
        state.setTopThree(toRows(outputData.getTopThree()));
        state.setError("");
        viewModel.firePropertyChanged();
    }

    private static List<SavedFighterRow> toRows(List<RosterEntryData> entries) {
        final List<SavedFighterRow> rows = new ArrayList<>();
        for (RosterEntryData entry : entries) {
            rows.add(new SavedFighterRow(
                    entry.getName(),
                    entry.getWeightClassName(),
                    entry.getRecordText(),
                    entry.getFinishes()));
        }
        return rows;
    }
}
