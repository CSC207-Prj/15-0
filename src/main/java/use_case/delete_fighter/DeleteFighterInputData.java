package use_case.delete_fighter;

/**
 * Input data for the Delete Fighter use case: the name of the saved fighter
 * the player wants to remove.
 */
public class DeleteFighterInputData {
    private final String fighterName;

    public DeleteFighterInputData(String fighterName) {
        this.fighterName = fighterName;
    }

    public String getFighterName() {
        return fighterName;
    }
}
