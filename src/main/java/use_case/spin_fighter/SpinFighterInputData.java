package use_case.spin_fighter;

import entity.CustomFighter;
import entity.UfcEra;

/**
 * Input data for the Spin Fighter use case.
 */
public class SpinFighterInputData {

    private final UfcEra era;
    private final CustomFighter customFighter;

    public SpinFighterInputData(UfcEra era) {
        this(era, null);
    }

    public SpinFighterInputData(UfcEra era,
                                CustomFighter customFighter) {
        this.era = era;
        this.customFighter = customFighter;
    }

    public UfcEra getEra() {
        return era;
    }

    public CustomFighter getCustomFighter() {
        return customFighter;
    }
}
