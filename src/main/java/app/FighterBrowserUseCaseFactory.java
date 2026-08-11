package app;

import interface_adapter.fighter_browser.FighterBrowserController;
import interface_adapter.fighter_browser.FighterBrowserPresenter;
import interface_adapter.fighter_browser.FighterBrowserViewModel;
import use_case.browse_fighters.BrowseFightersInputBoundary;
import use_case.browse_fighters.BrowseFightersInteractor;
import use_case.browse_fighters.BrowseFightersOutputBoundary;
import use_case.browse_fighters.FighterBrowserDataAccessInterface;
import view.FighterBrowserView;

/**
 * Dependency wiring for User Story 6.
 */
public final class FighterBrowserUseCaseFactory {

    private FighterBrowserUseCaseFactory() {
    }

    /**
     * Creates and wires the Fighter Browser use case.
     *
     * @param dataAccess data-access interface used to retrieve fighter data
     * @param backAction action performed when the user navigates back
     * @return fully configured fighter browser view
     */
    public static FighterBrowserView create(
            FighterBrowserDataAccessInterface dataAccess,
            Runnable backAction) {

        final FighterBrowserViewModel viewModel =
                new FighterBrowserViewModel();

        final BrowseFightersOutputBoundary presenter =
                new FighterBrowserPresenter(viewModel);

        final BrowseFightersInputBoundary interactor =
                new BrowseFightersInteractor(
                        dataAccess,
                        presenter);

        final FighterBrowserController controller =
                new FighterBrowserController(interactor);

        return new FighterBrowserView(
                controller,
                viewModel,
                backAction);
    }
}
