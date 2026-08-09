package interface_adapter;

import use_case.confirm.ConfirmInputBoundary;
import use_case.confirm.ConfirmInputData;


import java.util.List;

public class ConfirmController {
    private final ConfirmInputBoundary confirmInteractor;
    public ConfirmController(ConfirmInputBoundary confirmInteractor) {
        this.confirmInteractor = confirmInteractor;

    }
    public void spin(String figherName, List<String> attributePoints, String weightClass) {
        final ConfirmInputData inputData = new ConfirmInputData(figherName, attributePoints, weightClass);
        confirmInteractor.spin(inputData);
    }
    public void confirm(String figherName, List<String> attributePoints, String weightClass) {
        final ConfirmInputData inputData = new ConfirmInputData(figherName, attributePoints, weightClass);
        confirmInteractor.confirm(inputData);
    }

}
