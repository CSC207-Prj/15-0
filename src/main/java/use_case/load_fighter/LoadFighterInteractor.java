package use_case.load_fighter;

import java.util.LinkedHashMap;
import java.util.Map;

import entity.Attribute;
import entity.CustomFighter;

/**
 * Interactor for the Load Fighter use case: fetches a saved fighter from the
 * roster and presents its full details.
 */
public class LoadFighterInteractor implements LoadFighterInputBoundary {
    private static final String WEIGHT_CLASS_UNASSIGNED = "TBD";

    private final LoadFighterDataAccessInterface rosterDataAccess;
    private final LoadFighterOutputBoundary presenter;

    public LoadFighterInteractor(LoadFighterDataAccessInterface rosterDataAccess,
                                 LoadFighterOutputBoundary presenter) {
        this.rosterDataAccess = rosterDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(LoadFighterInputData inputData) {
        final String fighterName = inputData.getFighterName();

        if (fighterName == null || fighterName.trim().isEmpty()) {
            presenter.prepareFailView("Choose a fighter to load.");
            return;
        }

        final CustomFighter fighter = rosterDataAccess.getByName(fighterName);
        if (fighter == null) {
            presenter.prepareFailView("No saved fighter named \"" + fighterName + "\" was found.");
            return;
        }

        final String weightClassName;
        if (fighter.getWeightClass() == null) {
            weightClassName = WEIGHT_CLASS_UNASSIGNED;
        }
        else {
            weightClassName = fighter.getWeightClass().getDisplayName();
        }

        final Map<String, Double> attributeValues = new LinkedHashMap<>();
        for (Map.Entry<Attribute, Double> entry : fighter.getAttributes().entrySet()) {
            attributeValues.put(entry.getKey().getDisplayName(), entry.getValue());
        }

        presenter.prepareSuccessView(new LoadFighterOutputData(
                fighter.getName(),
                weightClassName,
                fighter.getRecord().getWins() + "-" + fighter.getRecord().getLosses(),
                fighter.getRecord().getFinishes(),
                attributeValues,
                false));
    }
}
