package data_access;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.Attribute;
import entity.CustomFighter;
import entity.FighterRecord;
import entity.WeightClass;
import use_case.delete_fighter.DeleteFighterDataAccessInterface;
import use_case.exhibition.ExhibitionDataAccessInterface;
import use_case.load_fighter.LoadFighterDataAccessInterface;
import use_case.save_fighter.SaveFighterDataAccessInterface;
import use_case.view_roster.ViewRosterDataAccessInterface;

/**
 * File-backed implementation of the saved-fighter roster, as described in the
 * blueprint: all fighters live in one local JSON file, which is read once on
 * startup and rewritten after every save or delete. Each use case accesses
 * the store through its own small data-access interface.
 */
public class JsonFighterRosterDataAccess
        implements SaveFighterDataAccessInterface, ViewRosterDataAccessInterface,
        DeleteFighterDataAccessInterface, LoadFighterDataAccessInterface,
        ExhibitionDataAccessInterface {

    private static final String NAME = "name";
    private static final String WEIGHT_CLASS = "weightClass";
    private static final String RECORD_KEY = "record";
    private static final String WINS = "wins";
    private static final String LOSSES = "losses";
    private static final String FINISHES = "finishes";
    private static final String ATTRIBUTES = "attributes";
    private static final int JSON_INDENT = 2;

    private final Path filePath;
    private final Map<String, CustomFighter> fighters = new LinkedHashMap<>();

    /**
     * Creates the store and loads any fighters already saved in the file.
     * @param filePath path of the roster file, for example "saved_fighters.json"
     * @throws RuntimeException if the file exists but cannot be read or parsed
     */
    public JsonFighterRosterDataAccess(String filePath) {
        this.filePath = Paths.get(filePath);
        load();
    }

    @Override
    public boolean existsByName(String fighterName) {
        return fighters.containsKey(key(fighterName));
    }

    @Override
    public void save(CustomFighter fighter) {
        fighters.put(key(fighter.getName()), fighter);
        persist();
    }

    @Override
    public List<CustomFighter> getAllFighters() {
        return new ArrayList<>(fighters.values());
    }

    @Override
    public void deleteByName(String fighterName) {
        fighters.remove(key(fighterName));
        persist();
    }

    @Override
    public CustomFighter getByName(String fighterName) {
        return fighters.get(key(fighterName));
    }

    private void load() {
        if (Files.exists(filePath)) {
            try {
                final String content =
                        new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
                if (!content.trim().isEmpty()) {
                    final JSONArray array = new JSONArray(content);
                    for (int i = 0; i < array.length(); i++) {
                        final CustomFighter fighter = fromJson(array.getJSONObject(i));
                        fighters.put(key(fighter.getName()), fighter);
                    }
                }
            }
            catch (IOException ex) {
                throw new RuntimeException(
                        "Could not read the saved roster file: " + filePath, ex);
            }
        }
    }

    private void persist() {
        final JSONArray array = new JSONArray();
        for (CustomFighter fighter : fighters.values()) {
            array.put(toJson(fighter));
        }
        try {
            Files.write(filePath, array.toString(JSON_INDENT).getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException ex) {
            throw new RuntimeException("Could not write the saved roster file: " + filePath, ex);
        }
    }

    private static JSONObject toJson(CustomFighter fighter) {
        final JSONObject json = new JSONObject();
        json.put(NAME, fighter.getName());
        if (fighter.getWeightClass() != null) {
            json.put(WEIGHT_CLASS, fighter.getWeightClass().name());
        }

        final JSONObject recordJson = new JSONObject();
        recordJson.put(WINS, fighter.getRecord().getWins());
        recordJson.put(LOSSES, fighter.getRecord().getLosses());
        recordJson.put(FINISHES, fighter.getRecord().getFinishes());
        json.put(RECORD_KEY, recordJson);

        final JSONObject attributes = new JSONObject();
        for (Map.Entry<Attribute, Double> entry : fighter.getAttributes().entrySet()) {
            attributes.put(entry.getKey().name(), entry.getValue());
        }
        json.put(ATTRIBUTES, attributes);

        return json;
    }

    private static CustomFighter fromJson(JSONObject json) {
        final String weightClassName = json.optString(WEIGHT_CLASS, null);
        final WeightClass weightClass;
        if (weightClassName == null) {
            weightClass = null;
        }
        else {
            weightClass = WeightClass.valueOf(weightClassName);
        }

        final JSONObject recordJson = json.optJSONObject(RECORD_KEY);
        final FighterRecord fighterRecord;
        if (recordJson == null) {
            fighterRecord = new FighterRecord();
        }
        else {
            fighterRecord = new FighterRecord(
                    recordJson.optInt(WINS, 0),
                    recordJson.optInt(LOSSES, 0),
                    recordJson.optInt(FINISHES, 0));
        }

        final Map<Attribute, Double> attributes = new EnumMap<>(Attribute.class);
        final JSONObject attributeJson = json.optJSONObject(ATTRIBUTES);
        if (attributeJson != null) {
            for (String attributeName : attributeJson.keySet()) {
                attributes.put(Attribute.valueOf(attributeName),
                        attributeJson.getDouble(attributeName));
            }
        }

        return new CustomFighter(json.getString(NAME), weightClass, fighterRecord, attributes);
    }

    private static String key(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }
}
