package use_case.browse_fighters;

import entity.UfcEra;
import entity.WeightClass;

/**
 * Search, filter, and selection criteria for the Fighter Browser.
 */
public class BrowseFightersInputData {
    private final String searchText;
    private final WeightClass weightClass;
    private final UfcEra era;
    private final String selectedFighterName;

    public BrowseFightersInputData(String searchText,
                                   WeightClass weightClass,
                                   UfcEra era,
                                   String selectedFighterName) {
        this.searchText = searchText == null ? "" : searchText.trim();
        this.weightClass = weightClass;
        this.era = era;
        this.selectedFighterName = selectedFighterName;
    }

    public String getSearchText() {
        return searchText;
    }

    public WeightClass getWeightClass() {
        return weightClass;
    }

    public UfcEra getEra() {
        return era;
    }

    public String getSelectedFighterName() {
        return selectedFighterName;
    }
}
