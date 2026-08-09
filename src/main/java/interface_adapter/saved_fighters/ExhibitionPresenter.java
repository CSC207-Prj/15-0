package interface_adapter.saved_fighters;

import use_case.exhibition.ExhibitionOutputBoundary;
import use_case.exhibition.ExhibitionOutputData;

/**
 * Presenter for the Exhibition Match use case: formats the raw result facts
 * into the one-line summary the Saved Fighters view shows.
 */
public class ExhibitionPresenter implements ExhibitionOutputBoundary {
    private static final String DECISION = "Decision";
    private static final int SECONDS_PER_MINUTE = 60;

    private final SavedFightersViewModel viewModel;

    public ExhibitionPresenter(SavedFightersViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ExhibitionOutputData outputData) {
        final SavedFightersState state = viewModel.getState();
        state.setExhibitionResult(formatResult(outputData));
        state.setError("");
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final SavedFightersState state = viewModel.getState();
        state.setError(errorMessage);
        state.setExhibitionResult("");
        viewModel.firePropertyChanged();
    }

    private static String formatResult(ExhibitionOutputData outputData) {
        final StringBuilder text = new StringBuilder();
        text.append(outputData.getWinnerName())
                .append(" def. ")
                .append(outputData.getLoserName())
                .append(" by ")
                .append(outputData.getMethod());
        if (!DECISION.equals(outputData.getMethod())) {
            text.append(" in round ").append(outputData.getRound())
                    .append(" (").append(formatTime(outputData.getSecondsInRound())).append(')');
        }
        return text.toString();
    }

    private static String formatTime(int totalSeconds) {
        final int minutes = totalSeconds / SECONDS_PER_MINUTE;
        final int seconds = totalSeconds % SECONDS_PER_MINUTE;
        return String.format("%d:%02d", minutes, seconds);
    }
}
