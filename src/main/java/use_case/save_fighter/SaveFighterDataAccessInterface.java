package use_case.save_fighter;

import entity.CustomFighter;

/**
 * Data access interface the Save Fighter use case needs from the roster store.
 */
public interface SaveFighterDataAccessInterface {

    /**
     * Reports whether a fighter with this name is already saved.
     * Name comparison ignores case.
     * @param fighterName the name to check
     * @return true if a saved fighter already has this name
     */
    boolean existsByName(String fighterName);

    /**
     * Saves the fighter to the persistent roster.
     * @param fighter the fighter to save
     */
    void save(CustomFighter fighter);
}
