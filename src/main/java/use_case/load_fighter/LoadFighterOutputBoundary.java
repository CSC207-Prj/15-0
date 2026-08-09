package use_case.load_fighter;

/**
 * Output boundary for presenting a loaded fighter.
 */
public interface LoadFighterOutputBoundary {

    /**
     * Prepares the view of the loaded fighter's details.
     * @param outputData the fighter's details
     */
    void prepareSuccessView(LoadFighterOutputData outputData);

    /**
     * Prepares the failure view when the fighter could not be loaded.
     * @param errorMessage explanation of what went wrong
     */
    void prepareFailView(String errorMessage);
}
