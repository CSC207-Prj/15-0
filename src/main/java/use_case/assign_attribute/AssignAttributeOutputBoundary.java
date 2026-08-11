package use_case.assign_attribute;

/**
 * Output boundary for the Assign Attribute use case.
 */
public interface AssignAttributeOutputBoundary {

    /**
     * Prepares a successful attribute assignment.
     *
     * @param outputData the assignment result
     */
    void prepareSuccessView(AssignAttributeOutputData outputData);

    /**
     * Prepares a failed attribute assignment.
     *
     * @param errorMessage the reason the assignment failed
     */
    void prepareFailView(String errorMessage);
}
