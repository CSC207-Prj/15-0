package interface_adapter.simulation;

import use_case.simulation.SimulationInputBoundary;
import use_case.simulation.SimulationInputData;

import java.util.Objects;

/**
 * Converts Swing button actions into input data for the Simulation use case.
 */
public final class SimulationController {
    private final SimulationInputBoundary interactor;

    public SimulationController(SimulationInputBoundary interactor) {
        this.interactor = Objects.requireNonNull(interactor, "interactor");
    }

    public void loadRun() {
        interactor.execute(new SimulationInputData(SimulationInputData.Action.LOAD));
    }

    public void simulateNextFight() {
        interactor.execute(new SimulationInputData(SimulationInputData.Action.SIMULATE_NEXT));
    }

    public void autoSimulateRun() {
        interactor.execute(new SimulationInputData(SimulationInputData.Action.AUTO_SIMULATE));
    }
}
