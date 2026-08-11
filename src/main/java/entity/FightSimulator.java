package entity;

/**
 * Defines the Strategy-pattern contract for resolving one fight.
 *
 * Higher-level use cases depend on this abstraction rather than on a concrete
 * algorithm, allowing the simulation policy to be replaced or faked without
 * changing SimulationInteractor or ExhibitionInteractor.
 */
public interface FightSimulator {
    /**
     * Simulates one matchup and returns its domain result.
     *
     * @param player custom fighter controlled by the user
     * @param opponent real fighter being faced
     * @param maxRounds maximum rounds permitted for the matchup
     * @param difficulty selected game difficulty
     * @return immutable result of the simulated fight
     */
    FightResult simulate(CustomFighter player,
                         RealFighter opponent,
                         int maxRounds,
                         Difficulty difficulty);
}
