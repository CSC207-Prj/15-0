package use_case.assign_attribute;

import entity.Attribute;

/**
 * Output data for the Assign Attribute use case.
 */
public class AssignAttributeOutputData {

    private final Attribute attribute;
    private final double value;
    private final String fighterName;

    public AssignAttributeOutputData(Attribute attribute, double value, String fighterName) {
        this.attribute = attribute;
        this.value = value;
        this.fighterName = fighterName;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public double getValue() {
        return value;
    }

    public String getFighterName() {
        return fighterName;
    }
}