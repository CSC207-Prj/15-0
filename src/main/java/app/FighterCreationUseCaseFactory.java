package app;

import entity.RandomSource;
import interface_adapter.assign_attribute.AssignAttributeController;
import interface_adapter.fighter_creation.AssignAttributePresenter;
import interface_adapter.fighter_creation.FighterCreationViewModel;
import interface_adapter.fighter_creation.RerollFighterController;
import interface_adapter.fighter_creation.RerollFighterPresenter;
import interface_adapter.fighter_creation.SpinFighterController;
import interface_adapter.fighter_creation.SpinFighterPresenter;
import use_case.assign_attribute.AssignAttributeInteractor;
import use_case.fighter_creation.FighterDataAccessInterface;
import use_case.reroll_fighter.RerollFighterInteractor;
import use_case.spin_fighter.SpinFighterInteractor;
import view.FighterCreationView;

/**
 * Wires together the Build Custom Fighter use cases.
 */
public final class FighterCreationUseCaseFactory {

    private FighterCreationUseCaseFactory() {
    }

    public static FighterCreationView create(
            FighterDataAccessInterface fighterDataAccess,
            RandomSource randomSource,
            FighterCreationViewModel viewModel,
            Runnable backAction,
            Runnable continueAction) {

        final SpinFighterPresenter spinPresenter =
                new SpinFighterPresenter(viewModel);

        final SpinFighterInteractor spinInteractor =
                new SpinFighterInteractor(
                        randomSource,
                        fighterDataAccess,
                        spinPresenter);

        final SpinFighterController spinController =
                new SpinFighterController(spinInteractor);

        final RerollFighterPresenter rerollPresenter =
                new RerollFighterPresenter(viewModel);

        final RerollFighterInteractor rerollInteractor =
                new RerollFighterInteractor(
                        randomSource,
                        fighterDataAccess,
                        rerollPresenter);

        final RerollFighterController rerollController =
                new RerollFighterController(rerollInteractor);

        final AssignAttributePresenter assignPresenter =
                new AssignAttributePresenter(viewModel);

        final AssignAttributeInteractor assignInteractor =
                new AssignAttributeInteractor(
                        assignPresenter,
                        spinInteractor);

        final AssignAttributeController assignController =
                new AssignAttributeController(assignInteractor);

        return new FighterCreationView(
                spinController,
                rerollController,
                assignController,
                viewModel,
                backAction,
                continueAction);
    }
}