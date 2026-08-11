package interface_adapter.saved_fighters;

import use_case.exhibition.ExhibitionInputBoundary;
import use_case.exhibition.ExhibitionInputData;

/**
 * Controller for the Exhibition Match use case.
 */
public class ExhibitionController {
    private final ExhibitionInputBoundary interactor;

    public ExhibitionController(ExhibitionInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Runs a one-off exhibition match between two saved fighters.
     * @param firstFighterName the first fighter's name
     * @param secondFighterName the second fighter's name
     */
    public void execute(String firstFighterName, String secondFighterName) {
        interactor.execute(new ExhibitionInputData(firstFighterName, secondFighterName));
    }
}
