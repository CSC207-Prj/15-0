package use_case.reroll_fighter;

import entity.Attribute;
import entity.RandomSource;
import entity.RealFighter;
import entity.UfcEra;
import entity.WeightClass;
import org.junit.Test;
import use_case.fighter_creation.FighterDataAccessInterface;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class RerollFighterInteractorTest {

    private static class FakeFighterDataAccess
            implements FighterDataAccessInterface {

        private final List<RealFighter> fighters;

        FakeFighterDataAccess(List<RealFighter> fighters) {
            this.fighters = fighters;
        }

        @Override
        public List<RealFighter> getFighters() {
            return fighters;
        }
    }

    private static class FixedRandomSource implements RandomSource {

        @Override
        public double nextDouble() {
            return 0.0;
        }

        @Override
        public int nextInt(int bound) {
            return 0;
        }
    }

    @Test
    public void rerollSelectsDifferentFighterAndConsumesReroll() {
        final RealFighter currentFighter =
                createFighter("Current Fighter");

        final RealFighter otherFighter =
                createFighter("Other Fighter");

        final FighterDataAccessInterface fighterDataAccess =
                new FakeFighterDataAccess(
                        Arrays.asList(currentFighter, otherFighter));

        final RerollFighterOutputBoundary presenter =
                new RerollFighterOutputBoundary() {
                    @Override
                    public void prepareSuccessView(
                            RerollFighterOutputData outputData) {
                        assertEquals(otherFighter, outputData.getFighter());
                        assertEquals(0, outputData.getRerollsLeft());
                    }

                    @Override
                    public void prepareFailView(String errorMessage) {
                        fail();
                    }
                };

        final RerollFighterInteractor interactor =
                new RerollFighterInteractor(
                        new FixedRandomSource(),
                        fighterDataAccess,
                        presenter);

        interactor.execute(
                new RerollFighterInputData(
                        UfcEra.MODERN,
                        1,
                        currentFighter));
    }

    private static RealFighter createFighter(String name) {
        return new RealFighter(
                name,
                WeightClass.LIGHTWEIGHT,
                1,
                UfcEra.MODERN,
                "10-0",
                new HashMap<Attribute, Double>());
    }
}