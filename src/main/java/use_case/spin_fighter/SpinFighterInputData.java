package use_case.spin_fighter;

import entity.UfcEra;

/**
 * Input data for the Spin Fighter use case.
 */
public class SpinFighterInputData {

    private final UfcEra era;

    public SpinFighterInputData(UfcEra era) {
        this.era = era;
    }

    public UfcEra getEra() {
        return era;
    }
}