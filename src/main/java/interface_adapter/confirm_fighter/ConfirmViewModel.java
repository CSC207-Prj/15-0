package interface_adapter.confirm_fighter;

import interface_adapter.ViewModel;

/**
 * View model for the Confirm Fighter screen.
 */
public class ConfirmViewModel extends ViewModel {
    public static final String VIEW_NAME = "Confirm Fighter";
    public static final String TITLE_LABEL = "Finalize Your Fighter";
    public static final String FIGHTER_NAME_LABEL = "Fighter Name: ";
    public static final String WEIGHT_CLASS_LABEL = "Weight Class: ";
    public static final String OVERALL_LABEL = "Overall: ";
    public static final String SPIN_BUTTON_LABEL =
            "Spin Fighter Weight Class";
    public static final String CONFIRM_BUTTON_LABEL =
            "Confirm Fighter";

    private ConfirmState state = new ConfirmState();

    /**
     * Creates a view model for the confirm-fighter screen.
     */
    public ConfirmViewModel() {
        super(VIEW_NAME);
    }

    /**
     * Gets the current confirm-screen state.
     *
     * @return the current state
     */
    public ConfirmState getState() {
        return state;
    }

    /**
     * Replaces the current confirm-screen state.
     *
     * @param state the new state, or null to restore the default state
     */
    public void setState(ConfirmState state) {
        if (state == null) {
            this.state = new ConfirmState();
        }
        else {
            this.state = state;
        }
    }

    /**
     * Notifies listeners that the confirm-screen state has changed.
     */
    public void firePropertyChanged() {
        firePropertyChanged("state", state);
    }
}
