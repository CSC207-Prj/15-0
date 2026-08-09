package use_case.view_roster;

/**
 * Input boundary for the View Roster use case. Opening or refreshing the
 * Saved Fighters screen takes no input, so there is no input data class.
 */
public interface ViewRosterInputBoundary {

    /**
     * Loads the saved roster, ranks it, and presents it.
     */
    void execute();
}
