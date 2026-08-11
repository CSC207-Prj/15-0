package use_case.confirm;

/**
 * Defines how confirm use-case results are presented.
 */
public interface ConfirmOutputBoundary {

    /**
     * Presents the result of a successful weight-class spin.
     *
     * @param outputData the fighter data produced by the spin
     */
    void prepareSpinSuccessView(ConfirmOutputData outputData);

    /**
     * Presents the result of a successful fighter confirmation.
     *
     * @param outputData the confirmed fighter data
     */
    void prepareConfirmSuccessView(ConfirmOutputData outputData);

    /**
     * Presents an error from the confirm use case.
     *
     * @param message the error message to display
     */
    void prepareFailureView(String message);
}
