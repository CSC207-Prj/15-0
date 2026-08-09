package interface_adapter;

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

    public ConfirmViewModel() {
        super(VIEW_NAME);
    }

    public ConfirmState getState() {
        return state;
    }

    public void setState(ConfirmState state) {
        if (state == null) {
            this.state = new ConfirmState();
        }
        else {
            this.state = state;
        }
    }

    public void firePropertyChanged() {
        firePropertyChanged("state", state);
    }
}
