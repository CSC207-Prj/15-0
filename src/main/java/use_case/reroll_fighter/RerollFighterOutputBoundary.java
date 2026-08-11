package use_case.reroll_fighter;

/**
 * Output boundary for the Reroll Fighter use case.
 */
public interface RerollFighterOutputBoundary {

    /**
     * Prepares a successful reroll result.
     *
     * @param outputData the reroll result
     */
    void prepareSuccessView(RerollFighterOutputData outputData);

    /**
     * Prepares a failed reroll result (e.g. no more rerolls left).
     *
     * @param errorMessage the reason the reroll failed
     */
    void prepareFailView(String errorMessage);
}
