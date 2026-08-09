package use_case.assign_attribute;

import entity.Attribute;
import entity.CustomFighter;
import entity.RealFighter;
import entity.UfcEra;
import entity.WeightClass;
import org.junit.Test;
import use_case.spin_fighter.SpinFighterInputBoundary;
import use_case.spin_fighter.SpinFighterInputData;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AssignAttributeInteractorTest {

    private static class FakeSpinFighterInteractor
            implements SpinFighterInputBoundary {

        private boolean executed;
        private UfcEra era;

        @Override
        public void execute(SpinFighterInputData inputData) {
            executed = true;
            era = inputData.getEra();
        }
    }

    @Test
    public void assignsAttributeAndSpinsNextFighter() {
        final Map<Attribute, Double> stats = new HashMap<>();
        stats.put(Attribute.TAKEDOWN, 96.0);

        final RealFighter realFighter = new RealFighter(
                "Test Fighter",
                WeightClass.LIGHTWEIGHT,
                1,
                UfcEra.MODERN,
                "10-0",
                stats);

        final CustomFighter customFighter =
                new CustomFighter("Custom Fighter");

        final AssignAttributeOutputBoundary presenter =
                new AssignAttributeOutputBoundary() {
                    @Override
                    public void prepareSuccessView(
                            AssignAttributeOutputData outputData) {
                        assertEquals(Attribute.TAKEDOWN, outputData.getAttribute());
                        assertEquals(96.0, outputData.getValue(), 0.001);
                        assertEquals("Test Fighter", outputData.getFighterName());
                    }

                    @Override
                    public void prepareFailView(String errorMessage) {
                        fail();
                    }
                };

        final FakeSpinFighterInteractor spinFighterInteractor =
                new FakeSpinFighterInteractor();

        final AssignAttributeInteractor interactor =
                new AssignAttributeInteractor(
                        presenter,
                        spinFighterInteractor);

        interactor.execute(new AssignAttributeInputData(
                customFighter,
                realFighter,
                Attribute.TAKEDOWN,
                UfcEra.MODERN));

        assertEquals(
                96.0,
                customFighter.getAttribute(Attribute.TAKEDOWN),
                0.001);

        assertTrue(spinFighterInteractor.executed);
        assertEquals(UfcEra.MODERN, spinFighterInteractor.era);
    }
}