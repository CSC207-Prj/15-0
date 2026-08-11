package app;

import entity.FightSimulator;
import entity.RandomSource;
import entity.WeightedFightSimulator;
import interface_adapter.simulation.SimulationController;
import interface_adapter.simulation.SimulationPresenter;
import interface_adapter.simulation.SimulationViewModel;
import use_case.simulation.SimulationDataAccessInterface;
import use_case.simulation.SimulationInteractor;
import view.SimulationView;

import java.util.Objects;

/**
 * Creates and wires the dependencies for the Simulation use case.
 */
public final class SimulationUseCaseFactory {
    private SimulationUseCaseFactory() {
    }

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
