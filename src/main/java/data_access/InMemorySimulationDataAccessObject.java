package data_access;

import entity.GameRun;
import use_case.simulation.SimulationDataAccessInterface;

/**
 * Stores the active game run in memory for the Simulation use case.
 */
public final class InMemorySimulationDataAccessObject
        implements SimulationDataAccessInterface {

    private GameRun gameRun;

    public InMemorySimulationDataAccessObject() {
    }

    public InMemorySimulationDataAccessObject(GameRun gameRun) {
        this.gameRun = gameRun;
    }

    @Override
    public GameRun getGameRun() {
        return gameRun;
    }

    @Override
    public void saveGameRun(GameRun gameRun) {
        this.gameRun = gameRun;
    }
}
