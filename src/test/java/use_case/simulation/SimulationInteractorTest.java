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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void missingRunIsPresentedAsFailure() {
        final CapturingPresenter presenter = new CapturingPresenter();
        final SimulationInteractor interactor = new SimulationInteractor(
                new InMemorySimulationDataAccessObject(),
                alwaysWinByDecision(), presenter);

        interactor.execute(new SimulationInputData(
                SimulationInputData.Action.LOAD));

        assertNull(presenter.output);
        assertEquals(
                "No active gauntlet exists. Finalize a fighter before starting the simulation.",
                presenter.error);
    }

    @Test
    void loadPresentsReadyRunWithoutSimulating() {
        final GameRun run = run();
        final CapturingPresenter presenter = new CapturingPresenter();
        final SimulationInteractor interactor = new SimulationInteractor(
                new InMemorySimulationDataAccessObject(run),
                (player, opponent, maxRounds, difficulty) -> {
                    throw new AssertionError("Load must not simulate");
                }, presenter);

        interactor.execute(new SimulationInputData(
                SimulationInputData.Action.LOAD));

        assertEquals("Ready for the next fight.", presenter.output.getMessage());
        assertEquals(SimulationOutputData.OpponentStatus.NEXT,
                presenter.output.getOpponents().get(0).status());
        assertEquals(SimulationOutputData.OpponentStatus.PENDING,
                presenter.output.getOpponents().get(1).status());
        assertEquals(0, presenter.output.getFightHistory().size());
    }

    @Test
    void lossUpdatesRecordAndOpponentStatus() {
        final GameRun run = run();
        final CapturingPresenter presenter = new CapturingPresenter();
        final FightSimulator alwaysLose = (player, opponent, maxRounds, difficulty) ->
                new FightResult(opponent, false, FightMethod.KO_TKO, 1, 15);

        new SimulationInteractor(
                new InMemorySimulationDataAccessObject(run),
                alwaysLose, presenter).execute(new SimulationInputData(
                SimulationInputData.Action.SIMULATE_NEXT));

        assertEquals(0, run.getPlayer().getRecord().getWins());
        assertEquals(1, run.getPlayer().getRecord().getLosses());
        assertEquals(SimulationOutputData.OpponentStatus.LOSS,
                presenter.output.getOpponents().get(0).status());
        assertEquals(SimulationOutputData.OpponentStatus.NEXT,
                presenter.output.getOpponents().get(1).status());
    }

    @Test
    void completedRunDoesNotSimulateAgain() {
        final GameRun run = run();
        while (!run.isComplete()) {
            run.recordResult(alwaysWinByDecision().simulate(
                    run.getPlayer(), run.getCurrentOpponent(),
                    run.getRoundsPerFight(), run.getDifficulty()));
        }
        final CapturingPresenter presenter = new CapturingPresenter();
        final FightSimulator forbidden = (player, opponent, maxRounds, difficulty) -> {
            throw new AssertionError("Completed run must not simulate");
        };
        final SimulationInteractor interactor = new SimulationInteractor(
                new InMemorySimulationDataAccessObject(run), forbidden, presenter);

        interactor.execute(new SimulationInputData(
                SimulationInputData.Action.SIMULATE_NEXT));
        assertEquals("Gauntlet complete.", presenter.output.getMessage());
        assertNull(presenter.output.getCurrentOpponent());

        interactor.execute(new SimulationInputData(
                SimulationInputData.Action.AUTO_SIMULATE));
        assertEquals("Gauntlet complete.", presenter.output.getMessage());

        interactor.execute(new SimulationInputData(
                SimulationInputData.Action.LOAD));
        assertEquals("Gauntlet complete.", presenter.output.getMessage());
    }

    @Test
    void invalidSimulationResultIsPresentedAsFailure() {
        final GameRun run = run();
        final CapturingPresenter presenter = new CapturingPresenter();
        final RealFighter wrongOpponent =
                DemoRankingsFactory.createDivision(WeightClass.WELTERWEIGHT)
                        .getRankedFighters().get(0);

        new SimulationInteractor(
                new InMemorySimulationDataAccessObject(run),
                (player, opponent, maxRounds, difficulty) ->
                        new FightResult(wrongOpponent, true,
                                FightMethod.DECISION, 3, 300),
                presenter).execute(new SimulationInputData(
                SimulationInputData.Action.SIMULATE_NEXT));

        assertEquals("Result does not match the current opponent.",
                presenter.error);
    }

    @Test
    void constructorAndExecuteRejectNullDependenciesAndInput() {
        final CapturingPresenter presenter = new CapturingPresenter();
        final InMemorySimulationDataAccessObject dataAccess =
                new InMemorySimulationDataAccessObject(run());

        assertThrows(NullPointerException.class,
                () -> new SimulationInteractor(
                        null, alwaysWinByDecision(), presenter));
        assertThrows(NullPointerException.class,
                () -> new SimulationInteractor(dataAccess, null, presenter));
        assertThrows(NullPointerException.class,
                () -> new SimulationInteractor(
                        dataAccess, alwaysWinByDecision(), null));
        assertThrows(NullPointerException.class,
                () -> new SimulationInteractor(
                        dataAccess, alwaysWinByDecision(), presenter)
                        .execute(null));
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
        private String error;

        @Override
        public void prepareSuccessView(SimulationOutputData outputData) {
            output = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }
    }
}
