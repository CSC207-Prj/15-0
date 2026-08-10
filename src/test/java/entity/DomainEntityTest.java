package entity;

import data_access.DemoRankingsFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DomainEntityTest {

    @Test
    void fighterRecordValidatesAndTracksResults() {
        final FighterRecord record = new FighterRecord();
        assertEquals(0, record.getTotalFights());
        record.registerWin(true);
        record.registerWin(false);
        record.registerLoss();
        assertEquals(2, record.getWins());
        assertEquals(1, record.getLosses());
        assertEquals(1, record.getFinishes());
        assertEquals(3, record.getTotalFights());
        assertEquals("2-1", record.toString());

        final FighterRecord copy = record.copy();
        assertNotSame(record, copy);
        assertEquals(record.toString(), copy.toString());

        assertThrows(IllegalArgumentException.class,
                () -> new FighterRecord(-1, 0, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new FighterRecord(0, -1, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new FighterRecord(0, 0, -1));
        assertThrows(IllegalArgumentException.class,
                () -> new FighterRecord(1, 0, 2));
    }

    @Test
    void fighterValidatesNamesWeightAndAttributes() {
        assertThrows(IllegalArgumentException.class,
                () -> new CustomFighter(null));
        assertThrows(IllegalArgumentException.class,
                () -> new CustomFighter("   "));
        assertThrows(NullPointerException.class,
                () -> new CustomFighter(
                        "A", null, null, Map.of()));

        final CustomFighter fighter = new CustomFighter("  Alpha  ");
        assertEquals("Alpha", fighter.getName());
        assertNull(fighter.getWeightClass());
        fighter.setName("  Bravo ");
        fighter.setWeightClass(WeightClass.WELTERWEIGHT);
        assertEquals("Bravo", fighter.getName());
        assertEquals(WeightClass.WELTERWEIGHT, fighter.getWeightClass());
        assertThrows(NullPointerException.class,
                () -> fighter.setWeightClass(null));
        assertThrows(IllegalStateException.class,
                () -> fighter.getAttribute(Attribute.CARDIO));
        assertThrows(NullPointerException.class,
                () -> fighter.assignAttribute(null, 1));
        assertThrows(IllegalArgumentException.class,
                () -> fighter.assignAttribute(Attribute.CARDIO, -0.1));
        assertThrows(IllegalArgumentException.class,
                () -> fighter.assignAttribute(Attribute.CARDIO, 100.1));
        assertThrows(IllegalArgumentException.class,
                () -> fighter.assignAttribute(Attribute.CARDIO, Double.NaN));

        for (Attribute attribute : Attribute.values()) {
            fighter.assignAttribute(attribute, 50.0);
        }
        assertTrue(fighter.hasAttribute(Attribute.CARDIO));
        assertTrue(fighter.hasAllAttributes());
        assertThrows(UnsupportedOperationException.class,
                () -> fighter.getAttributes().clear());
    }

    @Test
    void realFighterAndFightResultEnforceTheirRanges() {
        final RealFighter opponent = realFighter("Opponent", 1);
        assertEquals(1, opponent.getRank());
        assertEquals(UfcEra.MODERN, opponent.getEra());
        assertEquals("10-0", opponent.getProfessionalRecord());
        assertThrows(IllegalArgumentException.class,
                () -> realFighter("Bad", -1));
        assertThrows(IllegalArgumentException.class,
                () -> realFighter("Bad", 16));
        assertThrows(NullPointerException.class,
                () -> new RealFighter(
                        "Bad", WeightClass.LIGHTWEIGHT, 1,
                        null, "0-0", attributes(50)));
        assertThrows(NullPointerException.class,
                () -> new RealFighter(
                        "Bad", WeightClass.LIGHTWEIGHT, 1,
                        UfcEra.MODERN, null, attributes(50)));

        final FightResult result = new FightResult(
                opponent, true, FightMethod.SUBMISSION, 2, 70);
        assertEquals(opponent, result.getOpponent());
        assertTrue(result.isPlayerWon());
        assertEquals(FightMethod.SUBMISSION, result.getMethod());
        assertEquals(2, result.getRound());
        assertEquals(70, result.getSecondsInRound());
        assertThrows(NullPointerException.class,
                () -> new FightResult(
                        null, true, FightMethod.DECISION, 3, 300));
        assertThrows(NullPointerException.class,
                () -> new FightResult(opponent, true, null, 3, 300));
        assertThrows(IllegalArgumentException.class,
                () -> new FightResult(
                        opponent, true, FightMethod.DECISION, 0, 300));
        assertThrows(IllegalArgumentException.class,
                () -> new FightResult(
                        opponent, true, FightMethod.DECISION, 6, 300));
        assertThrows(IllegalArgumentException.class,
                () -> new FightResult(
                        opponent, true, FightMethod.DECISION, 3, -1));
        assertThrows(IllegalArgumentException.class,
                () -> new FightResult(
                        opponent, true, FightMethod.DECISION, 3, 301));
    }

    @Test
    void settingsEnumsAndDivisionExposeDomainValuesSafely() {
        assertTrue(new GameSettings(
                Difficulty.NORMAL, 1, UfcEra.MODERN, true).isValid());
        assertTrue(new GameSettings(
                Difficulty.NORMAL, 3, UfcEra.MODERN, false).isValid());
        final GameSettings fiveRounds = new GameSettings(
                Difficulty.HARD, 5, UfcEra.ALL_TIME, true);
        assertTrue(fiveRounds.isValid());
        assertEquals(Difficulty.HARD, fiveRounds.getDifficulty());
        assertEquals(5, fiveRounds.getRoundsPerFight());
        assertEquals(UfcEra.ALL_TIME, fiveRounds.getEra());
        assertTrue(fiveRounds.isHideOpponentStats());
        assertFalse(new GameSettings(
                null, 3, UfcEra.MODERN, false).isValid());
        assertFalse(new GameSettings(
                Difficulty.NORMAL, null, UfcEra.MODERN, false).isValid());
        assertFalse(new GameSettings(
                Difficulty.NORMAL, 3, null, false).isValid());
        assertFalse(new GameSettings(
                Difficulty.NORMAL, 2, UfcEra.MODERN, false).isValid());

        assertEquals(3, Difficulty.EASY.getRerollLimit());
        assertEquals("Striking", Attribute.STRIKING.toString());
        assertEquals("Modern Era (2016-present)", UfcEra.MODERN.toString());
        assertEquals("Heavyweight", WeightClass.HEAVYWEIGHT.toString());
        assertEquals("KO/TKO", FightMethod.KO_TKO.toString());
        assertTrue(FightMethod.KO_TKO.isFinish());
        assertTrue(FightMethod.SUBMISSION.isFinish());
        assertFalse(FightMethod.DECISION.isFinish());

        final ArrayList<RealFighter> ranked =
                new ArrayList<>(List.of(realFighter("A", 1)));
        final Division division =
                new Division(WeightClass.LIGHTWEIGHT, ranked);
        ranked.clear();
        assertEquals(1, division.getRankedFighters().size());
        assertEquals(WeightClass.LIGHTWEIGHT, division.getWeightClass());
        assertThrows(UnsupportedOperationException.class,
                () -> division.getRankedFighters().clear());
        assertThrows(NullPointerException.class,
                () -> new Division(null, List.of()));
        assertThrows(NullPointerException.class,
                () -> new Division(WeightClass.LIGHTWEIGHT, null));
    }

    @Test
    void gameRunRejectsInvalidSetupAndResultTransitions() {
        final CustomFighter player = customFighter(WeightClass.LIGHTWEIGHT);
        final Division division =
                DemoRankingsFactory.createDivision(WeightClass.LIGHTWEIGHT);
        assertThrows(NullPointerException.class,
                () -> new GameRun(null, division,
                        Difficulty.NORMAL, 3, false));
        assertThrows(NullPointerException.class,
                () -> new GameRun(player, null,
                        Difficulty.NORMAL, 3, false));
        assertThrows(NullPointerException.class,
                () -> new GameRun(player, division, null, 3, false));
        assertThrows(IllegalArgumentException.class,
                () -> new GameRun(player, division,
                        Difficulty.NORMAL, 0, false));
        assertThrows(IllegalArgumentException.class,
                () -> new GameRun(player, division,
                        Difficulty.NORMAL, 6, false));
        assertThrows(IllegalArgumentException.class,
                () -> new GameRun(
                        customFighter(WeightClass.WELTERWEIGHT), division,
                        Difficulty.NORMAL, 3, false));

        final GameRun run = new GameRun(
                player, division, Difficulty.NORMAL, 3, false);
        assertThrows(NullPointerException.class, () -> run.recordResult(null));
        assertThrows(IllegalArgumentException.class,
                () -> run.recordResult(new FightResult(
                        realFighter("Wrong", 15), true,
                        FightMethod.DECISION, 3, 300)));
        while (!run.isComplete()) {
            run.recordResult(new FightResult(
                    run.getCurrentOpponent(), true,
                    FightMethod.DECISION, 3, 300));
        }
        assertThrows(IllegalStateException.class,
                () -> run.recordResult(new FightResult(
                        realFighter("Extra", 1), true,
                        FightMethod.DECISION, 3, 300)));

        final List<RealFighter> tooFew = new ArrayList<>(
                division.getRankedFighters().subList(0, 14));
        assertThrows(IllegalArgumentException.class,
                () -> new GameRun(player,
                        new Division(WeightClass.LIGHTWEIGHT, tooFew),
                        Difficulty.NORMAL, 3, false));

        final List<RealFighter> duplicateRanks = new ArrayList<>(
                division.getRankedFighters());
        duplicateRanks.set(0, realFighter("Duplicate", 2));
        assertThrows(IllegalArgumentException.class,
                () -> new GameRun(customFighter(WeightClass.LIGHTWEIGHT),
                        new Division(WeightClass.LIGHTWEIGHT, duplicateRanks),
                        Difficulty.NORMAL, 3, false));
    }

    private static CustomFighter customFighter(WeightClass weightClass) {
        return new CustomFighter(
                "Player", weightClass, new FighterRecord(), attributes(80));
    }

    private static RealFighter realFighter(String name, int rank) {
        return new RealFighter(
                name, WeightClass.LIGHTWEIGHT, rank,
                UfcEra.MODERN, "10-0", attributes(70));
    }

    private static Map<Attribute, Double> attributes(double value) {
        final Map<Attribute, Double> attributes =
                new EnumMap<>(Attribute.class);
        for (Attribute attribute : Attribute.values()) {
            attributes.put(attribute, value);
        }
        return attributes;
    }
}
