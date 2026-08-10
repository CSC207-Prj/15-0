package interface_adapter.game_setting;

import entity.Attribute;
import entity.CustomFighter;
import entity.Difficulty;
import entity.GameSettings;
import entity.RealFighter;
import entity.UfcEra;
import entity.WeightClass;
import org.junit.jupiter.api.Test;
import use_case.game_setting.GameSettingInputData;
import use_case.game_setting.GameSettingOutputData;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameSettingAdaptersTest {

    @Test
    void controllerBuildsInputData() {
        final GameSettingInputData[] captured = {null};
        new GameSettingController(input -> captured[0] = input)
                .execute(Difficulty.HARD, 5, UfcEra.MODERN, true);

        assertEquals(Difficulty.HARD, captured[0].getDifficulty());
        assertEquals(5, captured[0].getRoundsPerFight());
        assertEquals(UfcEra.MODERN, captured[0].getEra());
        assertTrue(captured[0].isHideOpponentStats());
    }

    @Test
    void presenterStoresSuccessAndFailureStateAndFires() {
        final GameSettingViewModel viewModel = new GameSettingViewModel();
        final int[] events = {0};
        viewModel.addPropertyChangeListener(event -> events[0]++);
        final GameSettingPresenter presenter = new GameSettingPresenter(viewModel);
        final GameSettings settings = new GameSettings(
                Difficulty.EASY, 3, UfcEra.ALL_TIME, false);
        final CustomFighter fighter = new CustomFighter("Draft");
        final RealFighter eligible = realFighter();

        presenter.prepareSuccessView(
                new GameSettingOutputData(settings, fighter, List.of(eligible)));
        assertTrue(viewModel.getState().isConfigured());
        assertEquals(settings, viewModel.getState().getSettings());
        assertEquals(fighter, viewModel.getState().getCustomFighter());
        assertEquals(List.of(eligible),
                viewModel.getState().getEligibleFighters());
        assertEquals("", viewModel.getState().getErrorMessage());

        presenter.prepareFailView("invalid");
        assertFalse(viewModel.getState().isConfigured());
        assertEquals("invalid", viewModel.getState().getErrorMessage());
        assertEquals(2, events[0]);
    }

    @Test
    void stateCopiesFighterListsAndViewModelHandlesNullState() {
        final GameSettingState state = new GameSettingState();
        final java.util.ArrayList<RealFighter> fighters =
                new java.util.ArrayList<>(List.of(realFighter()));
        state.setEligibleFighters(fighters);
        fighters.clear();
        final List<RealFighter> returned = state.getEligibleFighters();
        returned.clear();
        assertEquals(1, state.getEligibleFighters().size());
        assertNotSame(returned, state.getEligibleFighters());

        final GameSettingViewModel viewModel = new GameSettingViewModel();
        final int[] events = {0};
        viewModel.addPropertyChangeListener(event -> events[0]++);
        viewModel.setState(state);
        assertEquals(state, viewModel.getState());
        viewModel.setState(null);
        assertFalse(viewModel.getState().isConfigured());
        viewModel.firePropertyChanged();
        assertEquals(3, events[0]);
        assertEquals(GameSettingViewModel.VIEW_NAME, viewModel.getViewName());
    }

    private static RealFighter realFighter() {
        final Map<Attribute, Double> attributes =
                new EnumMap<>(Attribute.class);
        for (Attribute attribute : Attribute.values()) {
            attributes.put(attribute, 70.0);
        }
        return new RealFighter(
                "Alpha", WeightClass.LIGHTWEIGHT, 1,
                UfcEra.MODERN, "10-0", attributes);
    }
}
