package data_access;

import entity.Attribute;
import entity.RealFighter;
import entity.UfcEra;
import entity.WeightClass;
import use_case.fighter_creation.FighterDataAccessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * In-memory fighter data access used for development and testing.
 */
public class InMemoryFighterDataAccessObject
        implements FighterDataAccessInterface {

    private final List<RealFighter> fighters = new ArrayList<>();

    public InMemoryFighterDataAccessObject() {
        fighters.add(new RealFighter(
                "Royce Gracie", WeightClass.WELTERWEIGHT, 0,
                UfcEra.EARLY_UFC, "15-2-3",
                stats(72, 86, 97, 77, 82, 88)));

        fighters.add(new RealFighter(
                "Ken Shamrock", WeightClass.LIGHT_HEAVYWEIGHT, 0,
                UfcEra.EARLY_UFC, "28-17-2",
                stats(79, 80, 94, 88, 90, 84)));

        fighters.add(new RealFighter(
                "Georges St-Pierre", WeightClass.WELTERWEIGHT, 0,
                UfcEra.ZUFFA_ERA, "26-2",
                stats(94, 97, 99, 89, 94, 98)));

        fighters.add(new RealFighter(
                "Anderson Silva", WeightClass.MIDDLEWEIGHT, 0,
                UfcEra.ZUFFA_ERA, "34-11",
                stats(99, 92, 78, 90, 95, 92)));

        fighters.add(new RealFighter(
                "Islam Makhachev", WeightClass.LIGHTWEIGHT, 0,
                UfcEra.MODERN, "27-1",
                stats(92, 95, 99, 84, 89, 96)));

        fighters.add(new RealFighter(
                "Max Holloway", WeightClass.FEATHERWEIGHT, 0,
                UfcEra.MODERN, "27-8",
                stats(98, 93, 81, 83, 90, 100)));
    }

    private Map<Attribute, Double> stats(
            double striking,
            double defense,
            double takedown,
            double height,
            double reach,
            double cardio) {

        return Map.of(
                Attribute.STRIKING, striking,
                Attribute.DEFENSE, defense,
                Attribute.TAKEDOWN, takedown,
                Attribute.HEIGHT, height,
                Attribute.REACH, reach,
                Attribute.CARDIO, cardio
        );
    }

    @Override
    public List<RealFighter> getFighters() {
        return new ArrayList<>(fighters);
    }
}