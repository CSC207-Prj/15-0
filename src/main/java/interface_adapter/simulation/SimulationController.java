package interface_adapter.simulation;

import java.util.Objects;

import use_case.simulation.SimulationInputBoundary;
import use_case.simulation.SimulationInputData;

/**
 * Adapts simulation view actions into input data understood by the use case.
 *
 * The controller is intentionally thin: it creates SimulationInputData and invokes the
 * SimulationInputBoundary, leaving business decisions to the interactor.
 */
public final class SimulationController {
    private final SimulationInputBoundary interactor;

    /**
     * Creates a controller for the supplied simulation input boundary.
     *
     * @param interactor use-case input boundary to invoke
     * @throws NullPointerException if interactor is null
     */
    public SimulationController(
            SimulationInputBoundary interactor) {

        this.interactor =
                Objects.requireNonNull(
                        interactor,
                        "interactor");
    }

    /**
     * Requests the current gauntlet state without simulating a fight.
     */
    public void loadRun() {
        interactor.execute(
                new SimulationInputData(
                        SimulationInputData.Action.LOAD));
    }

    /**
     * Requests simulation of only the current opponent.
     */
    public void simulateNextFight() {
        interactor.execute(
                new SimulationInputData(
                        SimulationInputData.Action.SIMULATE_NEXT));
    }

    /**
     * Requests simulation of every remaining opponent.
     */
    public void autoSimulateRun() {
        interactor.execute(
                new SimulationInputData(
                        SimulationInputData.Action.AUTO_SIMULATE));
    }
}
