package use_case.simulation;

import entity.GameRun;

/**
 * Defines the persistence operations required by the gauntlet simulation use case.
 *
 * The interface is owned by the use-case layer so that SimulationInteractor can
 * work with an active GameRun without depending on a concrete storage
 * mechanism. An outer-layer data-access object supplies the implementation.
 */
public interface SimulationDataAccessInterface {
    /**
     * Returns the currently active gauntlet run.
     *
     * @return the active run, or null when no run has been created yet
     */
    GameRun getGameRun();

    /**
     * Stores the current state of a gauntlet run.
     *
     * @param gameRun the run to store
     */
    void saveGameRun(GameRun gameRun);
}
