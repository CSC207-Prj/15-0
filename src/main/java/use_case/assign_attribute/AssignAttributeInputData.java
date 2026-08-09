package use_case.assign_attribute;

import entity.Attribute;
import entity.CustomFighter;
import entity.RealFighter;
import entity.UfcEra;

/**
 * Input data for the Assign Attribute use case.
 */
public class AssignAttributeInputData {

    private final CustomFighter customFighter;
    private final RealFighter realFighter;
    private final Attribute attribute;
    private final UfcEra era;


    public AssignAttributeInputData(CustomFighter customFighter, RealFighter realFighter, Attribute attribute, UfcEra era) {
        this.customFighter = customFighter;
        this.realFighter = realFighter;
        this.attribute = attribute;
        this.era = era;
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

    public UfcEra getEra() {
        return era;
    }
}