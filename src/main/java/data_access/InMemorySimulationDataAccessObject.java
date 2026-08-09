package data_access;

import entity.GameRun;
import use_case.simulation.SimulationDataAccessInterface;

/**
 * Temporary in-memory DAO for User Story 4.
 *
 * Later integration can replace this with the team's shared session DAO
 * without changing SimulationInteractor.
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
