package interface_adapter.assign_attribute;

import entity.Attribute;
import entity.CustomFighter;
import entity.RealFighter;
import entity.UfcEra;
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

    /**
     * Sends the selected attribute and fighters to the assignment interactor.
     *
     * @param customFighter fighter being built
     * @param realFighter fighter supplying the selected value
     * @param attribute attribute being assigned
     * @param era era used for the next automatic spin
     */
    public void execute(CustomFighter customFighter,
                        RealFighter realFighter,
                        Attribute attribute,
                        UfcEra era) {
        final AssignAttributeInputData inputData =
                new AssignAttributeInputData(customFighter, realFighter, attribute, era);

        interactor.execute(inputData);
    }
}
