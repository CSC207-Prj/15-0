package use_case.reroll_fighter;

/**
 * Input boundary for the Reroll Fighter use case.
 */
public interface RerollFighterInputBoundary {

    /**
     * Executes the Reroll Fighter use case.
     *
     * @param inputData the reroll request data
     */
    void execute(RerollFighterInputData inputData);
}