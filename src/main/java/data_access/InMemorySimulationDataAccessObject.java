package data_access;

import entity.GameRun;
import use_case.simulation.SimulationDataAccessInterface;

/**
 * Stores the currently active gauntlet run in process memory.
 *
 * This outer-layer data-access object implements the use-case-owned
 * SimulationDataAccessInterface. Replacing it with file or database
 * storage would not require changes to SimulationInteractor.
 */
public final class InMemorySimulationDataAccessObject
        implements SimulationDataAccessInterface {

    private GameRun gameRun;

    /**
     * Creates an empty in-memory session store.
     */
    public InMemorySimulationDataAccessObject() {
    }

    /**
     * Creates an in-memory session store initialized with a run.
     *
     * @param gameRun initial active run
     */
    public InMemorySimulationDataAccessObject(GameRun gameRun) {
        this.gameRun = gameRun;
    }

    /**
     * Returns the run currently held in memory.
     *
     * @return active run, or null when none has been stored
     */
    @Override
    public GameRun getGameRun() {
        return gameRun;
    }

    /**
     * Replaces the run currently held in memory.
     *
     * @param gameRun run to store
     */
    @Override
    public void saveGameRun(GameRun gameRun) {
        this.gameRun = gameRun;
    }
}
