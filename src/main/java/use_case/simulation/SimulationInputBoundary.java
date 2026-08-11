package use_case.simulation;

/**
 * Defines the input boundary for User Story 4 gauntlet simulation actions.
 *
 * Interface adapters call this use-case-owned abstraction rather than
 * depending directly on the concrete SimulationInteractor.
 */
public interface SimulationInputBoundary {
    /**
     * Executes the simulation action described by the input data.
     *
     * @param inputData the requested gauntlet action
     */
    void execute(SimulationInputData inputData);
}
