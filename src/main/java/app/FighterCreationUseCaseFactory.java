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
import use_case.fighter_creation.FighterDetailsDataAccessInterface;
import use_case.reroll_fighter.RerollFighterInteractor;
import use_case.spin_fighter.SpinFighterInteractor;
import view.FighterCreationView;

/**
 * Wires together the Build Custom Fighter use cases.
 */
public final class FighterCreationUseCaseFactory {

    private FighterCreationUseCaseFactory() {
    }

    /**
     * Creates and wires the Build Custom Fighter use cases.
     *
     * @param fighterDataAccess data-access interface used to retrieve fighter data
     * @param randomSource source of randomness used when spinning and rerolling fighters
     * @param viewModel view model shared by the fighter creation presenters
     * @param backAction action performed when the user navigates back
     * @param continueAction action performed when fighter creation is complete
     * @return fully configured fighter creation view
     */
    public static FighterCreationView create(
            FighterDataAccessInterface fighterDataAccess,
            RandomSource randomSource,
            FighterCreationViewModel viewModel,
            Runnable backAction,
            Runnable continueAction) {

        final FighterDetailsDataAccessInterface detailsDataAccess;

        if (fighterDataAccess instanceof FighterDetailsDataAccessInterface) {
            detailsDataAccess =
                    (FighterDetailsDataAccessInterface) fighterDataAccess;
        }
        else {
            detailsDataAccess = null;
        }

        final SpinFighterPresenter spinPresenter =
                new SpinFighterPresenter(viewModel);

        final SpinFighterInteractor spinInteractor =
                new SpinFighterInteractor(
                        randomSource,
                        fighterDataAccess,
                        detailsDataAccess,
                        spinPresenter);

        final SpinFighterController spinController =
                new SpinFighterController(spinInteractor);

        final RerollFighterPresenter rerollPresenter =
                new RerollFighterPresenter(viewModel);

        final RerollFighterInteractor rerollInteractor =
                new RerollFighterInteractor(
                        randomSource,
                        fighterDataAccess,
                        detailsDataAccess,
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
