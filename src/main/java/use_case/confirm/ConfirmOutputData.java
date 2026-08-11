package use_case.confirm;

import java.util.ArrayList;
import java.util.List;

public class ConfirmOutputData {
    private String fighterName;
    private List<String> attributePoints;
    private String weightClass;
    private int overall;

    public ConfirmOutputData(String fighterName, List<String> attributePoints, String weightClass,  int overall) {
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
    public String getFighterName() {
        return fighterName;
    }
    public List<String> getAttributePoints() {
        return new ArrayList<>(attributePoints);
    }
    public String getWeightClass() {
        return weightClass;
    }
    public int getOverall() {
        return overall;
    }

}
