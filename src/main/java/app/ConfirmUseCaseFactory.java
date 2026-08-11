package app;

import interface_adapter.confirm_fighter.ConfirmController;
import interface_adapter.confirm_fighter.ConfirmPresenter;
import interface_adapter.confirm_fighter.ConfirmViewModel;
import use_case.confirm.ConfirmInputBoundary;
import use_case.confirm.ConfirmInteractor;
import use_case.confirm.ConfirmOutputBoundary;
import view.ConfirmView;

/**
 * Creates and connects the components used by the confirm-fighter screen.
 */
public final class ConfirmUseCaseFactory {

    /**
     * Prevents this utility class from being instantiated.
     */
    private ConfirmUseCaseFactory() {
    }

    /**
     * Creates the confirm-fighter view and its supporting components.
     *
     * @param viewModel the view model used by the confirm-fighter screen
     * @param backAction the action to run when the user goes back
     * @param continueAction the action to run after confirmation
     * @return the configured confirm-fighter view
     */
    public static ConfirmView create(ConfirmViewModel viewModel,
                                     Runnable backAction,
                                     Runnable continueAction) {

        final ConfirmOutputBoundary presenter =
                new ConfirmPresenter(viewModel);

        final ConfirmInputBoundary interactor =
                new ConfirmInteractor(presenter);

        final ConfirmController controller =
                new ConfirmController(interactor);

        return new ConfirmView(controller, viewModel, backAction, continueAction);
    }
}
