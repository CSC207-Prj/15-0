package use_case.assign_attribute;

/**
 * Input boundary for the Assign Attribute use case.
 */
public interface AssignAttributeInputBoundary {

    /**
     * Executes an attribute-assignment request.
     *
     * @param inputData selected fighter, custom fighter, attribute, and era
     */
    void execute(AssignAttributeInputData inputData);
}
