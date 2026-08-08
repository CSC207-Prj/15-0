package use_case.spin_fighter;

/**
 * Input data for spinning a fighter.
 */
public class SpinFighterInputData {

    private final String era;

    public SpinFighterInputData(String era) {
        this.era = era;
    }

    public String getEra() {
        return era;
    }
}