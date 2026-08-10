package use_case.confirm;

import data_access.DemoRankingsFactory;
import entity.CustomFighter;
import entity.Difficulty;
import entity.Division;
import entity.GameRun;
import entity.GameSettings;
import entity.WeightClass;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfirmInteractorTest {
    private static final List<String> VALID_ATTRIBUTES =
            List.of("10", "20", "30", "40", "50", "60");

    @Test
    void spinProducesAValidWeightClassAndOverall() {
        final CapturingPresenter presenter = new CapturingPresenter();

        new ConfirmInteractor(presenter).spin(new ConfirmInputData(
                "Player", List.of("80", "80", "80", "80", "80", "80"), null));

        assertNotNull(presenter.spinOutput);
        assertEquals("Player", presenter.spinOutput.getFighterName());
        assertEquals(80, presenter.spinOutput.getOverall());
        assertTrue(List.of(
                "Flyweight", "Bantamweight", "Featherweight", "Lightweight",
                "Welterweight", "Middleweight", "Light Heavyweight", "Heavyweight")
                .contains(presenter.spinOutput.getWeightClass()));
        assertNull(presenter.error);
    }

    @Test
    void confirmCalculatesEveryWeightGroup() {
        assertOverall("Flyweight", 35);
        assertOverall("Bantamweight", 35);
        assertOverall("Featherweight", 33);
        assertOverall("Lightweight", 33);
        assertOverall("Welterweight", 31);
        assertOverall("Middleweight", 31);
        assertOverall("Light Heavyweight", 30);
        assertOverall("Heavyweight", 30);
    }

    @Test
    void invalidAttributeInputsProduceSpecificFailures() {
        assertSpinFailure(null, "All 6 values must be assigned");
        assertSpinFailure(List.of("1", "2"), "All 6 values must be assigned");
        assertSpinFailure(List.of("1", "2", "3", "4", "5", "TBD"),
                "All 6 values must be assigned");
        assertSpinFailure(List.of("1", "2", "3", "4", "5", " "),
                "All 6 values must be assigned");
        assertSpinFailure(List.of("1", "2", "3", "4", "5", "abc"),
                "Attribute values must be numbers");
        assertSpinFailure(List.of("1", "2", "3", "4", "5", "-1"),
                "Attributes must be between 0 and 100");
        assertSpinFailure(List.of("1", "2", "3", "4", "5", "101"),
                "Attributes must be between 0 and 100");
    }

    @Test
    void confirmRejectsMissingNameAttributesAndWeightClass() {
        final CapturingPresenter nullPresenter = new CapturingPresenter();
        new ConfirmInteractor(nullPresenter).confirm(null);
        assertEquals("Fighter name cannot be empty", nullPresenter.error);

        final CapturingPresenter namePresenter = new CapturingPresenter();
        new ConfirmInteractor(namePresenter).confirm(new ConfirmInputData(
                "", VALID_ATTRIBUTES, "Lightweight"));
        assertEquals("Fighter name cannot be empty", namePresenter.error);

        final CapturingPresenter emptyPresenter = new CapturingPresenter();
        new ConfirmInteractor(emptyPresenter).confirm(new ConfirmInputData(
                "Player", List.of(), "Lightweight"));
        assertEquals("Fighter name cannot be empty", emptyPresenter.error);

        final CapturingPresenter weightPresenter = new CapturingPresenter();
        new ConfirmInteractor(weightPresenter).confirm(new ConfirmInputData(
                "Player", VALID_ATTRIBUTES, "TBD"));
        assertEquals("Spin weight class before confirming", weightPresenter.error);
    }

    @Test
    void confirmCreatesAndSavesTheConfiguredGauntlet() {
        final CapturingPresenter presenter = new CapturingPresenter();
        final CustomFighter draft = new CustomFighter("Custom Fighter");
        final GameSettings settings = new GameSettings(
                Difficulty.HARD, 5, entity.UfcEra.MODERN, true);
        final FakeRunDataAccess dataAccess =
                new FakeRunDataAccess(draft, settings);

        new ConfirmInteractor(presenter, dataAccess).confirm(
                new ConfirmInputData(
                        "Final Name", VALID_ATTRIBUTES, "Lightweight"));

        assertNotNull(presenter.confirmOutput);
        assertNotNull(dataAccess.savedRun);
        assertSame(draft, dataAccess.savedRun.getPlayer());
        assertEquals("Final Name", draft.getName());
        assertEquals(WeightClass.LIGHTWEIGHT, draft.getWeightClass());
        assertEquals(Difficulty.HARD, dataAccess.savedRun.getDifficulty());
        assertEquals(5, dataAccess.savedRun.getRoundsPerFight());
        assertTrue(dataAccess.savedRun.isHideOpponentStats());
        assertFalse(presenter.confirmOutput.getOverall() < 0);
    }

    @Test
    void confirmFailsFastWhenConfiguredRunStateIsMissing() {
        final CapturingPresenter presenter = new CapturingPresenter();
        final FakeRunDataAccess dataAccess = new FakeRunDataAccess(
                null, new GameSettings(
                        Difficulty.NORMAL, 3, entity.UfcEra.ALL_TIME, false));

        final IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new ConfirmInteractor(presenter, dataAccess).confirm(
                        new ConfirmInputData(
                                "Player", VALID_ATTRIBUTES, "Welterweight")));

        assertEquals("The configured fighter run is missing.",
                exception.getMessage());
    }

    private static void assertOverall(String weightClass, int expected) {
        final CapturingPresenter presenter = new CapturingPresenter();
        new ConfirmInteractor(presenter).confirm(new ConfirmInputData(
                "Player", VALID_ATTRIBUTES, weightClass));
        assertEquals(expected, presenter.confirmOutput.getOverall(), weightClass);
        assertEquals(weightClass, presenter.confirmOutput.getWeightClass());
    }

    private static void assertSpinFailure(List<String> attributes,
                                          String expectedMessage) {
        final CapturingPresenter presenter = new CapturingPresenter();
        final ConfirmInteractor interactor = new ConfirmInteractor(presenter);
        if (attributes == null) {
            interactor.spin(null);
        }
        else {
            interactor.spin(new ConfirmInputData("Player", attributes, null));
        }
        assertEquals(expectedMessage, presenter.error);
        assertNull(presenter.spinOutput);
    }

    private static final class CapturingPresenter
            implements ConfirmOutputBoundary {
        private ConfirmOutputData spinOutput;
        private ConfirmOutputData confirmOutput;
        private String error;

        @Override
        public void prepareSpinSuccessView(ConfirmOutputData outputData) {
            spinOutput = outputData;
        }

        @Override
        public void prepareConfirmSuccessView(ConfirmOutputData outputData) {
            confirmOutput = outputData;
        }

        @Override
        public void prepareFailureView(String message) {
            error = message;
        }
    }

    private static final class FakeRunDataAccess
            implements ConfirmRunDataAccessInterface {
        private final CustomFighter fighter;
        private final GameSettings settings;
        private GameRun savedRun;

        private FakeRunDataAccess(CustomFighter fighter,
                                  GameSettings settings) {
            this.fighter = fighter;
            this.settings = settings;
        }

        @Override
        public CustomFighter getCustomFighter() {
            return fighter;
        }

        @Override
        public GameSettings getGameSettings() {
            return settings;
        }

        @Override
        public Division getDivision(WeightClass weightClass) {
            return DemoRankingsFactory.createDivision(weightClass);
        }

        @Override
        public void saveGameRun(GameRun gameRun) {
            savedRun = gameRun;
        }
    }
}
