package use_case.confirm;

/**
 * Defines the actions available to the confirm-fighter screen.
 */
public interface ConfirmInputBoundary {

    /**
     * Assigns a random weight class and calculates the fighter's overall rating.
     *
     * @param inputData the fighter data used for the spin
     */
    void spin(ConfirmInputData inputData);

    /**
     * Confirms the completed fighter.
     *
     * @param inputData the fighter data to confirm
     */
    void confirm(ConfirmInputData inputData);
}
