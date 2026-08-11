package use_case.assign_attribute;

import entity.Attribute;
import entity.CustomFighter;
import entity.RealFighter;
import use_case.spin_fighter.SpinFighterInputBoundary;
import use_case.spin_fighter.SpinFighterInputData;

/**
 * Interactor for the Assign Attribute use case.
 */
public class AssignAttributeInteractor implements AssignAttributeInputBoundary {

    private final AssignAttributeOutputBoundary presenter;
    private final SpinFighterInputBoundary spinFighterInteractor;

    public AssignAttributeInteractor(AssignAttributeOutputBoundary presenter,
                                     SpinFighterInputBoundary spinFighterInteractor) {
        this.presenter = presenter;
        this.spinFighterInteractor = spinFighterInteractor;
    }

    @Override
    public void execute(AssignAttributeInputData inputData) {
        final RealFighter realFighter = inputData.getRealFighter();
        final CustomFighter customFighter = inputData.getCustomFighter();
        final Attribute attribute = inputData.getAttribute();

        if (customFighter.hasAttribute(attribute)) {
            presenter.prepareFailView("That attribute is already assigned.");
        }
        else {
            final double value = realFighter.getAttribute(attribute);

            customFighter.assignAttribute(attribute, value);

            final AssignAttributeOutputData outputData =
                    new AssignAttributeOutputData(attribute, value, realFighter.getName());

            presenter.prepareSuccessView(outputData);

            if (!customFighter.hasAllAttributes()) {
                spinFighterInteractor.execute(new SpinFighterInputData(inputData.getEra()));
            }
        }
    }
}
