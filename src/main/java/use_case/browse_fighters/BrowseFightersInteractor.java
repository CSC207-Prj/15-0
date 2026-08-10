package use_case.browse_fighters;

import entity.RealFighter;
import entity.UfcEra;
import entity.WeightClass;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Interactor for searching, filtering, and selecting real UFC fighters.
 */
public class BrowseFightersInteractor implements BrowseFightersInputBoundary {
    private final FighterBrowserDataAccessInterface dataAccess;
    private final BrowseFightersOutputBoundary presenter;

    public BrowseFightersInteractor(FighterBrowserDataAccessInterface dataAccess,
                                    BrowseFightersOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(BrowseFightersInputData inputData) {
        if (inputData == null) {
            presenter.prepareFailView("Browser filters are unavailable.");
            return;
        }

        final List<RealFighter> fighters;
        try {
            fighters = dataAccess.getFighters();
        }
        catch (RuntimeException exception) {
            presenter.prepareFailView("Unable to load the fighter catalogue.");
            return;
        }

        if (fighters == null) {
            presenter.prepareFailView("Unable to load the fighter catalogue.");
            return;
        }

        final List<RealFighter> filtered = filterFighters(fighters, inputData);
        filtered.sort(Comparator.comparing(RealFighter::getName));

        final List<BrowseFightersOutputData.FighterSummary> summaries =
                new ArrayList<>();
        for (RealFighter fighter : filtered) {
            summaries.add(new BrowseFightersOutputData.FighterSummary(
                    fighter.getName(),
                    fighter.getWeightClass().getDisplayName(),
                    fighter.getEra().getDisplayName()));
        }

        final RealFighter selected = chooseSelectedFighter(
                filtered,
                inputData.getSelectedFighterName());

        final BrowseFightersOutputData.FighterProfile profile =
                selected == null ? null : toProfile(selected);

        presenter.prepareSuccessView(
                new BrowseFightersOutputData(summaries, profile));
    }

    private List<RealFighter> filterFighters(List<RealFighter> fighters,
                                             BrowseFightersInputData inputData) {
        final List<RealFighter> filtered = new ArrayList<>();
        final String query = inputData.getSearchText().toLowerCase(Locale.ROOT);
        final WeightClass weightClass = inputData.getWeightClass();
        final UfcEra era = inputData.getEra();

        for (RealFighter fighter : fighters) {
            if (fighter == null) {
                continue;
            }

            final boolean matchesSearch = query.isEmpty()
                    || fighter.getName().toLowerCase(Locale.ROOT).contains(query);
            final boolean matchesWeightClass = weightClass == null
                    || fighter.getWeightClass() == weightClass;
            final boolean matchesEra = era == null
                    || era == UfcEra.ALL_TIME
                    || fighter.getEra() == era;

            if (matchesSearch && matchesWeightClass && matchesEra) {
                filtered.add(fighter);
            }
        }

        return filtered;
    }

    private RealFighter chooseSelectedFighter(List<RealFighter> fighters,
                                              String selectedFighterName) {
        if (fighters.isEmpty()) {
            return null;
        }

        if (selectedFighterName != null) {
            for (RealFighter fighter : fighters) {
                if (fighter.getName().equals(selectedFighterName)) {
                    return fighter;
                }
            }
        }

        return fighters.get(0);
    }

    private BrowseFightersOutputData.FighterProfile toProfile(
            RealFighter fighter) {
        return new BrowseFightersOutputData.FighterProfile(
                fighter.getName(),
                fighter.getWeightClass().getDisplayName(),
                fighter.getEra().getDisplayName(),
                fighter.getProfessionalRecord(),
                fighter.getRank(),
                fighter.getAttributes());
    }
}
