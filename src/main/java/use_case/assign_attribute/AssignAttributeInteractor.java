package use_case.assign_attribute;

import entity.Attribute;
import entity.CustomFighter;
import entity.RealFighter;

/**
 * Interactor for the Assign Attribute use case.
 */
public class AssignAttributeInteractor implements AssignAttributeInputBoundary {

    private final AssignAttributeOutputBoundary presenter;

    public AssignAttributeInteractor(AssignAttributeOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(AssignAttributeInputData inputData) {
        final RealFighter realFighter = inputData.getRealFighter();
        final CustomFighter customFighter = inputData.getCustomFighter();
        final Attribute attribute = inputData.getAttribute();

        if (customFighter.hasAttribute(attribute)) {
            presenter.prepareFailView("That attribute is already assigned.");
            return;
        }

        final double value = realFighter.getAttribute(attribute);

        customFighter.assignAttribute(attribute, value);

        final AssignAttributeOutputData outputData = new AssignAttributeOutputData(attribute, value, realFighter.getName());

        presenter.prepareSuccessView(outputData);
    }
}