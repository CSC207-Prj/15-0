package use_case.load_fighter;

/**
 * Input data for the Load Fighter use case: the name of the saved fighter
 * the player wants to bring back up.
 */
public class LoadFighterInputData {
    private final String fighterName;

    public LoadFighterInputData(String fighterName) {
        this.fighterName = fighterName;
    }

    public String getFighterName() {
        return fighterName;
    }
}
