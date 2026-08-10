package use_case.browse_fighters;

import entity.Attribute;
import entity.RealFighter;
import entity.UfcEra;
import entity.WeightClass;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BrowseFightersInteractorTest {

    @Test
    void filtersBySearchWeightClassAndEraAndPresentsProfile() {
        final RealFighter early = fighter(
                "Royce Gracie", WeightClass.WELTERWEIGHT,
                UfcEra.EARLY_UFC, 0);
        final RealFighter modern = fighter(
                "Georges St-Pierre", WeightClass.WELTERWEIGHT,
                UfcEra.MODERN, 2);
        final RealFighter otherWeight = fighter(
                "Max Holloway", WeightClass.FEATHERWEIGHT,
                UfcEra.MODERN, 3);
        final CapturingPresenter presenter = new CapturingPresenter();

        new BrowseFightersInteractor(
                () -> List.of(early, modern, otherWeight), presenter)
                .execute(new BrowseFightersInputData(
                        "  GEORGES  ", WeightClass.WELTERWEIGHT,
                        UfcEra.MODERN, "Georges St-Pierre"));

        assertNull(presenter.error);
        assertEquals(1, presenter.output.getFighters().size());
        assertEquals("Georges St-Pierre",
                presenter.output.getFighters().get(0).getName());
        assertEquals("Welterweight",
                presenter.output.getFighters().get(0).getWeightClass());
        assertEquals(UfcEra.MODERN.getDisplayName(),
                presenter.output.getFighters().get(0).getEra());
        assertEquals("Georges St-Pierre",
                presenter.output.getSelectedFighter().getName());
        assertEquals(2, presenter.output.getSelectedFighter().getRank());
        assertEquals("10-0",
                presenter.output.getSelectedFighter().getProfessionalRecord());
        assertEquals(75.0,
                presenter.output.getSelectedFighter()
                        .getAttributes().get(Attribute.STRIKING));
    }

    @Test
    void allTimeSortsResultsAndFallsBackToFirstFighter() {
        final RealFighter zulu = fighter(
                "Zulu", WeightClass.HEAVYWEIGHT, UfcEra.EARLY_UFC, 0);
        final RealFighter alpha = fighter(
                "Alpha", WeightClass.LIGHTWEIGHT, UfcEra.MODERN, 1);
        final CapturingPresenter presenter = new CapturingPresenter();

        new BrowseFightersInteractor(
                () -> java.util.Arrays.asList(zulu, null, alpha), presenter)
                .execute(new BrowseFightersInputData(
                        null, null, UfcEra.ALL_TIME, "Not Present"));

        assertEquals(List.of("Alpha", "Zulu"),
                presenter.output.getFighters().stream()
                        .map(BrowseFightersOutputData.FighterSummary::getName)
                        .toList());
        assertEquals("Alpha",
                presenter.output.getSelectedFighter().getName());
    }

    @Test
    void noMatchesProducesAnEmptySuccessfulResult() {
        final CapturingPresenter presenter = new CapturingPresenter();

        new BrowseFightersInteractor(
                () -> List.of(fighter("Alpha", WeightClass.LIGHTWEIGHT,
                        UfcEra.MODERN, 1)), presenter)
                .execute(new BrowseFightersInputData(
                        "missing", null, null, null));

        assertEquals(0, presenter.output.getFighters().size());
        assertNull(presenter.output.getSelectedFighter());
    }

    @Test
    void nullInputFailsWithoutAccessingTheCatalogue() {
        final CapturingPresenter presenter = new CapturingPresenter();

        new BrowseFightersInteractor(() -> {
            throw new AssertionError("Catalogue should not be accessed");
        }, presenter).execute(null);

        assertEquals("Browser filters are unavailable.", presenter.error);
        assertNull(presenter.output);
    }

    @Test
    void catalogueExceptionIsPresentedAsFailure() {
        final CapturingPresenter presenter = new CapturingPresenter();

        new BrowseFightersInteractor(() -> {
            throw new IllegalStateException("offline");
        }, presenter).execute(new BrowseFightersInputData(
                "", null, UfcEra.ALL_TIME, null));

        assertEquals("Unable to load the fighter catalogue.", presenter.error);
    }

    @Test
    void nullCatalogueIsPresentedAsFailure() {
        final CapturingPresenter presenter = new CapturingPresenter();

        new BrowseFightersInteractor(() -> null, presenter)
                .execute(new BrowseFightersInputData(
                        "", null, UfcEra.ALL_TIME, null));

        assertEquals("Unable to load the fighter catalogue.", presenter.error);
    }

    private static RealFighter fighter(String name,
                                       WeightClass weightClass,
                                       UfcEra era,
                                       int rank) {
        final Map<Attribute, Double> attributes =
                new EnumMap<>(Attribute.class);
        for (Attribute attribute : Attribute.values()) {
            attributes.put(attribute, 75.0);
        }
        return new RealFighter(
                name, weightClass, rank, era, "10-0", attributes);
    }

    private static final class CapturingPresenter
            implements BrowseFightersOutputBoundary {
        private BrowseFightersOutputData output;
        private String error;

        @Override
        public void prepareSuccessView(BrowseFightersOutputData outputData) {
            output = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }
    }
}
