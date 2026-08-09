package entity;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/** Shared fighter state used by both real and player-built fighters. */
public abstract class Fighter {
    private String name;
    private WeightClass weightClass;
    private final FighterRecord record;
    private final EnumMap<Attribute, Double> attributes;

    protected Fighter(String name,
                      WeightClass weightClass,
                      FighterRecord record,
                      Map<Attribute, Double> attributes) {
        this.name = requireName(name);
        this.weightClass = weightClass;
        this.record = Objects.requireNonNull(record, "record");
        this.attributes = new EnumMap<>(Attribute.class);
        if (attributes != null) {
            attributes.forEach(this::setAttribute);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = requireName(name);
    }

    public WeightClass getWeightClass() {
        return weightClass;
    }

    public void setWeightClass(WeightClass weightClass) {
        this.weightClass = Objects.requireNonNull(weightClass, "weightClass");
    }

    public FighterRecord getRecord() {
        return record;
    }

    public double getAttribute(Attribute attribute) {
        final Double value = attributes.get(attribute);
        if (value == null) {
            throw new IllegalStateException(attribute.getDisplayName() + " has not been assigned.");
        }
        return value;
    }

    public boolean hasAttribute(Attribute attribute) {
        return attributes.containsKey(attribute);
    }

    /** Reports whether every one of the six attributes has been assigned a value. */
    public boolean hasAllAttributes() {
        return attributes.size() == Attribute.values().length;
    }

    public Map<Attribute, Double> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    /** Raw entity state operation used by later fighter-building and restore workflows. */
    protected final void setAttribute(Attribute attribute, double value) {
        Objects.requireNonNull(attribute, "attribute");
        if (value < 0.0 || value > 100.0 || Double.isNaN(value)) {
            throw new IllegalArgumentException("Attribute values must be between 0 and 100.");
        }
        attributes.put(attribute, value);
    }

    private static String requireName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Fighter name cannot be blank.");
        }
        return name.trim();
    }
}
