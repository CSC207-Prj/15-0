package interface_adapter.simulation;

import interface_adapter.ViewModel;

/**
 * Observable view model for the gauntlet simulation screen.
 *
 * The presenter writes SimulationState values here and the SimulationView observes
 * property-change events emitted by the inherited ViewModel support.
 */
public final class SimulationViewModel
        extends ViewModel {

    private SimulationState state =
            SimulationState.empty();

    /**
     * Creates a simulation view model with the default empty state.
     */
    public SimulationViewModel() {
        super("simulation");
    }

    /**
     * Returns the current display state.
     *
     * @return current simulation state
     */
    public SimulationState getState() {
        return state;
    }

    /**
     * Replaces the current state and notifies registered observers.
     *
     * @param state new display state
     */
    public void setState(
            SimulationState state) {

        final SimulationState oldState =
                this.state;

        this.state = state;

        firePropertyChanged(
                oldState,
                state);
    }
}
