package use_case.simulation;

import java.util.Objects;

/**
 * Carries a single command from the simulation controller into the use case.
 *
 * The object contains no Swing types or presentation details, which keeps
 * the input boundary independent of the user-interface framework.
 */
public final class SimulationInputData {
    /**
     * Enumerates the actions supported by the gauntlet simulation use case.
     */
    public enum Action {
        /** Loads the current run without simulating a fight. */
        LOAD,

        /** Simulates only the current opponent. */
        SIMULATE_NEXT,

        /** Simulates every remaining opponent in the gauntlet. */
        AUTO_SIMULATE
    }

    private final Action action;

    /**
     * Creates input data for one simulation action.
     *
     * @param action the action requested by the controller
     * @throws NullPointerException if action is null
     */
    public SimulationInputData(Action action) {
        this.action =
                Objects.requireNonNull(
                        action,
                        "action");
    }

    /**
     * Returns the requested simulation action.
     *
     * @return the action to execute
     */
    public Action getAction() {
        return action;
    }
}
