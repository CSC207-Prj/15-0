package interface_adapter.reroll_fighter;

import java.util.HashMap;
import java.util.Map;

import entity.Attribute;
import entity.RealFighter;
import interface_adapter.fighter_creation.FighterCreationViewModel;
import use_case.reroll_fighter.RerollFighterOutputBoundary;
import use_case.reroll_fighter.RerollFighterOutputData;

/**
 * Presenter for the Reroll Fighter use case.
 */
public class RerollFighterPresenter implements RerollFighterOutputBoundary {

    private static final int BULLET_CODE_POINT = 0x2022;
    private static final String DETAILS_SEPARATOR =
            " " + Character.toString(BULLET_CODE_POINT) + " ";

    private final FighterCreationViewModel viewModel;

    public RerollFighterPresenter(FighterCreationViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(RerollFighterOutputData outputData) {
        final RealFighter fighter = outputData.getFighter();
        viewModel.setCurrentFighter(fighter);
        final Map<String, Integer> stats = new HashMap<>();

        for (Map.Entry<Attribute, Double> entry : fighter.getAttributes().entrySet()) {
            stats.put(entry.getKey().getDisplayName(),
                    (int) Math.round(entry.getValue()));
        }

        final String details = fighter.getProfessionalRecord()
                + DETAILS_SEPARATOR + fighter.getWeightClass().getDisplayName();

        viewModel.setRolledFighter(fighter.getName(), details, stats);
        viewModel.setRerollsLeft(outputData.getRerollsLeft());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setRerollsLeft(0);
    }
}
