package data_access;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entity.Attribute;
import entity.RealFighter;
import entity.UfcEra;
import entity.WeightClass;
import use_case.fighter_creation.FighterDataAccessInterface;

/**
 * In-memory fighter data access used for development and testing.
 */
public class InMemoryFighterDataAccessObject
        implements FighterDataAccessInterface {

    private static final double ROYCE_GRACIE_STRIKING = 72;
    private static final double ROYCE_GRACIE_DEFENSE = 86;
    private static final double ROYCE_GRACIE_TAKEDOWN = 97;
    private static final double ROYCE_GRACIE_HEIGHT = 77;
    private static final double ROYCE_GRACIE_REACH = 82;
    private static final double ROYCE_GRACIE_CARDIO = 88;

    private static final double KEN_SHAMROCK_STRIKING = 79;
    private static final double KEN_SHAMROCK_DEFENSE = 80;
    private static final double KEN_SHAMROCK_TAKEDOWN = 94;
    private static final double KEN_SHAMROCK_HEIGHT = 88;
    private static final double KEN_SHAMROCK_REACH = 90;
    private static final double KEN_SHAMROCK_CARDIO = 84;

    private static final double GEORGES_ST_PIERRE_STRIKING = 94;
    private static final double GEORGES_ST_PIERRE_DEFENSE = 97;
    private static final double GEORGES_ST_PIERRE_TAKEDOWN = 99;
    private static final double GEORGES_ST_PIERRE_HEIGHT = 89;
    private static final double GEORGES_ST_PIERRE_REACH = 94;
    private static final double GEORGES_ST_PIERRE_CARDIO = 98;

    private static final double ANDERSON_SILVA_STRIKING = 99;
    private static final double ANDERSON_SILVA_DEFENSE = 92;
    private static final double ANDERSON_SILVA_TAKEDOWN = 78;
    private static final double ANDERSON_SILVA_HEIGHT = 90;
    private static final double ANDERSON_SILVA_REACH = 95;
    private static final double ANDERSON_SILVA_CARDIO = 92;

    private static final double ISLAM_MAKHACHEV_STRIKING = 92;
    private static final double ISLAM_MAKHACHEV_DEFENSE = 95;
    private static final double ISLAM_MAKHACHEV_TAKEDOWN = 99;
    private static final double ISLAM_MAKHACHEV_HEIGHT = 84;
    private static final double ISLAM_MAKHACHEV_REACH = 89;
    private static final double ISLAM_MAKHACHEV_CARDIO = 96;

    private static final double MAX_HOLLOWAY_STRIKING = 98;
    private static final double MAX_HOLLOWAY_DEFENSE = 93;
    private static final double MAX_HOLLOWAY_TAKEDOWN = 81;
    private static final double MAX_HOLLOWAY_HEIGHT = 83;
    private static final double MAX_HOLLOWAY_REACH = 90;
    private static final double MAX_HOLLOWAY_CARDIO = 100;

    private final List<RealFighter> fighters = new ArrayList<>();

    /**
     * Creates an in-memory fighter data source containing sample fighters.
     */
    public InMemoryFighterDataAccessObject() {
        fighters.add(new RealFighter(
                "Royce Gracie",
                WeightClass.WELTERWEIGHT,
                0,
                UfcEra.EARLY_UFC,
                "15-2-3",
                stats(
                        ROYCE_GRACIE_STRIKING,
                        ROYCE_GRACIE_DEFENSE,
                        ROYCE_GRACIE_TAKEDOWN,
                        ROYCE_GRACIE_HEIGHT,
                        ROYCE_GRACIE_REACH,
                        ROYCE_GRACIE_CARDIO)));

        fighters.add(new RealFighter(
                "Ken Shamrock",
                WeightClass.LIGHT_HEAVYWEIGHT,
                0,
                UfcEra.EARLY_UFC,
                "28-17-2",
                stats(
                        KEN_SHAMROCK_STRIKING,
                        KEN_SHAMROCK_DEFENSE,
                        KEN_SHAMROCK_TAKEDOWN,
                        KEN_SHAMROCK_HEIGHT,
                        KEN_SHAMROCK_REACH,
                        KEN_SHAMROCK_CARDIO)));

        fighters.add(new RealFighter(
                "Georges St-Pierre",
                WeightClass.WELTERWEIGHT,
                0,
                UfcEra.ZUFFA_ERA,
                "26-2",
                stats(
                        GEORGES_ST_PIERRE_STRIKING,
                        GEORGES_ST_PIERRE_DEFENSE,
                        GEORGES_ST_PIERRE_TAKEDOWN,
                        GEORGES_ST_PIERRE_HEIGHT,
                        GEORGES_ST_PIERRE_REACH,
                        GEORGES_ST_PIERRE_CARDIO)));

        fighters.add(new RealFighter(
                "Anderson Silva",
                WeightClass.MIDDLEWEIGHT,
                0,
                UfcEra.ZUFFA_ERA,
                "34-11",
                stats(
                        ANDERSON_SILVA_STRIKING,
                        ANDERSON_SILVA_DEFENSE,
                        ANDERSON_SILVA_TAKEDOWN,
                        ANDERSON_SILVA_HEIGHT,
                        ANDERSON_SILVA_REACH,
                        ANDERSON_SILVA_CARDIO)));

        fighters.add(new RealFighter(
                "Islam Makhachev",
                WeightClass.LIGHTWEIGHT,
                0,
                UfcEra.MODERN,
                "27-1",
                stats(
                        ISLAM_MAKHACHEV_STRIKING,
                        ISLAM_MAKHACHEV_DEFENSE,
                        ISLAM_MAKHACHEV_TAKEDOWN,
                        ISLAM_MAKHACHEV_HEIGHT,
                        ISLAM_MAKHACHEV_REACH,
                        ISLAM_MAKHACHEV_CARDIO)));

        fighters.add(new RealFighter(
                "Max Holloway",
                WeightClass.FEATHERWEIGHT,
                0,
                UfcEra.MODERN,
                "27-8",
                stats(
                        MAX_HOLLOWAY_STRIKING,
                        MAX_HOLLOWAY_DEFENSE,
                        MAX_HOLLOWAY_TAKEDOWN,
                        MAX_HOLLOWAY_HEIGHT,
                        MAX_HOLLOWAY_REACH,
                        MAX_HOLLOWAY_CARDIO)));
    }

    /**
     * Creates an attribute map from the six gameplay ratings.
     *
     * @param striking striking rating
     * @param defense defense rating
     * @param takedown takedown rating
     * @param height height rating
     * @param reach reach rating
     * @param cardio cardio rating
     * @return map containing all fighter gameplay attributes
     */
    private static Map<Attribute, Double> stats(
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

    /**
     * Returns the fighters stored by this in-memory data source.
     *
     * @return copy of the available fighter list
     */
    @Override
    public List<RealFighter> getFighters() {
        return new ArrayList<>(fighters);
    }
}
