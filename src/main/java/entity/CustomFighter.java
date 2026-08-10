package entity;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Player-built fighter assembled from one attribute per source fighter. */
public final class CustomFighter extends Fighter {
    private final EnumMap<Attribute, String> attributeSources =
            new EnumMap<>(Attribute.class);
    private final Set<String> usedSourceFighters = new HashSet<>();

    public CustomFighter(String temporaryName) {
        super(temporaryName, null, new FighterRecord(), Map.of());
    }

    /**
     * Rebuilds a finished fighter, for example when the saved roster is loaded
     * back from disk. Mirrors RealFighter's constructor shape.
     */
    public CustomFighter(String name,
                         WeightClass weightClass,
                         FighterRecord record,
                         Map<Attribute, Double> attributes) {
        super(name, weightClass, record, attributes);
    }

    /**
     * Legacy/raw assignment operation retained for restore and tests.
     * Drafting from a real fighter should use assignAttributeFrom.
     */
    public void assignAttribute(Attribute attribute, double value) {
        setAttribute(attribute, value);
    }

    /**
     * Assigns one stat from a real source fighter.
     *
     * A source fighter may contribute at most one attribute to this draft.
     */
    public void assignAttributeFrom(Attribute attribute,
                                    double value,
                                    String sourceFighterName) {
        if (sourceFighterName == null || sourceFighterName.isBlank()) {
            throw new IllegalArgumentException(
                    "Source fighter name cannot be blank."
            );
        }
        if (hasUsedSourceFighter(sourceFighterName)) {
            throw new IllegalStateException(
                    "That fighter has already contributed an attribute."
            );
        }

        setAttribute(attribute, value);
        final String normalizedName = sourceFighterName.trim();
        attributeSources.put(attribute, normalizedName);
        usedSourceFighters.add(normalizedName);
    }

    public boolean hasUsedSourceFighter(String fighterName) {
        return fighterName != null
                && usedSourceFighters.contains(fighterName.trim());
    }

    public String getAttributeSource(Attribute attribute) {
        return attributeSources.get(attribute);
    }

    public Map<Attribute, String> getAttributeSources() {
        return Collections.unmodifiableMap(attributeSources);
    }

    public Set<String> getUsedSourceFighterNames() {
        return Collections.unmodifiableSet(usedSourceFighters);
    }
}
