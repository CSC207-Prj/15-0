package use_case.spin_fighter;

import entity.RealFighter;

/**
 * Output data for the Spin Fighter use case.
 */
public class SpinFighterOutputData {

    private final RealFighter fighter;

    public SpinFighterOutputData(RealFighter fighter) {
        this.fighter = fighter;
    }

    public RealFighter getFighter() {
        return fighter;
    }
}


