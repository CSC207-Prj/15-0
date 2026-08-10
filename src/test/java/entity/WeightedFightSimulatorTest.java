package entity;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;
import java.util.ArrayDeque;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WeightedFightSimulatorTest {
    @Test
    void simulatorProducesValidResult() {
        final RandomSource deterministicRandom = new RandomSource() {
            @Override
            public double nextDouble() {
                return 0.20;
            }

            @Override
            public int nextInt(int bound) {
                return 0;
            }
        };

        final WeightedFightSimulator simulator =
                new WeightedFightSimulator(deterministicRandom);

        final CustomFighter player = new CustomFighter(
                "Player",
                WeightClass.LIGHTWEIGHT,
                new FighterRecord(),
                attributes(90.0));

        final RealFighter opponent = new RealFighter(
                "Opponent",
                WeightClass.LIGHTWEIGHT,
                15,
                UfcEra.values()[UfcEra.values().length - 1],
                "20-4",
                attributes(80.0));

        final FightResult result =
                simulator.simulate(player, opponent, 3, Difficulty.NORMAL);

        assertNotNull(result);
        assertEquals(opponent, result.getOpponent());
        assertEquals(3, result.getRound());
        assertEquals(FightMethod.DECISION, result.getMethod());
    }

    @Test
    void deterministicRandomnessCoversSubmissionAndKnockoutFinishes() {
        final CustomFighter player = custom("Player", 90.0);
        final RealFighter opponent = real("Opponent", 60.0);

        final FightResult submission = new WeightedFightSimulator(
                new SequenceRandom(0.0, 0.99, 0.0))
                .simulate(player, opponent, 5, Difficulty.EASY);
        assertTrue(submission.isPlayerWon());
        assertEquals(FightMethod.SUBMISSION, submission.getMethod());
        assertEquals(1, submission.getRound());
        assertEquals(1, submission.getSecondsInRound());

        final FightResult knockout = new WeightedFightSimulator(
                new SequenceRandom(0.99, 0.99, 0.99))
                .simulate(player, opponent, 1, Difficulty.HARD);
        assertFalse(knockout.isPlayerWon());
        assertEquals(FightMethod.KO_TKO, knockout.getMethod());
    }

    @Test
    void probabilityClampsAndAllDifficultiesRemainDeterministic() {
        final FightResult strong = new WeightedFightSimulator(
                new SequenceRandom(0.89, 0.0))
                .simulate(custom("Strong", 100.0), real("Weak", 0.0),
                        3, Difficulty.NORMAL);
        assertTrue(strong.isPlayerWon());

        final FightResult weak = new WeightedFightSimulator(
                new SequenceRandom(0.11, 0.0))
                .simulate(custom("Weak", 0.0), real("Strong", 100.0),
                        3, Difficulty.NORMAL);
        assertFalse(weak.isPlayerWon());
    }

    @Test
    void simulatorRejectsInvalidArguments() {
        final CustomFighter player = custom("Player", 50.0);
        final RealFighter opponent = real("Opponent", 50.0);
        final WeightedFightSimulator simulator =
                new WeightedFightSimulator(new SequenceRandom(0.0));

        assertThrows(NullPointerException.class,
                () -> new WeightedFightSimulator(null));
        assertThrows(NullPointerException.class,
                () -> simulator.simulate(null, opponent, 3, Difficulty.NORMAL));
        assertThrows(NullPointerException.class,
                () -> simulator.simulate(player, null, 3, Difficulty.NORMAL));
        assertThrows(NullPointerException.class,
                () -> simulator.simulate(player, opponent, 3, null));
        assertThrows(IllegalArgumentException.class,
                () -> simulator.simulate(player, opponent, 0, Difficulty.NORMAL));
        assertThrows(IllegalArgumentException.class,
                () -> simulator.simulate(player, opponent, 6, Difficulty.NORMAL));
    }

    private CustomFighter custom(String name, double value) {
        return new CustomFighter(
                name, WeightClass.LIGHTWEIGHT,
                new FighterRecord(), attributes(value));
    }

    private RealFighter real(String name, double value) {
        return new RealFighter(
                name, WeightClass.LIGHTWEIGHT, 1,
                UfcEra.MODERN, "10-0", attributes(value));
    }

    private static final class SequenceRandom implements RandomSource {
        private final ArrayDeque<Double> doubles = new ArrayDeque<>();

        private SequenceRandom(double... values) {
            for (double value : values) {
                doubles.add(value);
            }
        }

        @Override
        public double nextDouble() {
            return doubles.removeFirst();
        }

        @Override
        public int nextInt(int bound) {
            return 0;
        }
    }

    private Map<Attribute, Double> attributes(double value) {
        final Map<Attribute, Double> attributes = new EnumMap<>(Attribute.class);
        for (Attribute attribute : Attribute.values()) {
            attributes.put(attribute, value);
        }
        return attributes;
    }
}
