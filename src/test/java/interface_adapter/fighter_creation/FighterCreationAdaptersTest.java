package interface_adapter.fighter_creation;

import entity.Attribute;
import entity.CustomFighter;
import entity.Difficulty;
import entity.GameSettings;
import entity.RealFighter;
import entity.UfcEra;
import entity.WeightClass;
import interface_adapter.assign_attribute.AssignAttributeController;
import org.junit.jupiter.api.Test;
import use_case.assign_attribute.AssignAttributeInputData;
import use_case.assign_attribute.AssignAttributeOutputData;
import use_case.reroll_fighter.RerollFighterInputData;
import use_case.reroll_fighter.RerollFighterOutputData;
import use_case.spin_fighter.SpinFighterInputData;
import use_case.spin_fighter.SpinFighterOutputData;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FighterCreationAdaptersTest {

    @Test
    void viewModelInitializesAndResetsRunState() {
        final FighterCreationViewModel viewModel =
                new FighterCreationViewModel();
        final int[] events = {0};
        viewModel.addPropertyChangeListener(event -> events[0]++);
        final GameSettings settings = new GameSettings(
                Difficulty.EASY, 3, UfcEra.MODERN, false);
        final CustomFighter fighter = new CustomFighter("Draft");

        viewModel.setRolledFighter(
                "Old", "details", 88, Map.of("Striking", 88));
        viewModel.setAssignedAttribute("Striking", 88, "Old");
        viewModel.setCurrentFighter(realFighter("Old"));
        viewModel.setErrorMessage("old error");
        viewModel.initializeRun(settings, fighter);

        assertEquals(settings, viewModel.getGameSettings());
        assertEquals(fighter, viewModel.getCustomFighter());
        assertEquals(Difficulty.EASY.getRerollLimit(),
                viewModel.getRerollsLeft());
        assertEquals("", viewModel.getFighterName());
        assertEquals("", viewModel.getFighterDetails());
        assertEquals(0, viewModel.getOverall());
        assertNull(viewModel.getCurrentFighter());
        assertEquals("", viewModel.getErrorMessage());
        assertFalse(viewModel.isFighterRevealed());
        assertTrue(viewModel.getFighterStats().isEmpty());
        assertTrue(viewModel.getAssignedValues().isEmpty());
        assertTrue(viewModel.getAssignedFighters().isEmpty());
        assertTrue(events[0] >= 4);
    }

    @Test
    void viewModelStoresRolledAndAssignedDataAsReadOnlyMaps() {
        final FighterCreationViewModel viewModel =
                new FighterCreationViewModel();
        viewModel.setRolledFighter(
                "Alpha", "10-0", 91, Map.of("Cardio", 91));
        viewModel.setAssignedAttribute("Cardio", 91, "Alpha");
        viewModel.setRerollsLeft(2);

        assertEquals("Alpha", viewModel.getFighterName());
        assertEquals("10-0", viewModel.getFighterDetails());
        assertEquals(91, viewModel.getOverall());
        assertEquals(91, viewModel.getFighterStats().get("Cardio"));
        assertEquals(91, viewModel.getAssignedValues().get("Cardio"));
        assertEquals("Alpha", viewModel.getAssignedFighters().get("Cardio"));
        assertEquals(1, viewModel.getAttributesFilled());
        assertEquals(2, viewModel.getRerollsLeft());
        assertTrue(viewModel.isFighterRevealed());
        assertThrows(UnsupportedOperationException.class,
                () -> viewModel.getFighterStats().put("Reach", 2));

        viewModel.setRolledFighter(
                "Bravo", "9-1", Map.of("Reach", 80));
        assertEquals("Bravo", viewModel.getFighterName());
        assertEquals(80, viewModel.getFighterStats().get("Reach"));
    }

    @Test
    void spinRerollAndAssignPresentersMapFighterState() {
        final FighterCreationViewModel viewModel =
                new FighterCreationViewModel();
        final RealFighter fighter = realFighter("Alpha");

        new SpinFighterPresenter(viewModel).prepareSuccessView(
                new SpinFighterOutputData(fighter));
        assertEquals(fighter, viewModel.getCurrentFighter());
        assertEquals("Alpha", viewModel.getFighterName());
        assertEquals("10-0 • Lightweight", viewModel.getFighterDetails());
        assertEquals(76, viewModel.getFighterStats().get("Striking"));

        final RealFighter rerolled = realFighter("Bravo");
        final RerollFighterPresenter rerollPresenter =
                new RerollFighterPresenter(viewModel);
        rerollPresenter.prepareSuccessView(
                new RerollFighterOutputData(rerolled, 1));
        assertEquals(rerolled, viewModel.getCurrentFighter());
        assertEquals(1, viewModel.getRerollsLeft());
        rerollPresenter.prepareFailView("none left");
        assertEquals(0, viewModel.getRerollsLeft());

        final AssignAttributePresenter assignPresenter =
                new AssignAttributePresenter(viewModel);
        assignPresenter.prepareSuccessView(new AssignAttributeOutputData(
                Attribute.CARDIO, 89.6, "Bravo"));
        assertEquals(90, viewModel.getAssignedValues().get("Cardio"));
        assertEquals("Bravo", viewModel.getAssignedFighters().get("Cardio"));
        assertEquals("", viewModel.getErrorMessage());
        assignPresenter.prepareFailView("duplicate");
        assertEquals("duplicate", viewModel.getErrorMessage());
    }

    @Test
    void creationControllersForwardTheirInputData() {
        final SpinFighterInputData[] spin = {null};
        new SpinFighterController(input -> spin[0] = input)
                .execute(UfcEra.EARLY_UFC);
        assertEquals(UfcEra.EARLY_UFC, spin[0].getEra());

        final RerollFighterInputData[] reroll = {null};
        final RealFighter current = realFighter("Current");
        new RerollFighterController(input -> reroll[0] = input)
                .execute(UfcEra.MODERN, 2, current);
        assertEquals(2, reroll[0].getRerollsLeft());
        assertEquals(current, reroll[0].getCurrentFighter());

        final AssignAttributeInputData[] assign = {null};
        final CustomFighter custom = new CustomFighter("Draft");
        new AssignAttributeController(input -> assign[0] = input)
                .execute(custom, current, Attribute.REACH, UfcEra.MODERN);
        assertEquals(custom, assign[0].getCustomFighter());
        assertEquals(current, assign[0].getRealFighter());
        assertEquals(Attribute.REACH, assign[0].getAttribute());
        assertEquals(UfcEra.MODERN, assign[0].getEra());
    }

    private static RealFighter realFighter(String name) {
        final Map<Attribute, Double> attributes =
                new EnumMap<>(Attribute.class);
        for (Attribute attribute : Attribute.values()) {
            attributes.put(attribute, 75.6);
        }
        return new RealFighter(
                name, WeightClass.LIGHTWEIGHT, 1,
                UfcEra.MODERN, "10-0", attributes);
    }
}
