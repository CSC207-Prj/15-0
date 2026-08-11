package use_case.reroll_fighter;

import entity.RealFighter;

/**
 * Output data for the Reroll Fighter use case.
 */
public class RerollFighterOutputData {

    private final RealFighter fighter;
    private final int rerollsLeft;

    public RerollFighterOutputData(RealFighter fighter, int rerollsLeft) {
        this.fighter = fighter;
        this.rerollsLeft = rerollsLeft;
    }

    public RealFighter getFighter() {
        return fighter;
    }

    public int getRerollsLeft() {
        return rerollsLeft;
    }
}
