package use_case.simulation;

/** Output boundary implemented by the Simulation presenter. */
public interface SimulationOutputBoundary {
    void prepareSuccessView(SimulationOutputData outputData);

    void prepareFailView(String errorMessage);
}
