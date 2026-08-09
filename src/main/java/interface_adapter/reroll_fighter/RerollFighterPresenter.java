package interface_adapter.fighter_creation;

import entity.Attribute;
import entity.RealFighter;
import use_case.reroll_fighter.RerollFighterOutputBoundary;
import use_case.reroll_fighter.RerollFighterOutputData;

import java.util.HashMap;
import java.util.Map;

/**
 * Presenter for the Reroll Fighter use case.
 */
public class RerollFighterPresenter implements RerollFighterOutputBoundary {

    private final FighterCreationViewModel viewModel;

    public RerollFighterPresenter(FighterCreationViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(RerollFighterOutputData outputData) {
        final RealFighter fighter = outputData.getFighter();
        viewModel.setCurrentFighter(fighter);
        viewModel.setCurrentFighter(fighter);
        final Map<String, Integer> stats = new HashMap<>();

        for (Map.Entry<Attribute, Double> entry : fighter.getAttributes().entrySet()) {
            stats.put(entry.getKey().getDisplayName(),
                    (int) Math.round(entry.getValue()));
        }

        final String details = fighter.getProfessionalRecord() + " • " + fighter.getWeightClass().getDisplayName();

        viewModel.setRolledFighter(fighter.getName(), details, stats);
        viewModel.setRerollsLeft(outputData.getRerollsLeft());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setRerollsLeft(0);
    }
}