package use_case.reroll_fighter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.Attribute;
import entity.RandomSource;
import entity.RealFighter;
import entity.UfcEra;
import entity.WeightClass;
import use_case.fighter_creation.FighterDataAccessInterface;
import use_case.fighter_creation.FighterDetailsDataAccessInterface;

public class RerollFighterInteractorTest {

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

    @Test
    public void rerollFailsWhenNoFighterHasBeenSpun() {
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
                                "Spin a fighter before rerolling.",
                                errorMessage);
                    }
                };

        new RerollFighterInteractor(
                new FixedRandomSource(),
                () -> {
                    fail("Fighters should not be loaded.");
                    return List.of();
                },
                presenter).execute(new RerollFighterInputData(
                        UfcEra.ALL_TIME, 1, null));
    }

    @Test
    public void rerollHydratesSelectionAndAllowsAllTimePool() {
        final RealFighter current = createFighter("Current Fighter");
        final RealFighter basic = new RealFighter(
                "Early Fighter", WeightClass.WELTERWEIGHT, 0,
                UfcEra.EARLY_UFC, "2-1",
                new HashMap<Attribute, Double>());
        final RealFighter detailed = new RealFighter(
                "Detailed Early Fighter", WeightClass.WELTERWEIGHT, 0,
                UfcEra.EARLY_UFC, "20-1",
                new HashMap<Attribute, Double>());
        final FighterDetailsDataAccessInterface details = fighter -> {
            assertEquals(basic, fighter);
            return detailed;
        };

        new RerollFighterInteractor(
                new FixedRandomSource(),
                () -> List.of(current, basic),
                details,
                new RerollFighterOutputBoundary() {
                    @Override
                    public void prepareSuccessView(
                            RerollFighterOutputData outputData) {
                        assertEquals(detailed, outputData.getFighter());
                        assertEquals(1, outputData.getRerollsLeft());
                    }

                    @Override
                    public void prepareFailView(String errorMessage) {
                        fail(errorMessage);
                    }
                }).execute(new RerollFighterInputData(
                        UfcEra.ALL_TIME, 2, current));
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

    private static final class FixedRandomSource implements RandomSource {

        @Override
        public double nextDouble() {
            return 0.0;
        }

        @Override
        public int nextInt(int bound) {
            return 0;
        }
    }
}
