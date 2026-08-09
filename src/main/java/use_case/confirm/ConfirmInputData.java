package use_case.confirm;

import java.util.ArrayList;
import java.util.List;

public class ConfirmInputData {
    private String fighterName;
    private List<String> attributePoints;
    private String weightClass;

    public ConfirmInputData(String fighterName, List<String> attributePoints, String weightClass) {
        this.fighterName = fighterName;
        if (attributePoints == null) {
            this.attributePoints = new ArrayList<>();
        }
        this.attributePoints = new ArrayList<>(attributePoints);
        this.weightClass = weightClass;
    }
    public String getFighterName() {
        return fighterName;
    }
    public List<String> getAttributePoints() {
        return new ArrayList<>(attributePoints);
    }
    public String getWeightClass() {
        return weightClass;
    }
}
