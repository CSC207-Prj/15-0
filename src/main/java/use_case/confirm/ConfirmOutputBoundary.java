package use_case.confirm;

public interface ConfirmOutputBoundary {
    void prepareSpinSuccessView(ConfirmOutputData outputData);
    void prepareConfirmSuccessView(ConfirmOutputData outputData);
    void prepareFailureView(String message);
}
