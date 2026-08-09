package use_case.assign_attribute;

import entity.Attribute;
import entity.CustomFighter;
import entity.RealFighter;
import entity.UfcEra;
import entity.WeightClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AssignAttributeInteractorTest {

    @Test
    public void assignsSelectedAttributeToCustomFighter() {
        final Map<Attribute, Double> stats = new HashMap<>();
        stats.put(Attribute.TAKEDOWN, 96.0);

        final RealFighter realFighter = new RealFighter(
                "Test Fighter",
                WeightClass.LIGHTWEIGHT,
                1,
                UfcEra.MODERN,
                "10-0",
                stats);

        final CustomFighter customFighter = new CustomFighter("Custom Fighter");

        final AssignAttributeOutputBoundary presenter =
                new AssignAttributeOutputBoundary() {
                    @Override
                    public void prepareSuccessView(AssignAttributeOutputData outputData) {
                        assertEquals(Attribute.TAKEDOWN, outputData.getAttribute());
                        assertEquals(96.0, outputData.getValue(), 0.001);
                        assertEquals("Test Fighter", outputData.getFighterName());
                    }

                    @Override
                    public void prepareFailView(String errorMessage) {
                        fail();
                    }
                };

        final AssignAttributeInteractor interactor =
                new AssignAttributeInteractor(presenter);

        interactor.execute(new AssignAttributeInputData(
                customFighter,
                realFighter,
                Attribute.TAKEDOWN));

        assertEquals(96.0,
                customFighter.getAttribute(Attribute.TAKEDOWN),
                0.001);
    }
}