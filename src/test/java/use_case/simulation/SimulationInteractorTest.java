package use_case.simulation;

import data_access.DemoRankingsFactory;
import data_access.InMemorySimulationDataAccessObject;
import entity.Attribute;
import entity.CustomFighter;
import entity.Difficulty;
import entity.Division;
import entity.FightMethod;
import entity.FightResult;
import entity.FightSimulator;
import entity.FighterRecord;
import entity.GameRun;
import entity.RealFighter;
import entity.WeightClass;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimulationInteractorTest {
    @Test
    void simulateNextRunsExactlyOneFight() {
        final GameRun run = run();
        final InMemorySimulationDataAccessObject dao =
                new InMemorySimulationDataAccessObject(run);
        final CapturingPresenter presenter = new CapturingPresenter();

        final SimulationInteractor interactor =
                new SimulationInteractor(dao, alwaysWinByDecision(), presenter);

        interactor.execute(new SimulationInputData(
                SimulationInputData.Action.SIMULATE_NEXT));

        assertEquals(1, run.getFightHistory().size());
        assertEquals(1, run.getPlayer().getRecord().getWins());
        assertEquals(0, run.getPlayer().getRecord().getLosses());
        assertEquals(1, presenter.output.getFightHistory().size());
    }

    @Test
    void autoSimulateCompletesAllFifteenFights() {
        final GameRun run = run();
        final InMemorySimulationDataAccessObject dao =
                new InMemorySimulationDataAccessObject(run);
        final CapturingPresenter presenter = new CapturingPresenter();

        final SimulationInteractor interactor =
                new SimulationInteractor(dao, alwaysWinByDecision(), presenter);

        interactor.execute(new SimulationInputData(
                SimulationInputData.Action.AUTO_SIMULATE));

        assertTrue(run.isComplete());
        assertEquals(15, run.getFightHistory().size());
        assertEquals(15, run.getPlayer().getRecord().getWins());
        assertEquals(15, presenter.output.getFightHistory().size());
    }

    private FightSimulator alwaysWinByDecision() {
        return new FightSimulator() {
            @Override
            public FightResult simulate(CustomFighter player,
                                        RealFighter opponent,
                                        int maxRounds,
                                        Difficulty difficulty) {
                return new FightResult(
                        opponent,
                        true,
                        FightMethod.DECISION,
                        maxRounds,
                        300);
            }
        };
    }

    private GameRun run() {
        final Map<Attribute, Double> attributes = new EnumMap<>(Attribute.class);
        for (Attribute attribute : Attribute.values()) {
            attributes.put(attribute, 90.0);
        }

        final CustomFighter player = new CustomFighter(
                "Test Fighter",
                WeightClass.LIGHTWEIGHT,
                new FighterRecord(),
                attributes);
        final Division division =
                DemoRankingsFactory.createDivision(WeightClass.LIGHTWEIGHT);

        return new GameRun(
                player,
                division,
                Difficulty.NORMAL,
                3,
                false);
    }

    private static final class CapturingPresenter
            implements SimulationOutputBoundary {
        private SimulationOutputData output;

        @Override
        public void prepareSuccessView(SimulationOutputData outputData) {
            output = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            throw new AssertionError(errorMessage);
        }
    }
}
