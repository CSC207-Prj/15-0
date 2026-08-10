package interface_adapter.fighter_browser;

import interface_adapter.ViewModel;

/**
 * Observable view model for User Story 6.
 */
public class FighterBrowserViewModel extends ViewModel {
    public static final String VIEW_NAME = "fighter browser";

    private FighterBrowserState state = new FighterBrowserState();

    public FighterBrowserViewModel() {
        super(VIEW_NAME);
    }

    public FighterBrowserState getState() {
        return state;
    }

    public void setState(FighterBrowserState state) {
        this.state = state == null ? new FighterBrowserState() : state;
    }

    public void firePropertyChanged() {
        firePropertyChanged(null, state);
    }
}
