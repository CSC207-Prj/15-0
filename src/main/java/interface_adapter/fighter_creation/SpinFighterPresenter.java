package interface_adapter.fighter_creation;

import entity.Attribute;
import entity.RealFighter;
import use_case.spin_fighter.SpinFighterOutputBoundary;
import use_case.spin_fighter.SpinFighterOutputData;

import java.util.HashMap;
import java.util.Map;

/**
 * Presenter for the Spin Fighter use case.
 */
public class SpinFighterPresenter implements SpinFighterOutputBoundary {

    private final FighterCreationViewModel viewModel;

    public SpinFighterPresenter(FighterCreationViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SpinFighterOutputData outputData) {
        final RealFighter fighter = outputData.getFighter();
        final Map<String, Integer> stats = new HashMap<>();

        for (Map.Entry<Attribute, Double> entry : fighter.getAttributes().entrySet()) {
            stats.put(entry.getKey().getDisplayName(), (int) Math.round(entry.getValue()));
        }

        final String details = fighter.getProfessionalRecord()
                + " • " + fighter.getWeightClass().getDisplayName();

        viewModel.setCurrentFighter(fighter);
        viewModel.setRolledFighter(fighter.getName(), details, stats);
    }
}