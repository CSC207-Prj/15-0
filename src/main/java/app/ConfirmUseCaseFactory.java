package app;

import interface_adapter.ConfirmController;
import interface_adapter.ConfirmPresenter;
import interface_adapter.ConfirmViewModel;
import use_case.confirm.ConfirmInputBoundary;
import use_case.confirm.ConfirmInteractor;
import use_case.confirm.ConfirmOutputBoundary;
import view.ConfirmView;

public final class ConfirmUseCaseFactory {

    private ConfirmUseCaseFactory() {
    }

    public static ConfirmView create(Runnable backAction,
                                     Runnable continueAction) {
        final ConfirmViewModel viewModel = new ConfirmViewModel();

        final ConfirmOutputBoundary presenter =
                new ConfirmPresenter(viewModel);

        final ConfirmInputBoundary interactor =
                new ConfirmInteractor(presenter);

        final ConfirmController controller =
                new ConfirmController(interactor);

        return new ConfirmView(controller, viewModel, backAction, continueAction);
    }
}