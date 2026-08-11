package use_case.reroll_fighter;

import java.util.ArrayList;
import java.util.List;

import entity.RandomSource;
import entity.RealFighter;
import entity.UfcEra;
import use_case.fighter_creation.FighterDataAccessInterface;
import use_case.fighter_creation.FighterDetailsDataAccessInterface;

public class RerollFighterInteractor implements RerollFighterInputBoundary {

    private final RandomSource randomSource;
    private final FighterDataAccessInterface fighterDataAccess;
    private final FighterDetailsDataAccessInterface fighterDetailsDataAccess;
    private final RerollFighterOutputBoundary presenter;

    public RerollFighterInteractor(RandomSource randomSource,
                                   FighterDataAccessInterface fighterDataAccess,
                                   RerollFighterOutputBoundary presenter) {
        this(randomSource, fighterDataAccess, null, presenter);
    }

    public RerollFighterInteractor(RandomSource randomSource,
                                   FighterDataAccessInterface fighterDataAccess,
                                   FighterDetailsDataAccessInterface fighterDetailsDataAccess,
                                   RerollFighterOutputBoundary presenter) {
        this.randomSource = randomSource;
        this.fighterDataAccess = fighterDataAccess;
        this.fighterDetailsDataAccess = fighterDetailsDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(RerollFighterInputData inputData) {
        if (inputData.getCurrentFighter() == null) {
            presenter.prepareFailView("Spin a fighter before rerolling.");
        }
        else if (inputData.getRerollsLeft() <= 0) {
            presenter.prepareFailView("No rerolls remaining.");
        }
        else {
            final UfcEra fighterEra = inputData.getEra();
            final List<RealFighter> fighters = fighterDataAccess.getFighters();
            final List<RealFighter> eligibleFighters = new ArrayList<>();
            for (RealFighter fighter : fighters) {
                if ((fighterEra == UfcEra.ALL_TIME || fighter.getEra() == fighterEra)
                        && !fighter.getName().equals(inputData.getCurrentFighter().getName())) {
                    eligibleFighters.add(fighter);
                }
            }

            /*
             * Invariant: the fighter data source provides at least two eligible fighters
             * for every supported UFC era.
             */
            final int index = randomSource.nextInt(eligibleFighters.size());
            RealFighter fighter = eligibleFighters.get(index);
            if (fighterDetailsDataAccess != null) {
                fighter = fighterDetailsDataAccess.getFighterDetails(fighter);
            }
            final RerollFighterOutputData outputData =
                    new RerollFighterOutputData(fighter, inputData.getRerollsLeft() - 1);
            presenter.prepareSuccessView(outputData);
        }
    }
}
