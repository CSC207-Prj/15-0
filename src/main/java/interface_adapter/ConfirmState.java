package interface_adapter;

import java.util.ArrayList;
import java.util.List;

public class ConfirmState {
    private String fighterName;
    private List<String> attributePoints;
    private String WeightClass = "TBD";
    private String Overall = "";

    public ConfirmState() {
        attributePoints = new ArrayList<>();
        for  (int i = 0; i < 6; i++) {
            attributePoints.add("TBD");
        }
        
    }
    public String getFighterName() {
        return fighterName;
    }
    public void setFighterName(String fighterName) {
        this.fighterName = fighterName;
    }
    public List<String> getAttributePoints() {
        return new ArrayList<>(attributePoints);
    }
    public void setAttributePoints(List<String> attributePoints) {
        this.attributePoints = new ArrayList<>(attributePoints);
    }
    public String getWeightClass() {
        return WeightClass;
    }
    public void setWeightClass(String weightClass) {
        this.WeightClass = weightClass;
    }
    public String getOverall() {
        return Overall;
    }
    public void setOverall(String overall) {
        this.Overall = overall;
    }

}
