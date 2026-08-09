package use_case.delete_fighter;

/**
 * Data access interface the Delete Fighter use case needs from the roster store.
 */
public interface DeleteFighterDataAccessInterface {

    /**
     * Reports whether a fighter with this name is saved. Ignores case.
     * @param fighterName the name to check
     * @return true if a saved fighter has this name
     */
    boolean existsByName(String fighterName);

    /**
     * Removes the fighter with this name from the persistent roster.
     * @param fighterName the name of the fighter to remove
     */
    void deleteByName(String fighterName);
}
