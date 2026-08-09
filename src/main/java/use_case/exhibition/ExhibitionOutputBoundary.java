package use_case.exhibition;

/**
 * Output boundary for presenting the result of an exhibition match.
 */
public interface ExhibitionOutputBoundary {

    /**
     * Prepares the view of the finished exhibition match.
     * @param outputData the match result
     */
    void prepareSuccessView(ExhibitionOutputData outputData);

    /**
     * Prepares the failure view when the match could not be run.
     * @param errorMessage explanation of what went wrong
     */
    void prepareFailView(String errorMessage);
}
