package app;

import interface_adapter.confirm_fighter.ConfirmController;
import interface_adapter.confirm_fighter.ConfirmPresenter;
import interface_adapter.confirm_fighter.ConfirmViewModel;
import use_case.confirm.ConfirmInputBoundary;
import use_case.confirm.ConfirmInteractor;
import use_case.confirm.ConfirmOutputBoundary;
import view.ConfirmView;

public final class ConfirmUseCaseFactory {

    private ConfirmUseCaseFactory() {
    }

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