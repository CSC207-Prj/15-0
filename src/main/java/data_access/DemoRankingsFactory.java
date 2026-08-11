package data_access;

import entity.Attribute;
import entity.Division;
import entity.RealFighter;
import entity.UfcEra;
import entity.WeightClass;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Creates deterministic ranked divisions when live Cito rankings are unavailable.
 */
public final class DemoRankingsFactory {
    private DemoRankingsFactory() {
    }

    public static Division createDivision(WeightClass weightClass) {
        final List<RealFighter> fighters = new ArrayList<>();

        for (int rank = 1; rank <= 15; rank++) {
            final double base = 96.0 - (rank - 1) * 1.65;
            final Map<Attribute, Double> attributes = new EnumMap<>(Attribute.class);

            for (Attribute attribute : Attribute.values()) {
                final double variation = ((attribute.ordinal() * 3 + rank) % 7) - 3;
                attributes.put(attribute, clamp(base + variation));
            }

            final UfcEra era = UfcEra.values()[UfcEra.values().length - 1];
            fighters.add(new RealFighter(
                    weightClass.getDisplayName() + " Contender " + rank,
                    weightClass,
                    rank,
                    era,
                    (24 - rank / 2) + "-" + (2 + rank / 4),
                    attributes));
        }

        return new Division(weightClass, fighters);
    }

    private static double clamp(double value) {
        return Math.max(0.0, Math.min(100.0, value));
    }
}
