package use_case.reroll_fighter;

import entity.Attribute;
import entity.RandomSource;
import entity.RealFighter;
import entity.UfcEra;
import entity.WeightClass;
import org.junit.jupiter.api.Test;
import use_case.fighter_creation.FighterDataAccessInterface;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
                        fail("Reroll should succeed.");
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

    @Test
    public void rerollFailsWhenNoRerollsRemain() {
        final FighterDataAccessInterface fighterDataAccess =
                new FighterDataAccessInterface() {
                    @Override
                    public List<RealFighter> getFighters() {
                        fail("Fighter data should not be accessed.");
                        return null;
                    }
                };

        final RandomSource randomSource =
                new RandomSource() {
                    @Override
                    public double nextDouble() {
                        fail("Random source should not be used.");
                        return 0.0;
                    }

                    @Override
                    public int nextInt(int bound) {
                        fail("Random source should not be used.");
                        return 0;
                    }
                };

        final RerollFighterOutputBoundary presenter =
                new RerollFighterOutputBoundary() {
                    @Override
                    public void prepareSuccessView(
                            RerollFighterOutputData outputData) {
                        fail("Reroll should not succeed.");
                    }

                    @Override
                    public void prepareFailView(String errorMessage) {
                        assertEquals(
                                "No rerolls remaining.",
                                errorMessage);
                    }
                };

        final RerollFighterInteractor interactor =
                new RerollFighterInteractor(
                        randomSource,
                        fighterDataAccess,
                        presenter);

        interactor.execute(
                new RerollFighterInputData(
                        UfcEra.MODERN,
                        0,
                        createFighter("Current Fighter")));
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
