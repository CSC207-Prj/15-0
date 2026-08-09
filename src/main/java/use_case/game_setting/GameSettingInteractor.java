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
 * Responsibilities:
 * 1. Build and validate GameSettings.
 * 2. Ask the existing fighter data source for all RealFighters.
 * 3. Apply the selected UFC era as a real filtering rule.
 * 4. Create a blank CustomFighter.
 * 5. Send the configured run data to the presenter.
 */
public class GameSettingInteractor implements GameSettingBoundary {

    private final FighterDataAccessInterface fighterDataAccess;
    private final GameSettingOutputBoundary presenter;

    public GameSettingInteractor(FighterDataAccessInterface fighterDataAccess,
                                 GameSettingOutputBoundary presenter) {
        this.fighterDataAccess = fighterDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(GameSettingInputData inputData) {

        if (inputData == null) {
            presenter.prepareFailView("Game settings input is missing.");
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

        final List<RealFighter> allFighters = fighterDataAccess.getFighters();

        if (allFighters == null || allFighters.isEmpty()) {
            presenter.prepareFailView(
                    "No fighter data is currently available."
            );
            return;
        }

        final List<RealFighter> eligibleFighters =
                filterByEra(allFighters, settings.getEra());

        if (eligibleFighters.isEmpty()) {
            presenter.prepareFailView(
                    "No fighters are available for the selected UFC era."
            );
            return;
        }

        final CustomFighter customFighter =
                new CustomFighter("Custom Fighter");

        final GameSettingOutputData outputData =
                new GameSettingOutputData(
                        settings,
                        customFighter,
                        eligibleFighters
                );

        presenter.prepareSuccessView(outputData);
    }


    private List<RealFighter> filterByEra(List<RealFighter> fighters,
                                          UfcEra selectedEra) {

        if (selectedEra == UfcEra.ALL_TIME) {
            return new ArrayList<>(fighters);
        }

        final List<RealFighter> eligibleFighters = new ArrayList<>();

        for (RealFighter fighter : fighters) {
            if (fighter != null && fighter.getEra() == selectedEra) {
                eligibleFighters.add(fighter);
            }
        }

        return eligibleFighters;
    }
}