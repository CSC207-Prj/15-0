package use_case.game_setting;

import entity.CustomFighter;
import entity.GameSettings;
import entity.RealFighter;
import entity.UfcEra;
import use_case.fighter.FighterDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Interactor for configuring a new game run.
 * Responsibilities:
 * 1. Create and validate GameSettings.
 * 2. Get all real fighters from the project's existing data source.
 * 3. Filter fighters based on the selected UFC era.
 * 4. Create a blank CustomFighter.
 * 5. Send the result to the presenter.
 */
public class GameSettingInteractor implements GameSettingBoundary {

    private final FighterDataAccessInterface fighterDataAccess;
    private final GameSettingOutputBoundary presenter;

    public GameSettingInteractor(
            FighterDataAccessInterface fighterDataAccess,
            GameSettingOutputBoundary presenter) {

        this.fighterDataAccess = fighterDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(GameSettingInputData inputData) {

        GameSettings settings = new GameSettings(
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

        List<RealFighter> allFighters =
                fighterDataAccess.getFighters();

        if (allFighters == null || allFighters.isEmpty()) {
            presenter.prepareFailView(
                    "No fighter data is currently available."
            );
            return;
        }

        List<RealFighter> eligibleFighters =
                filterByEra(
                        allFighters,
                        settings.getEra()
                );

        if (eligibleFighters.isEmpty()) {
            presenter.prepareFailView(
                    "No fighters are available for the selected UFC era."
            );
            return;
        }

        CustomFighter customFighter =
                new CustomFighter("Custom Fighter");

        GameSettingOutputData outputData =
                new GameSettingOutputData(
                        settings,
                        customFighter,
                        eligibleFighters
                );

        presenter.prepareSuccessView(outputData);
    }

    private List<RealFighter> filterByEra(
            List<RealFighter> fighters,
            UfcEra selectedEra) {

        if (selectedEra == UfcEra.ALL_TIME) {
            return new ArrayList<>(fighters);
        }

        List<RealFighter> eligibleFighters =
                new ArrayList<>();

        for (RealFighter fighter : fighters) {
            if (fighter.getEra() == selectedEra) {
                eligibleFighters.add(fighter);
            }
        }

        return eligibleFighters;
    }
}