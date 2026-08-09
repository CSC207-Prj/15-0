package use_case.load_fighter;

import java.util.Collections;
import java.util.Map;

/**
 * Output data for the Load Fighter use case: the saved fighter's full details
 * as plain values, ready for the presenter to display.
 */
public class LoadFighterOutputData {
    private final String fighterName;
    private final String weightClassName;
    private final String recordText;
    private final int finishes;
    private final Map<String, Double> attributeValues;
    private final boolean useCaseFailed;

    public LoadFighterOutputData(String fighterName, String weightClassName, String recordText,
                                 int finishes, Map<String, Double> attributeValues,
                                 boolean useCaseFailed) {
        this.fighterName = fighterName;
        this.weightClassName = weightClassName;
        this.recordText = recordText;
        this.finishes = finishes;
        this.attributeValues = attributeValues;
        this.useCaseFailed = useCaseFailed;
    }

    public String getFighterName() {
        return fighterName;
    }

    public String getWeightClassName() {
        return weightClassName;
    }

    public String getRecordText() {
        return recordText;
    }

    public int getFinishes() {
        return finishes;
    }

    /**
     * Returns the fighter's attribute values keyed by attribute display name.
     * @return an unmodifiable map of attribute values
     */
    public Map<String, Double> getAttributeValues() {
        return Collections.unmodifiableMap(attributeValues);
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
