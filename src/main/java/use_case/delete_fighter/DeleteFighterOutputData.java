package use_case.delete_fighter;

/**
 * Output data for the Delete Fighter use case.
 */
public class DeleteFighterOutputData {
    private final String fighterName;
    private final boolean useCaseFailed;

    public DeleteFighterOutputData(String fighterName, boolean useCaseFailed) {
        this.fighterName = fighterName;
        this.useCaseFailed = useCaseFailed;
    }

    public String getFighterName() {
        return fighterName;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
