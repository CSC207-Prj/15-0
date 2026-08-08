package entity;

import java.util.List;
import java.util.Objects;

/** Shared representation of a UFC division and the ranked fighters associated with it. */
public final class Division {
    private final WeightClass weightClass;
    private final List<RealFighter> rankedFighters;

    public Division(WeightClass weightClass, List<RealFighter> rankedFighters) {
        this.weightClass = Objects.requireNonNull(weightClass, "weightClass");
        this.rankedFighters = List.copyOf(Objects.requireNonNull(rankedFighters, "rankedFighters"));
    }

    public WeightClass getWeightClass() {
        return weightClass;
    }

    public List<RealFighter> getRankedFighters() {
        return rankedFighters;
    }
}
