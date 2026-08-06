package entity;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * A player-built fighter, as defined in the project blueprint. On top of the
 * shared fighter state it remembers which real fighter each attribute value
 * came from, and how many rerolls are left for the current build.
 *
 * <p>The build-flow behaviour (assignAttribute from a wheel spin, reroll rules)
 * belongs to the fighter-creation use case and is not implemented here; this
 * entity only exposes the raw state those use cases need.</p>
 */
public class CustomFighter extends Fighter {
    /** Placeholder weight class before the weight-class wheel has been spun. */
    public static final String WEIGHT_CLASS_TBD = "TBD";

    private final Map<Attribute, String> attributeSources;
    private int rerollsRemaining;

    public CustomFighter(String name) {
        this(name, WEIGHT_CLASS_TBD);
    }

    public CustomFighter(String name, String weightClass) {
        super(name, weightClass);
        this.attributeSources = new EnumMap<>(Attribute.class);
    }

    public int getRerollsRemaining() {
        return rerollsRemaining;
    }

    public void setRerollsRemaining(int rerollsRemaining) {
        this.rerollsRemaining = rerollsRemaining;
    }

    /**
     * Records which real fighter an attribute value was taken from.
     * @param attribute the attribute that was assigned
     * @param sourceFighterName the real fighter the value came from
     */
    public void setAttributeSource(Attribute attribute, String sourceFighterName) {
        attributeSources.put(attribute, sourceFighterName);
    }

    /**
     * Returns the real fighter a given attribute value came from.
     * @param attribute the attribute to look up
     * @return the source fighter's name, or null if the attribute is unassigned
     */
    public String getAttributeSource(Attribute attribute) {
        return attributeSources.get(attribute);
    }

    /**
     * Returns a read-only view of every attribute's source fighter.
     * @return an unmodifiable map of attribute sources
     */
    public Map<Attribute, String> getAttributeSources() {
        return Collections.unmodifiableMap(attributeSources);
    }
}
