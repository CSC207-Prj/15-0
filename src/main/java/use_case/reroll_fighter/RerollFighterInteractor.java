package use_case.reroll_fighter;

import entity.CustomFighter;
import entity.RandomSource;
import entity.RealFighter;
import entity.UfcEra;
import use_case.fighter_creation.FighterDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Replaces the currently shown source fighter and consumes one reroll.
 */
public class RerollFighterInteractor
        implements RerollFighterInputBoundary {

    private final RandomSource randomSource;
    private final FighterDataAccessInterface fighterDataAccess;
    private final RerollFighterOutputBoundary presenter;

    public RerollFighterInteractor(
            RandomSource randomSource,
            FighterDataAccessInterface fighterDataAccess,
            RerollFighterOutputBoundary presenter) {
        this.randomSource = randomSource;
        this.fighterDataAccess = fighterDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(RerollFighterInputData inputData) {
        if (inputData.getRerollsLeft() <= 0) {
            presenter.prepareFailView(
                    "No rerolls remaining."
            );
            return;
        }

        if (inputData.getCurrentFighter() == null) {
            presenter.prepareFailView(
                    "Spin a fighter before using a reroll."
            );
            return;
        }

        final UfcEra fighterEra = inputData.getEra();
        final CustomFighter customFighter =
                inputData.getCustomFighter();

        final List<RealFighter> eligibleFighters =
                new ArrayList<>();

        for (RealFighter fighter
                : fighterDataAccess.getFighters()) {

            final boolean correctEra =
                    fighterEra == UfcEra.ALL_TIME
                            || fighter.getEra() == fighterEra;

            final boolean sameAsCurrent =
                    fighter.getName().equals(
                            inputData.getCurrentFighter().getName()
                    );

            final boolean alreadyUsed =
                    customFighter != null
                            && customFighter.hasUsedSourceFighter(
                                    fighter.getName()
                            );

            if (correctEra
                    && !sameAsCurrent
                    && !alreadyUsed) {
                eligibleFighters.add(fighter);
            }
        }

        if (eligibleFighters.isEmpty()) {
            presenter.prepareFailView(
                    "No other unused fighter is available."
            );
            return;
        }

        final int index =
                randomSource.nextInt(eligibleFighters.size());

        presenter.prepareSuccessView(
                new RerollFighterOutputData(
                        eligibleFighters.get(index),
                        inputData.getRerollsLeft() - 1
                )
        );
    }
}
