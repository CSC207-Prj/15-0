package use_case.game_setting;

import entity.Attribute;
import entity.Difficulty;
import entity.RealFighter;
import entity.UfcEra;
import entity.WeightClass;
import org.junit.jupiter.api.Test;
import use_case.fighter_creation.FighterDataAccessInterface;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the main business rules owned by the Configure a New Run user story.
 */
class GameSettingInteractorTest {

    @Test
    void modernEraOnlyReturnsModernFighters() {
        final RealFighter early = fighter("Early", UfcEra.EARLY_UFC);
        final RealFighter modern = fighter("Modern", UfcEra.MODERN);

        final TestPresenter presenter = new TestPresenter();

        final GameSettingInteractor interactor =
                new GameSettingInteractor(
                        () -> List.of(early, modern),
                        presenter
                );

        interactor.execute(
                new GameSettingInputData(
                        Difficulty.NORMAL,
                        3,
                        UfcEra.MODERN,
                        false
                )
        );

        assertNull(presenter.errorMessage);
        assertNotNull(presenter.outputData);
        assertEquals(
                List.of(modern),
                presenter.outputData.getEligibleFighters()
        );
        assertNotNull(presenter.outputData.getCustomFighter());
    }

    @Test
    void allTimeReturnsEveryFighter() {
        final RealFighter early = fighter("Early", UfcEra.EARLY_UFC);
        final RealFighter zuffa = fighter("Zuffa", UfcEra.ZUFFA_ERA);
        final RealFighter modern = fighter("Modern", UfcEra.MODERN);

        final TestPresenter presenter = new TestPresenter();

        final GameSettingInteractor interactor =
                new GameSettingInteractor(
                        () -> List.of(early, zuffa, modern),
                        presenter
                );

        interactor.execute(
                new GameSettingInputData(
                        Difficulty.EASY,
                        5,
                        UfcEra.ALL_TIME,
                        true
                )
        );

        assertNull(presenter.errorMessage);
        assertEquals(
                3,
                presenter.outputData
                        .getEligibleFighters()
                        .size()
        );
    }

    @Test
    void invalidSettingsFail() {
        final TestPresenter presenter = new TestPresenter();

        final FighterDataAccessInterface dataAccess =
                () -> List.of(fighter("Modern", UfcEra.MODERN));

        final GameSettingInteractor interactor =
                new GameSettingInteractor(
                        dataAccess,
                        presenter
                );

        interactor.execute(
                new GameSettingInputData(
                        null,
                        3,
                        UfcEra.MODERN,
                        false
                )
        );

        assertNull(presenter.outputData);
        assertNotNull(presenter.errorMessage);
    }

    private static RealFighter fighter(String name,
                                       UfcEra era) {

        final Map<Attribute, Double> attributes =
                new EnumMap<>(Attribute.class);

        for (Attribute attribute : Attribute.values()) {
            attributes.put(attribute, 75.0);
        }

        return new RealFighter(
                name,
                WeightClass.LIGHTWEIGHT,
                0,
                era,
                "0-0",
                attributes
        );
    }

    private static class TestPresenter
            implements GameSettingOutputBoundary {

        private GameSettingOutputData outputData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(
                GameSettingOutputData outputData) {
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(
                String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
