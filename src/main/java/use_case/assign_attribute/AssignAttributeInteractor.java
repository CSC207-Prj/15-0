package use_case.assign_attribute;

import entity.Attribute;
import entity.CustomFighter;
import entity.RealFighter;
import use_case.spin_fighter.SpinFighterInputBoundary;
import use_case.spin_fighter.SpinFighterInputData;

/**
 * Interactor for the Assign Attribute use case.
 */
public class AssignAttributeInteractor
        implements AssignAttributeInputBoundary {

    private final AssignAttributeOutputBoundary presenter;
    private final SpinFighterInputBoundary spinFighterInteractor;

    public AssignAttributeInteractor(
            AssignAttributeOutputBoundary presenter,
            SpinFighterInputBoundary spinFighterInteractor) {
        this.presenter = presenter;
        this.spinFighterInteractor = spinFighterInteractor;
    }

    @Override
    public void execute(AssignAttributeInputData inputData) {
        final RealFighter realFighter =
                inputData.getRealFighter();
        final CustomFighter customFighter =
                inputData.getCustomFighter();
        final Attribute attribute =
                inputData.getAttribute();

        if (realFighter == null) {
            presenter.prepareFailView(
                    "Spin a fighter before assigning an attribute."
            );
            return;
        }

        if (attribute == null) {
            presenter.prepareFailView(
                    "Select an attribute first."
            );
            return;
        }

        if (customFighter.hasAttribute(attribute)) {
            presenter.prepareFailView(
                    "That attribute is already assigned."
            );
            return;
        }

        if (customFighter.hasUsedSourceFighter(
                realFighter.getName())) {
            presenter.prepareFailView(
                    "That fighter has already contributed an attribute."
            );
            return;
        }

        final double value =
                realFighter.getAttribute(attribute);

        customFighter.assignAttributeFrom(
                attribute,
                value,
                realFighter.getName()
        );

        presenter.prepareSuccessView(
                new AssignAttributeOutputData(
                        attribute,
                        value,
                        realFighter.getName()
                )
        );

        if (!customFighter.hasAllAttributes()) {
            spinFighterInteractor.execute(
                    new SpinFighterInputData(
                            inputData.getEra(),
                            customFighter
                    )
            );
        }
    }
}
