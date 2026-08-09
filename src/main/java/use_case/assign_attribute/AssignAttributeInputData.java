package use_case.assign_attribute;

import entity.Attribute;
import entity.CustomFighter;
import entity.RealFighter;

/**
 * Input data for the Assign Attribute use case.
 */
public class AssignAttributeInputData {

    private final CustomFighter customFighter;
    private final RealFighter realFighter;
    private final Attribute attribute;

    public AssignAttributeInputData(CustomFighter customFighter, RealFighter realFighter, Attribute attribute) {
        this.customFighter = customFighter;
        this.realFighter = realFighter;
        this.attribute = attribute;
    }

    public CustomFighter getCustomFighter() {
        return customFighter;
    }

    public RealFighter getRealFighter() {
        return realFighter;
    }

    public Attribute getAttribute() {
        return attribute;
    }
}