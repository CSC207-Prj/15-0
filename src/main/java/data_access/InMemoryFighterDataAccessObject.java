package data_access;

import entity.RealFighter;
import use_case.fighter_creation.FighterDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory fighter data access used for development and testing.
 */
public class InMemoryFighterDataAccessObject
        implements FighterDataAccessInterface {

    private final List<RealFighter> fighters = new ArrayList<>();

    @Override
    public List<RealFighter> getFighters() {
        return new ArrayList<>(fighters);
    }
}