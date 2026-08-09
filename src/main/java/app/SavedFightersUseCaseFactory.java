package app;

import java.util.Objects;

import entity.FightSimulator;
import entity.RandomSource;
import entity.WeightedFightSimulator;
import interface_adapter.saved_fighters.DeleteFighterController;
import interface_adapter.saved_fighters.DeleteFighterPresenter;
import interface_adapter.saved_fighters.ExhibitionController;
import interface_adapter.saved_fighters.ExhibitionPresenter;
import interface_adapter.saved_fighters.LoadFighterController;
import interface_adapter.saved_fighters.LoadFighterPresenter;
import interface_adapter.saved_fighters.SavedFightersViewModel;
import interface_adapter.saved_fighters.ViewRosterController;
import interface_adapter.saved_fighters.ViewRosterPresenter;
import use_case.delete_fighter.DeleteFighterDataAccessInterface;
import use_case.delete_fighter.DeleteFighterInteractor;
import use_case.exhibition.ExhibitionDataAccessInterface;
import use_case.exhibition.ExhibitionInteractor;
import use_case.load_fighter.LoadFighterDataAccessInterface;
import use_case.load_fighter.LoadFighterInteractor;
import use_case.view_roster.ViewRosterDataAccessInterface;
import use_case.view_roster.ViewRosterInteractor;
import view.SavedFightersView;

/**
 * App-layer assembly helper for the User Story 5 dependency graph. The four
 * data access parameters are usually the same roster object; they are typed
 * separately so each use case keeps its own small interface.
 */
public final class SavedFightersUseCaseFactory {
    private SavedFightersUseCaseFactory() {
    }

    public static SavedFightersView create(
            ViewRosterDataAccessInterface viewRosterDataAccess,
            DeleteFighterDataAccessInterface deleteFighterDataAccess,
            LoadFighterDataAccessInterface loadFighterDataAccess,
            ExhibitionDataAccessInterface exhibitionDataAccess,
            RandomSource randomSource,
            Runnable backHomeAction) {

        Objects.requireNonNull(viewRosterDataAccess, "viewRosterDataAccess");
        Objects.requireNonNull(randomSource, "randomSource");

        final SavedFightersViewModel viewModel = new SavedFightersViewModel();

        final ViewRosterController viewRosterController = new ViewRosterController(
                new ViewRosterInteractor(viewRosterDataAccess, new ViewRosterPresenter(viewModel)));
        final DeleteFighterController deleteFighterController = new DeleteFighterController(
                new DeleteFighterInteractor(deleteFighterDataAccess,
                        new DeleteFighterPresenter(viewModel)));
        final LoadFighterController loadFighterController = new LoadFighterController(
                new LoadFighterInteractor(loadFighterDataAccess,
                        new LoadFighterPresenter(viewModel)));

        final FightSimulator fightSimulator = new WeightedFightSimulator(randomSource);
        final ExhibitionController exhibitionController = new ExhibitionController(
                new ExhibitionInteractor(exhibitionDataAccess, fightSimulator,
                        new ExhibitionPresenter(viewModel)));

        return new SavedFightersView(
                viewRosterController,
                deleteFighterController,
                loadFighterController,
                exhibitionController,
                viewModel,
                backHomeAction);
    }
}
