package use_case.game_setting;

import entity.CustomFighter;
import entity.GameSettings;
import entity.RealFighter;
import entity.UfcEra;
import use_case.fighter_creation.FighterDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Interactor for the Configure a New Run user story.
 */
public class GameSettingInteractor implements GameSettingBoundary {

    private final FighterDataAccessInterface fighterDataAccess;
    private final GameSettingOutputBoundary presenter;
    private final GameSettingSessionDataAccessInterface sessionDataAccess;

    /**
     * Constructor used by the application.
     */
    public GameSettingInteractor(
            FighterDataAccessInterface fighterDataAccess,
            GameSettingOutputBoundary presenter,
            GameSettingSessionDataAccessInterface sessionDataAccess) {
        this.fighterDataAccess = fighterDataAccess;
        this.presenter = presenter;
        this.sessionDataAccess = sessionDataAccess;
    }

    /**
     * Backwards-compatible constructor retained for the existing unit tests.
     */
    public GameSettingInteractor(
            FighterDataAccessInterface fighterDataAccess,
            GameSettingOutputBoundary presenter) {
        this(fighterDataAccess, presenter, null);
    }

    @Override
    public void execute(GameSettingInputData inputData) {
        if (inputData == null) {
            presenter.prepareFailView(
                    "Game settings input is missing."
            );
            return;
        }

        final GameSettings settings = new GameSettings(
                inputData.getDifficulty(),
                inputData.getRoundsPerFight(),
                inputData.getEra(),
                inputData.isHideOpponentStats()
        );

        if (!settings.isValid()) {
            presenter.prepareFailView(
                    "Please complete all game settings before continuing."
            );
            return;
        }

        final List<RealFighter> allFighters =
                fighterDataAccess.getFighters();

        if (allFighters == null || allFighters.isEmpty()) {
            presenter.prepareFailView(
                    "No fighter data is currently available."
            );
            return;
        }

        final List<RealFighter> eligibleFighters =
                filterByEra(allFighters, settings.getEra());

        final CustomFighter customFighter =
                new CustomFighter("Custom Fighter");

        if (sessionDataAccess != null) {
            sessionDataAccess.saveConfiguredRun(
                    settings,
                    customFighter,
                    eligibleFighters
            );
        }

        presenter.prepareSuccessView(
                new GameSettingOutputData(
                        settings,
                        customFighter,
                        eligibleFighters
                )
        );
    }

    private List<RealFighter> filterByEra(
            List<RealFighter> fighters,
            UfcEra selectedEra) {

        if (selectedEra == UfcEra.ALL_TIME) {
            return new ArrayList<>(fighters);
        }

        final List<RealFighter> eligibleFighters =
                new ArrayList<>();

        for (RealFighter fighter : fighters) {
            if (fighter != null
                    && fighter.getEra() == selectedEra) {
                eligibleFighters.add(fighter);
            }
        }

        return eligibleFighters;
    }
}
