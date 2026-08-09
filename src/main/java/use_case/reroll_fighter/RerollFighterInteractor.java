package use_case.reroll_fighter;


import entity.RandomSource;
import entity.RealFighter;
import entity.UfcEra;
import use_case.fighter_creation.FighterDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class RerollFighterInteractor implements RerollFighterInputBoundary {

    private final RandomSource randomSource;
    private final FighterDataAccessInterface fighterDataAccess;
    private final RerollFighterOutputBoundary presenter;

    public RerollFighterInteractor(RandomSource randomSource, FighterDataAccessInterface fighterDataAccess, RerollFighterOutputBoundary presenter) {
        this.randomSource = randomSource;
        this.fighterDataAccess = fighterDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(RerollFighterInputData inputData) {
        if (inputData.getRerollsLeft() <= 0) {
            presenter.prepareFailView("No rerolls remaining.");
            return;
        }
        final UfcEra fighterEra = inputData.getEra();
        final List<RealFighter> fighters = fighterDataAccess.getFighters();
        final List<RealFighter> eligibleFighters = new ArrayList<>();
        for (RealFighter fighter : fighters) {
            if ((fighterEra == UfcEra.ALL_TIME || fighter.getEra() == fighterEra) && !fighter.getName().equals(inputData.getCurrentFighter().getName())) {
                eligibleFighters.add(fighter);
            }
        }

        /*
         * Invariant: the fighter data source provides at least two eligible fighters
         * for every supported UFC era.
         */
        final int index = randomSource.nextInt(eligibleFighters.size());
        final RealFighter fighter = eligibleFighters.get(index);
        final RerollFighterOutputData outputData = new RerollFighterOutputData(fighter, inputData.getRerollsLeft() - 1);
        presenter.prepareSuccessView(outputData);
    }



}
