package use_case.spin_fighter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import entity.Attribute;
import entity.RandomSource;
import entity.RealFighter;
import entity.UfcEra;
import entity.WeightClass;
import use_case.fighter_creation.FighterDataAccessInterface;
import use_case.fighter_creation.FighterDetailsDataAccessInterface;

public class SpinFighterInteractorTest {

    @Test
    public void spinOnlySelectsFighterFromChosenEra() {
        final RealFighter earlyFighter =
                createFighter("Early Fighter", UfcEra.EARLY_UFC);

        final RealFighter modernFighter =
                createFighter("Modern Fighter", UfcEra.MODERN);

        final FighterDataAccessInterface fighterDataAccess =
                new FakeFighterDataAccess(
                        Arrays.asList(earlyFighter, modernFighter));

        final RandomSource randomSource = new FixedRandomSource();

        final SpinFighterOutputBoundary presenter =
                new SpinFighterOutputBoundary() {
                    @Override
                    public void prepareSuccessView(
                            SpinFighterOutputData outputData) {
                        assertEquals(
                                modernFighter,
                                outputData.getFighter());
                    }
                };

        final SpinFighterInteractor interactor =
                new SpinFighterInteractor(
                        randomSource,
                        fighterDataAccess,
                        presenter);

        interactor.execute(
                new SpinFighterInputData(UfcEra.MODERN));
    }

    @Test
    public void spinHydratesSelectedFighterWhenDetailAccessIsAvailable() {
        final RealFighter basic =
                createFighter("Basic Fighter", UfcEra.EARLY_UFC);
        final RealFighter detailed =
                createFighter("Detailed Fighter", UfcEra.EARLY_UFC);
        final FighterDataAccessInterface fighterDataAccess =
                new FakeFighterDataAccess(List.of(basic));
        final FighterDetailsDataAccessInterface detailsDataAccess = fighter -> {
            assertEquals(basic, fighter);
            return detailed;
        };

        final SpinFighterInteractor interactor = new SpinFighterInteractor(
                new FixedRandomSource(),
                fighterDataAccess,
                detailsDataAccess,
                outputData -> {
                    assertEquals(detailed, outputData.getFighter());
                });

        interactor.execute(new SpinFighterInputData(UfcEra.ALL_TIME));
    }

    private static RealFighter createFighter(
            String name, UfcEra era) {

        final Map<Attribute, Double> attributes =
                new HashMap<>();

        return new RealFighter(
                name,
                WeightClass.LIGHTWEIGHT,
                1,
                era,
                "10-0",
                attributes);
    }

    /**
     * Fake fighter data access used to control the available fighter pool.
     */
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

    /**
     * Fixed random source used to make fighter selection predictable.
     */
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
