package interface_adapter.fighter_creation;

import java.util.HashMap;
import java.util.Map;

import entity.Attribute;
import entity.RealFighter;
import use_case.spin_fighter.SpinFighterOutputBoundary;
import use_case.spin_fighter.SpinFighterOutputData;

/**
 * Presenter for the Spin Fighter use case.
 */
public class SpinFighterPresenter implements SpinFighterOutputBoundary {

    private static final int BULLET_CODE_POINT = 0x2022;
    private static final String DETAILS_SEPARATOR =
            " " + Character.toString(BULLET_CODE_POINT) + " ";

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
                + DETAILS_SEPARATOR + fighter.getWeightClass().getDisplayName();

        viewModel.setCurrentFighter(fighter);
        viewModel.setRolledFighter(fighter.getName(), details, stats);
    }
}
