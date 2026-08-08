package entity;

import java.util.Map;
import java.util.Objects;

/** Real UFC fighter represented in the game's shared domain model. */
public final class RealFighter extends Fighter {
    private final int rank;
    private final UfcEra era;
    private final String professionalRecord;

    public RealFighter(String name,
                       WeightClass weightClass,
                       int rank,
                       UfcEra era,
                       String professionalRecord,
                       Map<Attribute, Double> attributes) {
        super(name, weightClass, new FighterRecord(), attributes);
        if (rank < 0 || rank > 15) {
            throw new IllegalArgumentException("Rank must be between 0 and 15.");
        }
        this.rank = rank;
        this.era = Objects.requireNonNull(era, "era");
        this.professionalRecord = Objects.requireNonNull(professionalRecord, "professionalRecord");
    }

    public int getRank() {
        return rank;
    }

    public UfcEra getEra() {
        return era;
    }

    public String getProfessionalRecord() {
        return professionalRecord;
    }
}
