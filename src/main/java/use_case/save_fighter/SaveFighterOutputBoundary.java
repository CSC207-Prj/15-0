package use_case.save_fighter;

/**
 * Output boundary for presenting the result of the Save Fighter use case.
 */
public interface SaveFighterOutputBoundary {

    /**
     * Prepares the success view after a fighter was saved.
     * @param outputData the saved fighter's details
     */
    void prepareSuccessView(SaveFighterOutputData outputData);

    /**
     * Prepares the failure view when the fighter could not be saved.
     * @param errorMessage explanation of what went wrong
     */
    void prepareFailView(String errorMessage);
}
