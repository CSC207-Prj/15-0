package use_case.spin_fighter;

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
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SpinFighterInteractorTest {

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
}