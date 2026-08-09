package interface_adapter.fighter_creation;

import entity.Attribute;
import entity.CustomFighter;
import entity.RealFighter;
import use_case.assign_attribute.AssignAttributeInputBoundary;
import use_case.assign_attribute.AssignAttributeInputData;

/**
 * Controller for the Assign Attribute use case.
 */
public class AssignAttributeController {

    private final AssignAttributeInputBoundary interactor;

    public AssignAttributeController(AssignAttributeInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(CustomFighter customFighter, RealFighter realFighter, Attribute attribute) {
        final AssignAttributeInputData inputData = new AssignAttributeInputData(customFighter, realFighter, attribute);

        interactor.execute(inputData);
    }
}