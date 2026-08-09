package entity;

import data_access.DemoRankingsFactory;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameRunTest {
    @Test
    void lossDoesNotEndRunAndAllFifteenFightsComplete() {
        final CustomFighter player = player();
        final Division division = DemoRankingsFactory.createDivision(WeightClass.LIGHTWEIGHT);
        final GameRun run = new GameRun(player, division, Difficulty.NORMAL, 3, false);

        for (int index = 0; index < 15; index++) {
            final boolean win = index % 2 == 0;
            final FightResult result = new FightResult(
                    run.getCurrentOpponent(),
                    win,
                    FightMethod.DECISION,
                    3,
                    300);
            run.recordResult(result);

            if (index < 14) {
                assertFalse(run.isComplete());
            }
        }

        assertTrue(run.isComplete());
        assertEquals(15, run.getFightHistory().size());
        assertEquals(8, player.getRecord().getWins());
        assertEquals(7, player.getRecord().getLosses());
    }

    @Test
    void gauntletStartsAtRankFifteenAndEndsAtRankOne() {
        final GameRun run = new GameRun(
                player(),
                DemoRankingsFactory.createDivision(WeightClass.LIGHTWEIGHT),
                Difficulty.NORMAL,
                3,
                false);

        assertEquals(15, run.getOpponents().get(0).getRank());
        assertEquals(1, run.getOpponents().get(14).getRank());
    }

    private CustomFighter player() {
        final Map<Attribute, Double> attributes = new EnumMap<>(Attribute.class);
        for (Attribute attribute : Attribute.values()) {
            attributes.put(attribute, 90.0);
        }
        return new CustomFighter(
                "Test Fighter",
                WeightClass.LIGHTWEIGHT,
                new FighterRecord(),
                attributes);
    }
}
