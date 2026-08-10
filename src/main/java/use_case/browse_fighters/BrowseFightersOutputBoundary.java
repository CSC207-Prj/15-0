package use_case.browse_fighters;

/**
 * Output boundary for the Fighter Browser.
 */
public interface BrowseFightersOutputBoundary {
    void prepareSuccessView(BrowseFightersOutputData outputData);

    void prepareFailView(String errorMessage);
}
