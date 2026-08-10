package use_case.reroll_fighter;

import entity.CustomFighter;
import entity.RealFighter;
import entity.UfcEra;

/**
 * Input data for the Reroll Fighter use case.
 */
public class RerollFighterInputData {

    private final UfcEra era;
    private final int rerollsLeft;
    private final RealFighter currentFighter;
    private final CustomFighter customFighter;

    public RerollFighterInputData(
            UfcEra era,
            int rerollsLeft,
            RealFighter currentFighter) {
        this(era, rerollsLeft, currentFighter, null);
    }

    public RerollFighterInputData(
            UfcEra era,
            int rerollsLeft,
            RealFighter currentFighter,
            CustomFighter customFighter) {
        this.era = era;
        this.rerollsLeft = rerollsLeft;
        this.currentFighter = currentFighter;
        this.customFighter = customFighter;
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

    public CustomFighter getCustomFighter() {
        return customFighter;
    }
}
