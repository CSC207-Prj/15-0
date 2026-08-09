package entity;

/**
 * Strategy interface for simulating one fight.
 *
 * Higher-level use-case code depends on this interface rather than on a
 * particular simulation algorithm.
 */
public interface FightSimulator {
    FightResult simulate(CustomFighter player,
                         RealFighter opponent,
                         int maxRounds,
                         Difficulty difficulty);
}
