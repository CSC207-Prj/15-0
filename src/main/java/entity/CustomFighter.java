package entity;

import java.util.Map;

/** Player-built fighter with assignable attributes and a persistent fight record. */
public final class CustomFighter extends Fighter {
    public CustomFighter(String temporaryName) {
        super(temporaryName, null, new FighterRecord(), Map.of());
    }

    /**
     * Rebuilds a finished fighter, for example when the saved roster is loaded
     * back from disk. Mirrors RealFighter's constructor shape.
     * @param name the fighter's name
     * @param weightClass the locked-in weight class, or null if not assigned yet
     * @param record the fighter's win/loss record
     * @param attributes the assigned attribute values
     */
    public CustomFighter(String name,
                         WeightClass weightClass,
                         FighterRecord record,
                         Map<Attribute, Double> attributes) {
        super(name, weightClass, record, attributes);
    }

    public void assignAttribute(Attribute attribute, double value) {
        setAttribute(attribute, value);
    }
}
