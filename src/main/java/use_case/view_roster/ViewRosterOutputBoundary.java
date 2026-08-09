package use_case.view_roster;

/**
 * Output boundary for presenting the saved roster.
 */
public interface ViewRosterOutputBoundary {

    /**
     * Prepares the view of the ranked roster. An empty roster is still a
     * success; the view decides how to show "no saved fighters yet".
     * @param outputData the ranked roster and top three
     */
    void prepareSuccessView(ViewRosterOutputData outputData);
}
