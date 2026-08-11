package use_case.reroll_fighter;

import entity.RealFighter;
import entity.UfcEra;

/**
 * Input data for the Reroll Fighter use case.
 */
public class RerollFighterInputData {

    private final UfcEra era;
    private final int rerollsLeft;
    private final RealFighter currentFighter;

    public RerollFighterInputData(UfcEra era, int rerollsLeft, RealFighter currentFighter) {
        this.era = era;
        this.rerollsLeft = rerollsLeft;
        this.currentFighter = currentFighter;
    }

    public UfcEra getEra() {
        return era;
    }

    public int getRerollsLeft() {
        return rerollsLeft;
    }

    public RealFighter getCurrentFighter() {
        return currentFighter;
    }
}
