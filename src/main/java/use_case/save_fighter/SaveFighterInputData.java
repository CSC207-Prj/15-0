package use_case.save_fighter;

import entity.CustomFighter;

/**
 * Input data for the Save Fighter use case: the finished custom fighter
 * the player wants to keep in their roster.
 */
public class SaveFighterInputData {
    private final CustomFighter fighter;

    public SaveFighterInputData(CustomFighter fighter) {
        this.fighter = fighter;
    }

    public CustomFighter getFighter() {
        return fighter;
    }
}
