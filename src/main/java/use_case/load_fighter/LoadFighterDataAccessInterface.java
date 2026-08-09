package use_case.load_fighter;

import entity.CustomFighter;

/**
 * Data access interface the Load Fighter use case needs from the roster store.
 */
public interface LoadFighterDataAccessInterface {

    /**
     * Looks up a saved fighter by name. Ignores case.
     * @param fighterName the name to look up
     * @return the saved fighter, or null if no fighter has this name
     */
    CustomFighter getByName(String fighterName);
}
