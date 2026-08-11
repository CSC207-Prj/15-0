package use_case.assign_attribute;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import entity.Attribute;
import entity.CustomFighter;
import entity.RealFighter;
import entity.UfcEra;
import entity.WeightClass;
import use_case.spin_fighter.SpinFighterInputBoundary;
import use_case.spin_fighter.SpinFighterInputData;

public class AssignAttributeInteractorTest {

    @Test
    public void assignsAttributeAndSpinsNextFighter() {
        final Map<Attribute, Double> stats = new HashMap<>();
        stats.put(Attribute.TAKEDOWN, 96.0);

        final RealFighter realFighter =
                createFighter("Test Fighter", stats);

        final CustomFighter customFighter =
                new CustomFighter("Custom Fighter");

        final FakeSpinFighterInteractor spinFighterInteractor =
                new FakeSpinFighterInteractor();

        final AssignAttributeOutputBoundary presenter =
                new AssignAttributeOutputBoundary() {
                    @Override
                    public void prepareSuccessView(
                            AssignAttributeOutputData outputData) {
                        assertEquals(
                                Attribute.TAKEDOWN,
                                outputData.getAttribute());
                        assertEquals(
                                96.0,
                                outputData.getValue(),
                                0.001);
                        assertEquals(
                                "Test Fighter",
                                outputData.getFighterName());
                    }

                    @Override
                    public void prepareFailView(String errorMessage) {
                        fail("Assignment should succeed.");
                    }
                };

        final AssignAttributeInteractor interactor =
                new AssignAttributeInteractor(
                        presenter,
                        spinFighterInteractor);

        interactor.execute(
                new AssignAttributeInputData(
                        customFighter,
                        realFighter,
                        Attribute.TAKEDOWN,
                        UfcEra.MODERN));

        assertEquals(
                96.0,
                customFighter.getAttribute(Attribute.TAKEDOWN),
                0.001);

        assertTrue(spinFighterInteractor.executed);
        assertEquals(
                UfcEra.MODERN,
                spinFighterInteractor.era);
    }

    @Test
    public void assigningExistingAttributeFailsWithoutSpinning() {
        final CustomFighter customFighter =
                new CustomFighter("Custom Fighter");

        customFighter.assignAttribute(
                Attribute.TAKEDOWN,
                50.0);

        final Map<Attribute, Double> stats = new HashMap<>();
        stats.put(Attribute.TAKEDOWN, 96.0);

        final RealFighter realFighter =
                createFighter("Test Fighter", stats);

        final FakeSpinFighterInteractor spinFighterInteractor =
                new FakeSpinFighterInteractor();

        final AssignAttributeOutputBoundary presenter =
                new AssignAttributeOutputBoundary() {
                    @Override
                    public void prepareSuccessView(
                            AssignAttributeOutputData outputData) {
                        fail("Assignment should not succeed.");
                    }

                    @Override
                    public void prepareFailView(String errorMessage) {
                        assertEquals(
                                "That attribute is already assigned.",
                                errorMessage);
                    }
                };

        final AssignAttributeInteractor interactor =
                new AssignAttributeInteractor(
                        presenter,
                        spinFighterInteractor);

        interactor.execute(
                new AssignAttributeInputData(
                        customFighter,
                        realFighter,
                        Attribute.TAKEDOWN,
                        UfcEra.MODERN));

        assertEquals(
                50.0,
                customFighter.getAttribute(Attribute.TAKEDOWN),
                0.001);

        assertFalse(spinFighterInteractor.executed);
    }

    @Test
    public void assigningFinalAttributeDoesNotSpinNextFighter() {
        final CustomFighter customFighter =
                new CustomFighter("Custom Fighter");

        customFighter.assignAttribute(Attribute.STRIKING, 80.0);
        customFighter.assignAttribute(Attribute.DEFENSE, 81.0);
        customFighter.assignAttribute(Attribute.TAKEDOWN, 82.0);
        customFighter.assignAttribute(Attribute.HEIGHT, 83.0);
        customFighter.assignAttribute(Attribute.REACH, 84.0);

        final Map<Attribute, Double> stats = new HashMap<>();
        stats.put(Attribute.CARDIO, 90.0);

        final RealFighter realFighter =
                createFighter("Final Fighter", stats);

        final FakeSpinFighterInteractor spinFighterInteractor =
                new FakeSpinFighterInteractor();

        final AssignAttributeOutputBoundary presenter =
                new AssignAttributeOutputBoundary() {
                    @Override
                    public void prepareSuccessView(
                            AssignAttributeOutputData outputData) {
                        assertEquals(
                                Attribute.CARDIO,
                                outputData.getAttribute());
                        assertEquals(
                                90.0,
                                outputData.getValue(),
                                0.001);
                    }

                    @Override
                    public void prepareFailView(String errorMessage) {
                        fail("Final assignment should succeed.");
                    }
                };

        final AssignAttributeInteractor interactor =
                new AssignAttributeInteractor(
                        presenter,
                        spinFighterInteractor);

        interactor.execute(
                new AssignAttributeInputData(
                        customFighter,
                        realFighter,
                        Attribute.CARDIO,
                        UfcEra.MODERN));

        assertTrue(customFighter.hasAllAttributes());

        assertEquals(
                90.0,
                customFighter.getAttribute(Attribute.CARDIO),
                0.001);

        assertFalse(spinFighterInteractor.executed);
    }

    private static RealFighter createFighter(
            String name,
            Map<Attribute, Double> stats) {

        return new RealFighter(
                name,
                WeightClass.LIGHTWEIGHT,
                1,
                UfcEra.MODERN,
                "10-0",
                stats);
    }

    private static final class FakeSpinFighterInteractor
            implements SpinFighterInputBoundary {

        private boolean executed;
        private UfcEra era;

        @Override
        public void execute(SpinFighterInputData inputData) {
            executed = true;
            era = inputData.getEra();
        }
    }
}
