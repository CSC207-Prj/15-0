package interface_adapter.saved_fighters;

import java.util.Map;

import use_case.load_fighter.LoadFighterOutputBoundary;
import use_case.load_fighter.LoadFighterOutputData;

/**
 * Presenter for the Load Fighter use case: formats the loaded fighter's
 * details into the summary line the Saved Fighters view shows.
 */
public class LoadFighterPresenter implements LoadFighterOutputBoundary {
    private final SavedFightersViewModel viewModel;

    public LoadFighterPresenter(SavedFightersViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(LoadFighterOutputData outputData) {
        final SavedFightersState state = viewModel.getState();
        state.setLoadedFighterDetails(formatDetails(outputData));
        state.setError("");
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final SavedFightersState state = viewModel.getState();
        state.setError(errorMessage);
        state.setLoadedFighterDetails("");
        viewModel.firePropertyChanged();
    }

    private static String formatDetails(LoadFighterOutputData outputData) {
        final StringBuilder text = new StringBuilder();
        text.append(outputData.getFighterName())
                .append(" (").append(outputData.getWeightClassName())
                .append(", ").append(outputData.getRecordText())
                .append(", ").append(outputData.getFinishes()).append(" finishes)");

        String separator = " — ";
        for (Map.Entry<String, Double> entry : outputData.getAttributeValues().entrySet()) {
            text.append(separator)
                    .append(entry.getKey()).append(' ')
                    .append(Math.round(entry.getValue()));
            separator = ", ";
        }
        return text.toString();
    }
}
