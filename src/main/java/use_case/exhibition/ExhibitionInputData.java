package use_case.exhibition;

/**
 * Input data for the Exhibition Match use case: the names of the two saved
 * fighters the player picked.
 */
public class ExhibitionInputData {
    private final String firstFighterName;
    private final String secondFighterName;

    public ExhibitionInputData(String firstFighterName, String secondFighterName) {
        this.firstFighterName = firstFighterName;
        this.secondFighterName = secondFighterName;
    }

    public String getFirstFighterName() {
        return firstFighterName;
    }

    public String getSecondFighterName() {
        return secondFighterName;
    }
}
