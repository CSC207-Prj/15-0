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

    public ConfirmState() {
        resetAttributePoints();
    }

    public String getFighterName() {
        return fighterName;
    }

    public void setFighterName(String fighterName) {
        this.fighterName = fighterName == null ? "" : fighterName;
    }

    public List<String> getAttributePoints() {
        return new ArrayList<>(attributePoints);
    }

    public void setAttributePoints(List<String> newAttributePoints) {
        attributePoints.clear();
        if (newAttributePoints == null) {
            resetAttributePoints();
        }else {
            attributePoints.addAll(newAttributePoints);
        }
    }
    public String getWeightClass() {
        return weightClass;
    }

    public void setWeightClass(String weightClass) {
        if (weightClass == null){
            this.weightClass = DEFAULT_WEIGHT_CLASS;
        }
        this.weightClass = weightClass;

    }

    public String getOverall() {
        return overall;
    }

    public void setOverall(String overall) {
        if (overall == null){
            this.overall = DEFAULT_OVERALL;
        }
        this.overall = overall;
    }
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isWeightClassLocked() {
        return weightClassLocked;
    }

    public void setWeightClassLocked(boolean weightClassLocked) {
        this.weightClassLocked = weightClassLocked;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    private void resetAttributePoints() {
        for (int index = 0; index < ATTRIBUTE_COUNT; index++) {
            attributePoints.add(DEFAULT_ATTRIBUTE_VALUE);
        }
    }
}