package use_case.confirm;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains fighter information produced by the confirm use case.
 */
public class ConfirmOutputData {
    private String fighterName;
    private List<String> attributePoints;
    private String weightClass;
    private int overall;

    /**
     * Creates output data for a successful confirm-fighter action.
     *
     * @param fighterName the fighter's name
     * @param attributePoints the fighter's attribute values
     * @param weightClass the fighter's selected weight class
     * @param overall the fighter's calculated overall rating
     */
    public ConfirmOutputData(String fighterName, List<String> attributePoints, String weightClass, int overall) {
        this.fighterName = fighterName;
        if (attributePoints == null) {
            this.attributePoints = new ArrayList<>();
        }
        else {
            this.attributePoints = new ArrayList<>(attributePoints);
        }
        this.weightClass = weightClass;
        this.overall = overall;
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

    /**
     * Gets the fighter's calculated overall rating.
     *
     * @return the overall rating
     */
    public int getOverall() {
        return overall;
    }

}
