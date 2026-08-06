package entity;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Abstract base class for all fighters, as defined in the project blueprint.
 * Holds the name, weight class, record, and the six core attribute values
 * shared by real and custom fighters.
 */
public abstract class Fighter {
    private final String name;
    private String weightClass;
    private FighterRecord record;
    private final Map<Attribute, Double> attributeValues;

    protected Fighter(String name, String weightClass) {
        this.name = name;
        this.weightClass = weightClass;
        this.record = FighterRecord.empty();
        this.attributeValues = new EnumMap<>(Attribute.class);
    }

    public String getName() {
        return name;
    }

    public String getWeightClass() {
        return weightClass;
    }

    public void setWeightClass(String weightClass) {
        this.weightClass = weightClass;
    }

    public FighterRecord getRecord() {
        return record;
    }

    public void setRecord(FighterRecord record) {
        this.record = record;
    }

    /**
     * Returns the value of the given attribute.
     * @param attribute the attribute to look up
     * @return the value, or null if this attribute has not been assigned yet
     */
    public Double getAttribute(Attribute attribute) {
        return attributeValues.get(attribute);
    }

    /**
     * Sets the value of one attribute. Higher-level assignment logic
     * (for example the wheel spin) lives in the use case layer; this is
     * just the raw state change on the entity.
     * @param attribute the attribute to set
     * @param value the value to store
     */
    public void setAttribute(Attribute attribute, double value) {
        attributeValues.put(attribute, value);
    }

    /**
     * Reports whether every one of the six attributes has been assigned a value.
     * @return true if no attribute is missing
     */
    public boolean hasAllAttributes() {
        return attributeValues.size() == Attribute.values().length;
    }

    /**
     * Returns a read-only view of all assigned attribute values.
     * @return an unmodifiable map of attribute values
     */
    public Map<Attribute, Double> getAttributeValues() {
        return Collections.unmodifiableMap(attributeValues);
    }
}
