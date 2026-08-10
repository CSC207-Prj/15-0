package use_case.spin_fighter;

/**
 * Output boundary for the Spin Fighter use case.
 */
public interface SpinFighterOutputBoundary {

    void prepareSuccessView(SpinFighterOutputData outputData);

    /**
     * Optional failure hook retained as a default method so older test
     * presenters that only implement success still compile.
     */
    default void prepareFailView(String errorMessage) {
        // No-op by default.
    }
}
