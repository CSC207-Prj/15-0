package use_case.simulation;

import entity.GameRun;

/**
 * Persistence boundary owned by the Simulation use case.
 *
 * An outer-layer DAO implements this interface. The interactor therefore does
 * not know whether the run lives in memory, a file, or another data source.
 */
public interface SimulationDataAccessInterface {
    GameRun getGameRun();

    void saveGameRun(GameRun gameRun);
}
