package use_case.save_fighter;

/**
 * Output data for the Save Fighter use case.
 */
public class SaveFighterOutputData {
    private final String fighterName;
    private final boolean useCaseFailed;

    public SaveFighterOutputData(String fighterName, boolean useCaseFailed) {
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
