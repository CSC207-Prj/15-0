package use_case.delete_fighter;

/**
 * Output boundary for presenting the result of the Delete Fighter use case.
 */
public interface DeleteFighterOutputBoundary {

    /**
     * Prepares the success view after a fighter was deleted.
     * @param outputData the deleted fighter's details
     */
    void prepareSuccessView(DeleteFighterOutputData outputData);

    /**
     * Prepares the failure view when the fighter could not be deleted.
     * @param errorMessage explanation of what went wrong
     */
    void prepareFailView(String errorMessage);
}
