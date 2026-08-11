package use_case.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import entity.FightResult;
import entity.FightSimulator;
import entity.GameRun;
import entity.RealFighter;

/**
 * Coordinates the application-specific business rules for User Story 4.
 *
 * The interactor loads the active GameRun, assigns/delegates individual
 * fight outcomes to the injected FightSimulator, persists run
 * mutations through SimulationDataAccessInterface, and sends raw
 * output through SimulationOutputBoundary. It purposely contains
 * no Swing, API, or concrete persistence dependencies.
 */
public final class SimulationInteractor
        implements SimulationInputBoundary {

    private final SimulationDataAccessInterface dataAccess;
    private final FightSimulator fightSimulator;
    private final SimulationOutputBoundary presenter;

    /**
     * Creates a simulation interactor with all outer collaborators injected.
     *
     * @param dataAccess abstraction used to load and save the active game run
     * @param fightSimulator strategy used to resolve one fight
     * @param presenter output boundary used to publish success or failure data
     * @throws NullPointerException if any dependency is null
     */
    public SimulationInteractor(
            SimulationDataAccessInterface dataAccess,
            FightSimulator fightSimulator,
            SimulationOutputBoundary presenter) {

        this.dataAccess =
                Objects.requireNonNull(
                        dataAccess,
                        "dataAccess");

        this.fightSimulator =
                Objects.requireNonNull(
                        fightSimulator,
                        "fightSimulator");

        this.presenter =
                Objects.requireNonNull(
                        presenter,
                        "presenter");
    }

    @Override
    public void execute(
            SimulationInputData inputData) {

        Objects.requireNonNull(
                inputData,
                "inputData");

        final GameRun run =
                dataAccess.getGameRun();

        if (run == null) {
            presenter.prepareFailView(
                    "No active gauntlet exists. "
                            + "Finalize a fighter before "
                            + "starting the simulation.");
        }
        else {
            try {
                switch (inputData.getAction()) {
                    case LOAD ->
                            present(
                                    run,
                                    loadMessage(run));

                    case SIMULATE_NEXT ->
                            simulateNext(run);

                    case AUTO_SIMULATE ->
                            autoSimulate(run);

                    default ->
                            throw new IllegalStateException(
                                    "Unsupported simulation action.");
                }
            }
            catch (IllegalArgumentException
                   | IllegalStateException exception) {

                presenter.prepareFailView(
                        exception.getMessage());
            }
        }
    }

    /**
     * Builds the status message used when an existing run is loaded.
     *
     * @param run active gauntlet run
     * @return message describing whether the run is ready or complete
     */
    private String loadMessage(GameRun run) {
        final String message;

        if (run.isComplete()) {
            message = "Gauntlet complete.";
        }
        else {
            message =
                    "Ready for the next fight.";
        }

        return message;
    }

    /**
     * Simulates the current opponent once, saves the mutation,
     * and presents the run.
     *
     * @param run active gauntlet run
     */
    private void simulateNext(GameRun run) {
        if (run.isComplete()) {
            present(
                    run,
                    "Gauntlet complete.");
        }
        else {
            simulateCurrentOpponent(run);

            dataAccess.saveGameRun(run);

            final String message;

            if (run.isComplete()) {
                message =
                        "Gauntlet complete. "
                                + "All 15 fights were simulated.";
            }
            else {
                message =
                        "Fight complete. "
                                + "Ready for the next opponent.";
            }

            present(run, message);
        }
    }

    /**
     * Simulates every remaining opponent, saves the completed run,
     * and presents it.
     *
     * @param run active gauntlet run
     */
    private void autoSimulate(GameRun run) {
        if (run.isComplete()) {
            present(
                    run,
                    "Gauntlet complete.");
        }
        else {
            while (!run.isComplete()) {
                simulateCurrentOpponent(run);
            }

            dataAccess.saveGameRun(run);

            present(
                    run,
                    "Gauntlet complete. "
                            + "All 15 fights were simulated.");
        }
    }

    /**
     * Delegates one fight to the strategy and records the returned
     * domain result.
     *
     * @param run active gauntlet run
     */
    private void simulateCurrentOpponent(
            GameRun run) {

        final FightResult result =
                fightSimulator.simulate(
                        run.getPlayer(),
                        run.getCurrentOpponent(),
                        run.getRoundsPerFight(),
                        run.getDifficulty());

        run.recordResult(result);
    }

    /**
     * Converts current entity state into raw use-case output for the presenter.
     *
     * @param run active gauntlet run
     * @param message status message describing the most recent action
     */
    private void present(
            GameRun run,
            String message) {

        final List<SimulationOutputData.OpponentData> opponents =
                new ArrayList<>();

        final List<FightResult> history =
                run.getFightHistory();

        for (int index = 0;
             index < run.getOpponents().size();
             index++) {

            final RealFighter opponent =
                    run.getOpponents().get(index);

            final SimulationOutputData.OpponentStatus status;

            if (index < history.size()) {
                if (history.get(index).isPlayerWon()) {
                    status =
                            SimulationOutputData
                                    .OpponentStatus.WIN;
                }
                else {
                    status =
                            SimulationOutputData
                                    .OpponentStatus.LOSS;
                }
            }
            else if (index == history.size()
                    && !run.isComplete()) {

                status =
                        SimulationOutputData
                                .OpponentStatus.NEXT;
            }
            else {
                status =
                        SimulationOutputData
                                .OpponentStatus.PENDING;
            }

            opponents.add(
                    new SimulationOutputData.OpponentData(
                            opponent.getRank(),
                            opponent.getName(),
                            opponent.getAttributes(),
                            status));
        }

        final List<SimulationOutputData.ResultData> resultData =
                history.stream()
                        .map(result ->
                                new SimulationOutputData.ResultData(
                                        result.getOpponent().getRank(),
                                        result.getOpponent().getName(),
                                        result.isPlayerWon(),
                                        result.getMethod(),
                                        result.getRound(),
                                        result.getSecondsInRound()))
                        .toList();

        final RealFighter current =
                run.getCurrentOpponent();

        final SimulationOutputData.OpponentData currentOutput;

        if (current == null) {
            currentOutput = null;
        }
        else {
            currentOutput =
                    new SimulationOutputData.OpponentData(
                            current.getRank(),
                            current.getName(),
                            current.getAttributes(),
                            SimulationOutputData
                                    .OpponentStatus.NEXT);
        }

        presenter.prepareSuccessView(
                new SimulationOutputData(
                        run.getPlayer().getName(),
                        run.getDivision()
                                .getWeightClass(),
                        run.getPlayer()
                                .getRecord()
                                .getWins(),
                        run.getPlayer()
                                .getRecord()
                                .getLosses(),
                        run.isHideOpponentStats(),
                        run.isComplete(),
                        currentOutput,
                        opponents,
                        resultData,
                        message));
    }
}
