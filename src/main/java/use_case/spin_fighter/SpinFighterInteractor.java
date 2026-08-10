package use_case.spin_fighter;

import entity.CustomFighter;
import entity.RandomSource;
import entity.RealFighter;
import entity.UfcEra;
import use_case.fighter_creation.FighterDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Selects an eligible real fighter for the attribute draft.
 */
public class SpinFighterInteractor
        implements SpinFighterInputBoundary {

    private final RandomSource randomSource;
    private final FighterDataAccessInterface fighterDataAccess;
    private final SpinFighterOutputBoundary presenter;

    public SpinFighterInteractor(
            RandomSource randomSource,
            FighterDataAccessInterface fighterDataAccess,
            SpinFighterOutputBoundary presenter) {
        this.randomSource = randomSource;
        this.fighterDataAccess = fighterDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SpinFighterInputData inputData) {
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

            final boolean alreadyUsed =
                    customFighter != null
                            && customFighter.hasUsedSourceFighter(
                                    fighter.getName()
                            );

            if (correctEra && !alreadyUsed) {
                eligibleFighters.add(fighter);
            }
        }

        if (eligibleFighters.isEmpty()) {
            presenter.prepareFailView(
                    "No unused fighters remain in the selected era."
            );
            return;
        }

        final int index =
                randomSource.nextInt(eligibleFighters.size());

        presenter.prepareSuccessView(
                new SpinFighterOutputData(
                        eligibleFighters.get(index)
                )
        );
    }
}
