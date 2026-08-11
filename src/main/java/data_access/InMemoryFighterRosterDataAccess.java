package data_access;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import entity.CustomFighter;
import use_case.delete_fighter.DeleteFighterDataAccessInterface;
import use_case.exhibition.ExhibitionDataAccessInterface;
import use_case.load_fighter.LoadFighterDataAccessInterface;
import use_case.save_fighter.SaveFighterDataAccessInterface;
import use_case.view_roster.ViewRosterDataAccessInterface;

/**
 * Stores saved fighters in memory for the lifetime of the application process.
 */
public class InMemoryFighterRosterDataAccess
        implements SaveFighterDataAccessInterface, ViewRosterDataAccessInterface,
        DeleteFighterDataAccessInterface, LoadFighterDataAccessInterface,
        ExhibitionDataAccessInterface {

    private final Map<String, CustomFighter> fighters = new LinkedHashMap<>();

    @Override
    public boolean existsByName(String fighterName) {
        return fighters.containsKey(key(fighterName));
    }

    @Override
    public void save(CustomFighter fighter) {
        fighters.put(key(fighter.getName()), fighter);
    }

    @Override
    public List<CustomFighter> getAllFighters() {
        return new ArrayList<>(fighters.values());
    }

    @Override
    public void deleteByName(String fighterName) {
        fighters.remove(key(fighterName));
    }

    @Override
    public CustomFighter getByName(String fighterName) {
        return fighters.get(key(fighterName));
    }

    private static String key(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }
}
