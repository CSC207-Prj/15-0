package use_case.load_fighter;

/**
 * Input boundary for the Load Fighter use case.
 */
public interface LoadFighterInputBoundary {

    /**
     * Executes the Load Fighter use case.
     * @param inputData the fighter to load
     */
    void execute(LoadFighterInputData inputData);
}
