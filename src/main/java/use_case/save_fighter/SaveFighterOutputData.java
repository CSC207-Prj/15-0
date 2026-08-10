package use_case.save_fighter;

import entity.CustomFighter;

/**
 * Output data for the Save Fighter use case.
 */
public class SaveFighterOutputData {
    private final String fighterName;
    private final boolean useCaseFailed;
    private final CustomFighter fighter;

    public SaveFighterOutputData(String fighterName, boolean useCaseFailed) {
        this(fighterName, useCaseFailed, null);
    }

    public SaveFighterOutputData(String fighterName, boolean useCaseFailed, CustomFighter fighter) {
        this.fighterName = fighterName;
        this.useCaseFailed = useCaseFailed;
        this.fighter = fighter;
    }

    public String getFighterName() {
        return fighterName;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }

    /**
     * Returns the fighter this outcome is about, so a duplicate-name failure
     * can offer a rename-and-retry without losing the unsaved fighter.
     * @return the fighter, or null when the outcome carries no fighter
     */
    public CustomFighter getFighter() {
        return fighter;
    }
}
