package use_case.spin_fighter;

/**
 * Output boundary for the Spin Fighter use case.
 */
public interface SpinFighterOutputBoundary {

    /**
     * Prepares the successfully rolled fighter for presentation.
     *
     * @param outputData the result of the Spin Fighter use case
     */
    void prepareSuccessView(SpinFighterOutputData outputData);
}