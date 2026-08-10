package use_case.fighter_creation;

/**
 * Output boundary for loading Fighter Creation.
 */
public interface LoadFighterCreationOutputBoundary {
    void prepareSuccessView(LoadFighterCreationOutputData outputData);

    void prepareFailView(String errorMessage);
}
