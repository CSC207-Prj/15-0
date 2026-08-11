package interface_adapter.assign_attribute;

import interface_adapter.fighter_creation.FighterCreationViewModel;
import use_case.assign_attribute.AssignAttributeOutputBoundary;
import use_case.assign_attribute.AssignAttributeOutputData;

/**
 * Presenter for the Assign Attribute use case.
 */
public class AssignAttributePresenter implements AssignAttributeOutputBoundary {

    private final FighterCreationViewModel viewModel;

    public AssignAttributePresenter(FighterCreationViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(AssignAttributeOutputData outputData) {
        viewModel.setErrorMessage("");

        viewModel.setAssignedAttribute(
                outputData.getAttribute().getDisplayName(),
                (int) Math.round(outputData.getValue()),
                outputData.getFighterName());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
    }
}
