package interface_adapter.saved_fighters;

import use_case.view_roster.ViewRosterInputBoundary;

/**
 * Controller for the View Roster use case. The view calls this when the
 * Saved Fighters screen opens or needs a refresh.
 */
public class ViewRosterController {
    private final ViewRosterInputBoundary interactor;

    public ViewRosterController(ViewRosterInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Loads and ranks the saved roster.
     */
    public void execute() {
        interactor.execute();
    }
}
