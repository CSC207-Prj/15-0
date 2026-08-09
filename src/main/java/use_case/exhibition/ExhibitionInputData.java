package use_case.exhibition;

/**
 * Input data for the Exhibition Match use case: the names of the two saved
 * fighters the player picked.
 */
public class ExhibitionInputData {
    private final String fighterAName;
    private final String fighterBName;

    public ExhibitionInputData(String fighterAName, String fighterBName) {
        this.fighterAName = fighterAName;
        this.fighterBName = fighterBName;
    }

    public String getFighterAName() {
        return fighterAName;
    }

    public String getFighterBName() {
        return fighterBName;
    }
}
