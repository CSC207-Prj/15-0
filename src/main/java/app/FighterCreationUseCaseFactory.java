package app;

import entity.RandomSource;
import interface_adapter.fighter_creation.AssignAttributeController;
import interface_adapter.fighter_creation.FighterCreationViewModel;
import interface_adapter.fighter_creation.LoadFighterCreationController;
import interface_adapter.fighter_creation.LoadFighterCreationPresenter;
import interface_adapter.fighter_creation.RerollFighterController;
import interface_adapter.fighter_creation.RerollFighterPresenter;
import interface_adapter.fighter_creation.SpinFighterController;
import interface_adapter.fighter_creation.SpinFighterPresenter;
import use_case.assign_attribute.AssignAttributeInputBoundary;
import use_case.assign_attribute.AssignAttributeInteractor;
import use_case.assign_attribute.AssignAttributeOutputBoundary;
import use_case.fighter_creation.FighterCreationSessionDataAccessInterface;
import use_case.fighter_creation.LoadFighterCreationInputBoundary;
import use_case.fighter_creation.LoadFighterCreationInteractor;
import use_case.fighter_creation.LoadFighterCreationOutputBoundary;
import use_case.reroll_fighter.RerollFighterInputBoundary;
import use_case.reroll_fighter.RerollFighterInteractor;
import use_case.reroll_fighter.RerollFighterOutputBoundary;
import use_case.spin_fighter.SpinFighterInputBoundary;
import use_case.spin_fighter.SpinFighterInteractor;
import use_case.spin_fighter.SpinFighterOutputBoundary;
import view.FighterCreationView;

/**
 * Wires the Fighter Creation user story.
 */
public final class FighterCreationUseCaseFactory {

    private FighterCreationUseCaseFactory() {
    }

    public static FighterCreationView create(
            FighterCreationSessionDataAccessInterface sessionDataAccess,
            RandomSource randomSource,
            Runnable backAction,
            Runnable continueAction) {

        final FighterCreationViewModel viewModel =
                new FighterCreationViewModel();

        final SpinFighterOutputBoundary spinPresenter =
                new SpinFighterPresenter(viewModel);

        final SpinFighterInputBoundary spinInteractor =
                new SpinFighterInteractor(
                        randomSource,
                        sessionDataAccess,
                        spinPresenter
                );

        final RerollFighterOutputBoundary rerollPresenter =
                new RerollFighterPresenter(viewModel);

        final RerollFighterInputBoundary rerollInteractor =
                new RerollFighterInteractor(
                        randomSource,
                        sessionDataAccess,
                        rerollPresenter
                );

        final AssignAttributeOutputBoundary assignPresenter =
                new interface_adapter.fighter_creation.AssignAttributePresenter(
                        viewModel
                );

        final AssignAttributeInputBoundary assignInteractor =
                new AssignAttributeInteractor(
                        assignPresenter,
                        spinInteractor
                );

        final LoadFighterCreationOutputBoundary loadPresenter =
                new LoadFighterCreationPresenter(
                        viewModel
                );

        final LoadFighterCreationInputBoundary loadInteractor =
                new LoadFighterCreationInteractor(
                        sessionDataAccess,
                        loadPresenter
                );

        return new FighterCreationView(
                new SpinFighterController(spinInteractor),
                new RerollFighterController(rerollInteractor),
                new AssignAttributeController(assignInteractor),
                new LoadFighterCreationController(loadInteractor),
                viewModel,
                backAction,
                continueAction
        );
    }
}
