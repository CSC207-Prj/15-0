package use_case.spin_fighter;


import entity.RandomSource;
import entity.RealFighter;
import entity.UfcEra;
import use_case.fighter_creation.FighterDataAccessInterface;
import use_case.fighter_creation.FighterDetailsDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class SpinFighterInteractor implements SpinFighterInputBoundary {

    private final RandomSource randomSource;
    private final FighterDataAccessInterface fighterDataAccess;
    private final FighterDetailsDataAccessInterface fighterDetailsDataAccess;
    private final SpinFighterOutputBoundary presenter;

    public SpinFighterInteractor(RandomSource randomSource,
                                 FighterDataAccessInterface fighterDataAccess,
                                 SpinFighterOutputBoundary presenter) {
        this(randomSource, fighterDataAccess, null, presenter);
    }

    public SpinFighterInteractor(RandomSource randomSource,
                                 FighterDataAccessInterface fighterDataAccess,
                                 FighterDetailsDataAccessInterface fighterDetailsDataAccess,
                                 SpinFighterOutputBoundary presenter) {
        this.randomSource = randomSource;
        this.fighterDataAccess = fighterDataAccess;
        this.fighterDetailsDataAccess = fighterDetailsDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SpinFighterInputData inputData) {
        final UfcEra fighterEra = inputData.getEra();
        final List<RealFighter> fighters = fighterDataAccess.getFighters();
        final List<RealFighter> eligibleFighters = new ArrayList<>();
        for (RealFighter fighter : fighters) {
            if (fighterEra == UfcEra.ALL_TIME || fighter.getEra() == fighterEra) {
                eligibleFighters.add(fighter);
            }
        }
        final int index = randomSource.nextInt(eligibleFighters.size());
        RealFighter fighter = eligibleFighters.get(index);
        if (fighterDetailsDataAccess != null) {
            fighter = fighterDetailsDataAccess.getFighterDetails(fighter);
        }
        final SpinFighterOutputData outputData = new SpinFighterOutputData(fighter);
        presenter.prepareSuccessView(outputData);
    }



}
