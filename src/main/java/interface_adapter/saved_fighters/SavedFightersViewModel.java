package interface_adapter.saved_fighters;

import interface_adapter.ViewModel;

/**
 * View model for the Saved Fighters screen. The view name matches
 * ViewNames.SAVED_FIGHTERS so the navigation shell can switch to it.
 */
public class SavedFightersViewModel extends ViewModel {
    public static final String VIEW_NAME = "saved fighters";

    public static final String TITLE_LABEL = "SAVED FIGHTERS";
    public static final String SUBTITLE_LABEL = "Roster, rankings, and exhibition fights";
    public static final String ROSTER_LABEL = "YOUR ROSTER";
    public static final String TOP_THREE_LABEL = "TOP 3 FIGHTERS";
    public static final String EXHIBITION_LABEL = "EXHIBITION MATCH";
    public static final String LOAD_BUTTON_LABEL = "Load Selected";
    public static final String DELETE_BUTTON_LABEL = "Delete Selected";
    public static final String EXHIBITION_BUTTON_LABEL = "Run Exhibition";
    public static final String BACK_BUTTON_LABEL = "Back to Home";
    public static final String EMPTY_ROSTER_MESSAGE = "No saved fighters yet.";

    private SavedFightersState state = new SavedFightersState();

    public SavedFightersViewModel() {
        super(VIEW_NAME);
    }

    public SavedFightersState getState() {
        return state;
    }

    public void setState(SavedFightersState state) {
        if (state == null) {
            this.state = new SavedFightersState();
        }
        else {
            this.state = state;
        }
    }

    /**
     * Notifies the view that the state has changed.
     */
    public void firePropertyChanged() {
        firePropertyChanged(null, state);
    }
}
