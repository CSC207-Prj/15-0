package use_case.view_roster;

import java.util.List;

import entity.CustomFighter;

/**
 * Data access interface the View Roster use case needs from the roster store.
 */
public interface ViewRosterDataAccessInterface {

    /**
     * Returns every saved fighter, in no particular order.
     * @return all saved fighters
     */
    List<CustomFighter> getAllFighters();
}
