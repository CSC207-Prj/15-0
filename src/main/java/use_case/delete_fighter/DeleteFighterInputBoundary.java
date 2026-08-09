package use_case.delete_fighter;

/**
 * Input boundary for the Delete Fighter use case.
 */
public interface DeleteFighterInputBoundary {

    /**
     * Executes the Delete Fighter use case.
     * @param inputData the fighter to delete
     */
    void execute(DeleteFighterInputData inputData);
}
