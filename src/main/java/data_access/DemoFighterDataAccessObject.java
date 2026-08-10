package data_access;

import entity.Attribute;
import entity.RealFighter;
import entity.UfcEra;
import entity.WeightClass;
import use_case.fighter_creation.FighterDataAccessInterface;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Temporary fighter catalogue used until the Cito API adapter is connected.
 *
 * There are at least six fighters in each selectable era so a complete
 * six-attribute draft can always use six different source fighters.
 */
public class DemoFighterDataAccessObject implements FighterDataAccessInterface {
    private final List<RealFighter> fighters = new ArrayList<>();

    public DemoFighterDataAccessObject() {
        // Early UFC
        add("Royce Gracie", WeightClass.WELTERWEIGHT, UfcEra.EARLY_UFC,
                "15-2-3", 72, 86, 97, 77, 82, 88);
        add("Ken Shamrock", WeightClass.LIGHT_HEAVYWEIGHT, UfcEra.EARLY_UFC,
                "28-17-2", 79, 80, 94, 88, 90, 84);
        add("Dan Severn", WeightClass.HEAVYWEIGHT, UfcEra.EARLY_UFC,
                "101-19-7", 74, 84, 98, 91, 90, 91);
        add("Bas Rutten", WeightClass.HEAVYWEIGHT, UfcEra.EARLY_UFC,
                "28-4-1", 94, 90, 84, 89, 91, 92);
        add("Mark Coleman", WeightClass.HEAVYWEIGHT, UfcEra.EARLY_UFC,
                "16-10", 82, 83, 97, 92, 91, 84);
        add("Frank Shamrock", WeightClass.LIGHT_HEAVYWEIGHT, UfcEra.EARLY_UFC,
                "23-10-2", 88, 90, 92, 85, 87, 94);
        add("Tito Ortiz", WeightClass.LIGHT_HEAVYWEIGHT, UfcEra.EARLY_UFC,
                "21-12-1", 84, 87, 95, 91, 94, 91);
        add("Pat Miletich", WeightClass.WELTERWEIGHT, UfcEra.EARLY_UFC,
                "29-7-2", 88, 89, 90, 82, 85, 92);

        // Zuffa era
        add("Georges St-Pierre", WeightClass.WELTERWEIGHT, UfcEra.ZUFFA_ERA,
                "26-2", 94, 97, 99, 89, 94, 98);
        add("Anderson Silva", WeightClass.MIDDLEWEIGHT, UfcEra.ZUFFA_ERA,
                "34-11", 99, 92, 78, 90, 95, 92);
        add("Jose Aldo", WeightClass.FEATHERWEIGHT, UfcEra.ZUFFA_ERA,
                "32-8", 97, 95, 92, 82, 87, 96);
        add("BJ Penn", WeightClass.LIGHTWEIGHT, UfcEra.ZUFFA_ERA,
                "16-14-2", 92, 90, 91, 80, 84, 91);
        add("Chuck Liddell", WeightClass.LIGHT_HEAVYWEIGHT, UfcEra.ZUFFA_ERA,
                "21-9", 96, 87, 83, 91, 94, 88);
        add("Randy Couture", WeightClass.HEAVYWEIGHT, UfcEra.ZUFFA_ERA,
                "19-11", 88, 92, 97, 91, 92, 95);
        add("Dominick Cruz", WeightClass.BANTAMWEIGHT, UfcEra.ZUFFA_ERA,
                "24-4", 91, 98, 88, 77, 82, 99);
        add("Cain Velasquez", WeightClass.HEAVYWEIGHT, UfcEra.ZUFFA_ERA,
                "14-3", 94, 92, 96, 94, 94, 99);

        // Modern era
        add("Islam Makhachev", WeightClass.LIGHTWEIGHT, UfcEra.MODERN,
                "27-1", 92, 95, 99, 84, 89, 96);
        add("Max Holloway", WeightClass.FEATHERWEIGHT, UfcEra.MODERN,
                "27-8", 98, 93, 81, 83, 90, 100);
        add("Alexander Volkanovski", WeightClass.FEATHERWEIGHT, UfcEra.MODERN,
                "26-4", 97, 96, 91, 79, 86, 99);
        add("Alex Pereira", WeightClass.LIGHT_HEAVYWEIGHT, UfcEra.MODERN,
                "12-3", 100, 90, 76, 95, 98, 91);
        add("Ilia Topuria", WeightClass.FEATHERWEIGHT, UfcEra.MODERN,
                "16-0", 98, 95, 90, 80, 85, 94);
        add("Tom Aspinall", WeightClass.HEAVYWEIGHT, UfcEra.MODERN,
                "15-3", 97, 94, 91, 97, 97, 95);
        add("Leon Edwards", WeightClass.WELTERWEIGHT, UfcEra.MODERN,
                "22-4", 95, 96, 90, 89, 95, 96);
        add("Sean O'Malley", WeightClass.BANTAMWEIGHT, UfcEra.MODERN,
                "18-2", 97, 91, 78, 85, 94, 94);
    }

    private void add(String name,
                     WeightClass weightClass,
                     UfcEra era,
                     String record,
                     double striking,
                     double defense,
                     double takedown,
                     double height,
                     double reach,
                     double cardio) {

        final Map<Attribute, Double> attributes =
                new EnumMap<>(Attribute.class);
        attributes.put(Attribute.STRIKING, striking);
        attributes.put(Attribute.DEFENSE, defense);
        attributes.put(Attribute.TAKEDOWN, takedown);
        attributes.put(Attribute.HEIGHT, height);
        attributes.put(Attribute.REACH, reach);
        attributes.put(Attribute.CARDIO, cardio);

        fighters.add(new RealFighter(
                name,
                weightClass,
                0,
                era,
                record,
                attributes
        ));
    }

    @Override
    public List<RealFighter> getFighters() {
        return new ArrayList<>(fighters);
    }
}
