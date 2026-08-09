package interface_adapter.saved_fighters;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import use_case.exhibition.ExhibitionOutputData;

public class ExhibitionPresenterTest {

    @Test
    public void formatsFinishWithRoundAndTime() {
        final SavedFightersViewModel viewModel = new SavedFightersViewModel();

        new ExhibitionPresenter(viewModel).prepareSuccessView(
                new ExhibitionOutputData("Alpha", "Bravo", "KO/TKO", 2, 97, false));

        assertEquals("Alpha def. Bravo by KO/TKO in round 2 (1:37)",
                viewModel.getState().getExhibitionResult());
        assertEquals("", viewModel.getState().getError());
    }

    @Test
    public void formatsDecisionWithoutRoundAndTime() {
        final SavedFightersViewModel viewModel = new SavedFightersViewModel();

        new ExhibitionPresenter(viewModel).prepareSuccessView(
                new ExhibitionOutputData("Bravo", "Alpha", "Decision", 3, 300, false));

        assertEquals("Bravo def. Alpha by Decision",
                viewModel.getState().getExhibitionResult());
    }

    @Test
    public void failClearsResultAndSetsError() {
        final SavedFightersViewModel viewModel = new SavedFightersViewModel();
        viewModel.getState().setExhibitionResult("stale");

        new ExhibitionPresenter(viewModel).prepareFailView("Choose two different fighters.");

        assertEquals("", viewModel.getState().getExhibitionResult());
        assertEquals("Choose two different fighters.", viewModel.getState().getError());
    }
}
