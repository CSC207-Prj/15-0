package use_case.simulation;

/**
 * Defines the output boundary used by the simulation interactor.
 *
 * The use case sends raw output through this interface and therefore
 * does not depend on the concrete presenter or on Swing.
 */
public interface SimulationOutputBoundary {
    /**
     * Presents a successful snapshot of the current gauntlet.
     *
     * @param outputData raw use-case output to adapt for the view
     */
    void prepareSuccessView(
            SimulationOutputData outputData);

    /**
     * Presents a recoverable use-case failure.
     *
     * @param errorMessage message describing why the requested action failed
     */
    void prepareFailView(String errorMessage);
}
