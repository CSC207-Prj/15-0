package interface_adapter.fighter_browser;

import entity.UfcEra;
import entity.WeightClass;
import use_case.browse_fighters.BrowseFightersInputBoundary;
import use_case.browse_fighters.BrowseFightersInputData;

/**
 * Controller for User Story 6.
 */
public class FighterBrowserController {
    private final BrowseFightersInputBoundary interactor;

    public FighterBrowserController(BrowseFightersInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void load() {
        execute("", null, UfcEra.ALL_TIME, null);
    }

    public void filter(String searchText,
                       WeightClass weightClass,
                       UfcEra era) {
        execute(searchText, weightClass, era, null);
    }

    public void selectFighter(String searchText,
                              WeightClass weightClass,
                              UfcEra era,
                              String fighterName) {
        execute(searchText, weightClass, era, fighterName);
    }

    private void execute(String searchText,
                         WeightClass weightClass,
                         UfcEra era,
                         String selectedFighterName) {
        interactor.execute(new BrowseFightersInputData(
                searchText,
                weightClass,
                era,
                selectedFighterName));
    }
}
