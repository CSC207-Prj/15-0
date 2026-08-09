package use_case.spin_fighter;

/**
 * Input boundary for the Spin Fighter use case.
 */
public interface SpinFighterInputBoundary {

    /**
     * Executes the Spin Fighter use case.
     *
     * @param inputData the selected fighter-pool settings
     */
    void execute(SpinFighterInputData inputData);
}