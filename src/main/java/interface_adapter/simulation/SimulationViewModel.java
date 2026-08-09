package interface_adapter.simulation;

import interface_adapter.ViewModel;

/** Observable View Model for User Story 4. */
public final class SimulationViewModel extends ViewModel {
    private SimulationState state = SimulationState.empty();

    public SimulationViewModel() {
        super("simulation");
    }

    public SimulationState getState() {
        return state;
    }

    public void setState(SimulationState state) {
        final SimulationState oldState = this.state;
        this.state = state;
        firePropertyChanged(oldState, state);
    }
}
