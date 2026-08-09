package use_case.exhibition;

/**
 * Input boundary for the Exhibition Match use case.
 */
public interface ExhibitionInputBoundary {

    /**
     * Executes the Exhibition Match use case.
     * @param inputData the two saved fighters to match up
     */
    void execute(ExhibitionInputData inputData);
}
