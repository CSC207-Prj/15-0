package use_case.confirm;

public interface ConfirmInputBoundary {
    void spin(ConfirmInputData inputData);
    void confirm(ConfirmInputData inputData);
}
