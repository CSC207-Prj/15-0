package interface_adapter.confirm_fighter;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the information displayed by the Confirm Fighter screen.
 */
public class ConfirmState {
    private static final int ATTRIBUTE_COUNT = 6;
    private static final String DEFAULT_ATTRIBUTE_VALUE = "TBD";
    private static final String DEFAULT_WEIGHT_CLASS = "TBD";
    private static final String DEFAULT_OVERALL = "--";

    private String fighterName = "";
    private final List<String> attributePoints = new ArrayList<>();
    private String weightClass = DEFAULT_WEIGHT_CLASS;
    private String overall = DEFAULT_OVERALL;
    private String errorMessage;
    private boolean weightClassLocked;
    private boolean confirmed;

    /**
     * Creates the default state for the confirm-fighter screen.
     */
    public ConfirmState() {
        resetAttributePoints();
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
     * Sets the fighter's name.
     *
     * @param fighterName the new fighter name
     */
    public void setFighterName(String fighterName) {
        this.fighterName = fighterName == null ? "" : fighterName;
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
     * Replaces the fighter's attribute values.
     *
     * @param newAttributePoints the new attribute values
     */
    public void setAttributePoints(List<String> newAttributePoints) {
        attributePoints.clear();
        if (newAttributePoints == null) {
            resetAttributePoints();
        }
        else {
            attributePoints.addAll(newAttributePoints);
        }
    }

    /**
     * Gets the selected weight class.
     *
     * @return the weight-class display name
     */
    public String getWeightClass() {
        return weightClass;
    }

    /**
     * Sets the selected weight class.
     *
     * @param weightClass the weight-class display name
     */
    public void setWeightClass(String weightClass) {
        if (weightClass == null) {
            this.weightClass = DEFAULT_WEIGHT_CLASS;
        }
        this.weightClass = weightClass;

    }

    /**
     * Gets the fighter's overall rating.
     *
     * @return the display-ready overall rating
     */
    public String getOverall() {
        return overall;
    }

    /**
     * Sets the fighter's overall rating.
     *
     * @param overall the display-ready overall rating
     */
    public void setOverall(String overall) {
        if (overall == null) {
            this.overall = DEFAULT_OVERALL;
        }
        this.overall = overall;
    }

    /**
     * Gets the current error message.
     *
     * @return the current error message, or null when there is no error
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the current error message.
     *
     * @param errorMessage the error message to display
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Reports whether the weight class is locked.
     *
     * @return true when the weight class is locked; otherwise, false
     */
    public boolean isWeightClassLocked() {
        return weightClassLocked;
    }

    /**
     * Sets whether the weight class is locked.
     *
     * @param weightClassLocked whether the weight class is locked
     */
    public void setWeightClassLocked(boolean weightClassLocked) {
        this.weightClassLocked = weightClassLocked;
    }

    /**
     * Reports whether the fighter has been confirmed.
     *
     * @return true when the fighter is confirmed; otherwise, false
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Sets whether the fighter has been confirmed.
     *
     * @param confirmed whether the fighter is confirmed
     */
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    /**
     * Restores all attribute slots to their default value.
     */
    private void resetAttributePoints() {
        for (int index = 0; index < ATTRIBUTE_COUNT; index++) {
            attributePoints.add(DEFAULT_ATTRIBUTE_VALUE);
        }
    }
}
