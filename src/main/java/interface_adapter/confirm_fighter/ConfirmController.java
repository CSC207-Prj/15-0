package interface_adapter.confirm_fighter;

import java.util.List;

import use_case.confirm.ConfirmInputBoundary;
import use_case.confirm.ConfirmInputData;

/**
 * Converts confirm-screen actions into input data for the confirm use case.
 */
public class ConfirmController {
    private final ConfirmInputBoundary confirmInteractor;

    /**
     * Creates a controller for the confirm use case.
     *
     * @param confirmInteractor the input boundary that handles confirm actions
     */
    public ConfirmController(ConfirmInputBoundary confirmInteractor) {
        this.confirmInteractor = confirmInteractor;

    }

    /**
     * Requests a random weight class for the fighter.
     *
     * @param figherName the fighter's name
     * @param attributePoints the fighter's attribute values
     * @param weightClass the fighter's current weight class
     */
    public void spin(String figherName, List<String> attributePoints, String weightClass) {
        final ConfirmInputData inputData = new ConfirmInputData(figherName, attributePoints, weightClass);
        confirmInteractor.spin(inputData);
    }

    /**
     * Requests confirmation of the completed fighter.
     *
     * @param fighterName the fighter's name
     * @param attributePoints the fighter's attribute values
     * @param weightClass the fighter's selected weight class
     */
    public void confirm(String fighterName, List<String> attributePoints, String weightClass) {
        final ConfirmInputData inputData = new ConfirmInputData(fighterName, attributePoints, weightClass);
        confirmInteractor.confirm(inputData);
    }

}
