package use_case.save_fighter;

/**
 * Input boundary for the Save Fighter use case.
 */
public interface SaveFighterInputBoundary {

    /**
     * Executes the Save Fighter use case.
     * @param inputData the fighter to save
     */
    void execute(SaveFighterInputData inputData);
}
