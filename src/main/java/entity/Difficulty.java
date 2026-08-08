package entity;

/**
 * Difficulty choices for one gauntlet run.
 *
 * This is an entity-level value because other use cases (rerolls/fight simulation)
 * may also need to know the selected difficulty later.
 */
public enum Difficulty {
    EASY,
    NORMAL,
    HARD
}
