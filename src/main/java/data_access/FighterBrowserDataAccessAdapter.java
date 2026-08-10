package data_access;

import entity.RealFighter;
import use_case.browse_fighters.FighterBrowserDataAccessInterface;
import use_case.fighter_creation.FighterDataAccessInterface;

import java.util.List;

/**
 * Reuses the application's current real-fighter catalogue for User Story 6.
 * A Cito-backed implementation can replace this adapter later without
 * changing the browser use case.
 */
public class FighterBrowserDataAccessAdapter
        implements FighterBrowserDataAccessInterface {

    private final FighterDataAccessInterface fighterDataAccess;

    public FighterBrowserDataAccessAdapter(
            FighterDataAccessInterface fighterDataAccess) {
        this.fighterDataAccess = fighterDataAccess;
    }

    @Override
    public List<RealFighter> getFighters() {
        return fighterDataAccess.getFighters();
    }
}
