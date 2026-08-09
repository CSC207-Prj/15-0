package use_case.simulation;

import java.util.Objects;

/** Input data created by the Simulation controller. */
public final class SimulationInputData {
    public enum Action {
        LOAD,
        SIMULATE_NEXT,
        AUTO_SIMULATE
    }

    private final Action action;

    public SimulationInputData(Action action) {
        this.action = Objects.requireNonNull(action, "action");
    }

    public Action getAction() {
        return action;
    }
}
