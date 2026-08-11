package app;

import java.util.Objects;

import entity.FightSimulator;
import entity.RandomSource;
import entity.WeightedFightSimulator;
import interface_adapter.simulation.SimulationController;
import interface_adapter.simulation.SimulationPresenter;
import interface_adapter.simulation.SimulationViewModel;
import use_case.simulation.SimulationDataAccessInterface;
import use_case.simulation.SimulationInteractor;
import view.SimulationView;

/**
 * Creates and wires the dependencies for the Simulation use case.
 */
public final class SimulationUseCaseFactory {
    private SimulationUseCaseFactory() {
    }

    /**
     * Creates a fully wired simulation view and its Clean Architecture collaborators.
     *
     * @param dataAccess concrete implementation of the simulation session port
     * @param randomSource randomness implementation used by the weighted strategy
     * @param backHomeAction navigation callback for returning home
     * @param savedFightersAction callback used after the gauntlet is complete
     * @return fully configured simulation Swing view
     * @throws NullPointerException if {@code dataAccess} or {@code randomSource} is null
     */
    public static SimulationView create(
            SimulationDataAccessInterface dataAccess,
            RandomSource randomSource,
            Runnable backHomeAction,
            Runnable savedFightersAction) {

        Objects.requireNonNull(dataAccess, "dataAccess");
        Objects.requireNonNull(randomSource, "randomSource");

        final SimulationViewModel viewModel = new SimulationViewModel();
        final SimulationPresenter presenter = new SimulationPresenter(viewModel);
        final FightSimulator simulator = new WeightedFightSimulator(randomSource);
        final SimulationInteractor interactor =
                new SimulationInteractor(dataAccess, simulator, presenter);
        final SimulationController controller = new SimulationController(interactor);

        return new SimulationView(
                controller,
                viewModel,
                backHomeAction,
                savedFightersAction);
    }
}
