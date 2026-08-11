package use_case.confirm;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the fighter information submitted to the confirm use case.
 */
public class ConfirmInputData {
    private String fighterName;
    private List<String> attributePoints;
    private String weightClass;

    /**
     * Creates input data for a confirm-fighter action.
     *
     * @param fighterName the fighter's name
     * @param attributePoints the fighter's attribute values
     * @param weightClass the fighter's current weight class
     */
    public ConfirmInputData(String fighterName, List<String> attributePoints, String weightClass) {
        this.fighterName = fighterName;
        if (attributePoints == null) {
            this.attributePoints = new ArrayList<>();
        }else{
        this.attributePoints = new ArrayList<>(attributePoints);}
        this.weightClass = weightClass;
    }

    /**
     * Gets the fighter's name.
     *
     * @return the fighter's name
     */
    public String getFighterName() {
        return fighterName;
    }

    /**
     * Gets a copy of the fighter's attribute values.
     *
     * @return a copy of the attribute values
     */
    public List<String> getAttributePoints() {
        return new ArrayList<>(attributePoints);
    }

    /**
     * Gets the fighter's weight class.
     *
     * @return the weight-class display name
     */
    public String getWeightClass() {
        return weightClass;
    }
}
