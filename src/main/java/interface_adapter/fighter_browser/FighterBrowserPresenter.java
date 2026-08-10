package interface_adapter.fighter_browser;

import entity.Attribute;
import use_case.browse_fighters.BrowseFightersOutputBoundary;
import use_case.browse_fighters.BrowseFightersOutputData;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Presenter for the Fighter Browser.
 */
public class FighterBrowserPresenter implements BrowseFightersOutputBoundary {
    private final FighterBrowserViewModel viewModel;

    public FighterBrowserPresenter(FighterBrowserViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(BrowseFightersOutputData outputData) {
        final FighterBrowserState state = new FighterBrowserState();
        final List<FighterBrowserRow> rows = new ArrayList<>();

        for (BrowseFightersOutputData.FighterSummary fighter
                : outputData.getFighters()) {
            rows.add(new FighterBrowserRow(
                    fighter.getName(),
                    fighter.getWeightClass() + " • " + fighter.getEra()));
        }
        state.setRows(rows);
        state.setResultText(rows.size() == 1
                ? "1 fighter found"
                : rows.size() + " fighters found");

        final BrowseFightersOutputData.FighterProfile selected =
                outputData.getSelectedFighter();

        if (selected == null) {
            state.setSelectedName("No fighter found");
            state.setSelectedDetails(
                    "Try changing the search text or filters.");
            state.setRankText("");
            state.setErrorMessage("");
            viewModel.setState(state);
            viewModel.firePropertyChanged();
            return;
        }

        state.setSelectedName(selected.getName());
        state.setSelectedDetails(
                selected.getWeightClass()
                        + " • " + selected.getEra()
                        + " • Record: " + selected.getProfessionalRecord());
        state.setRankText(selected.getRank() == 0
                ? "Unranked / historical catalogue entry"
                : "Rank #" + selected.getRank());

        final Map<Attribute, Integer> attributes =
                new EnumMap<>(Attribute.class);
        for (Map.Entry<Attribute, Double> entry
                : selected.getAttributes().entrySet()) {
            attributes.put(
                    entry.getKey(),
                    (int) Math.round(entry.getValue()));
        }
        state.setAttributes(attributes);
        state.setErrorMessage("");

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final FighterBrowserState state = new FighterBrowserState();
        state.setSelectedName("Fighter catalogue unavailable");
        state.setSelectedDetails("");
        state.setRankText("");
        state.setResultText("0 fighters found");
        state.setErrorMessage(errorMessage);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}
