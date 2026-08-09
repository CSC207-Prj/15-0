package use_case.simulation;

import entity.FightResult;
import entity.FightSimulator;
import entity.GameRun;
import entity.RealFighter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Main application business rules for User Story 4. */
public final class SimulationInteractor implements SimulationInputBoundary {
    private final SimulationDataAccessInterface dataAccess;
    private final FightSimulator fightSimulator;
    private final SimulationOutputBoundary presenter;

    public SimulationInteractor(SimulationDataAccessInterface dataAccess,
                                FightSimulator fightSimulator,
                                SimulationOutputBoundary presenter) {
        this.dataAccess = Objects.requireNonNull(dataAccess, "dataAccess");
        this.fightSimulator = Objects.requireNonNull(fightSimulator, "fightSimulator");
        this.presenter = Objects.requireNonNull(presenter, "presenter");
    }

    @Override
    public void execute(SimulationInputData inputData) {
        Objects.requireNonNull(inputData, "inputData");

        final GameRun run = dataAccess.getGameRun();
        if (run == null) {
            presenter.prepareFailView(
                    "No active gauntlet exists. Finalize a fighter before starting the simulation.");
            return;
        }

        try {
            switch (inputData.getAction()) {
                case LOAD -> present(run, run.isComplete()
                        ? "Gauntlet complete."
                        : "Ready for the next fight.");
                case SIMULATE_NEXT -> simulateNext(run);
                case AUTO_SIMULATE -> autoSimulate(run);
                default -> throw new IllegalStateException("Unsupported simulation action.");
            }
        }
        catch (IllegalArgumentException | IllegalStateException exception) {
            presenter.prepareFailView(exception.getMessage());
        }
    }

    private void simulateNext(GameRun run) {
        if (run.isComplete()) {
            present(run, "Gauntlet complete.");
            return;
        }

        simulateCurrentOpponent(run);
        dataAccess.saveGameRun(run);

        final String message = run.isComplete()
                ? "Gauntlet complete. All 15 fights were simulated."
                : "Fight complete. Ready for the next opponent.";
        present(run, message);
    }

    private void autoSimulate(GameRun run) {
        if (run.isComplete()) {
            present(run, "Gauntlet complete.");
            return;
        }

        while (!run.isComplete()) {
            simulateCurrentOpponent(run);
        }

        dataAccess.saveGameRun(run);
        present(run, "Gauntlet complete. All 15 fights were simulated.");
    }

    private void simulateCurrentOpponent(GameRun run) {
        final FightResult result = fightSimulator.simulate(
                run.getPlayer(),
                run.getCurrentOpponent(),
                run.getRoundsPerFight(),
                run.getDifficulty());
        run.recordResult(result);
    }

    private void present(GameRun run, String message) {
        final List<SimulationOutputData.OpponentData> opponents = new ArrayList<>();
        final List<FightResult> history = run.getFightHistory();

        for (int index = 0; index < run.getOpponents().size(); index++) {
            final RealFighter opponent = run.getOpponents().get(index);
            final SimulationOutputData.OpponentStatus status;

            if (index < history.size()) {
                status = history.get(index).isPlayerWon()
                        ? SimulationOutputData.OpponentStatus.WIN
                        : SimulationOutputData.OpponentStatus.LOSS;
            }
            else if (index == history.size() && !run.isComplete()) {
                status = SimulationOutputData.OpponentStatus.NEXT;
            }
            else {
                status = SimulationOutputData.OpponentStatus.PENDING;
            }

            opponents.add(new SimulationOutputData.OpponentData(
                    opponent.getRank(),
                    opponent.getName(),
                    opponent.getAttributes(),
                    status));
        }

        final List<SimulationOutputData.ResultData> resultData = history.stream()
                .map(result -> new SimulationOutputData.ResultData(
                        result.getOpponent().getRank(),
                        result.getOpponent().getName(),
                        result.isPlayerWon(),
                        result.getMethod(),
                        result.getRound(),
                        result.getSecondsInRound()))
                .toList();

        final RealFighter current = run.getCurrentOpponent();
        final SimulationOutputData.OpponentData currentOutput =
                current == null
                        ? null
                        : new SimulationOutputData.OpponentData(
                                current.getRank(),
                                current.getName(),
                                current.getAttributes(),
                                SimulationOutputData.OpponentStatus.NEXT);

        presenter.prepareSuccessView(new SimulationOutputData(
                run.getPlayer().getName(),
                run.getDivision().getWeightClass(),
                run.getPlayer().getRecord().getWins(),
                run.getPlayer().getRecord().getLosses(),
                run.isHideOpponentStats(),
                run.isComplete(),
                currentOutput,
                opponents,
                resultData,
                message));
    }
}
