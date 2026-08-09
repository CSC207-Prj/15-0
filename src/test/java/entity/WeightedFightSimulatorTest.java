package entity;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    private Map<Attribute, Double> attributes(double value) {
        final Map<Attribute, Double> attributes = new EnumMap<>(Attribute.class);
        for (Attribute attribute : Attribute.values()) {
            attributes.put(attribute, value);
        }
        return attributes;
    }
}
