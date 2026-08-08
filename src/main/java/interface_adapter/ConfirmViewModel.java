package interface_adapter;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ConfirmViewModel extends Viewmodel{
    public static final String VIEW_NAME = "Confirm Fighter";
    public static final String TITLE_LABEL = "Finalize Your Fighter";
    public static final String FIGHTER_NAME_LABEL = "FIghter Name: ";
    public static final String WEIGHT_CLASS_LABEL = "Weight Class: ";
    public static final String OVERALL_LABEL  = "Overall: ";
    public static final String SPIN_BUTTON_LABEL = "Spin Fighter Weight Class";
    public static final String CONFIRM_BUTTON_LABEL = "Confirm Fighter";

    private final PropertyChangeSupport support =
            new PropertyChangeSupport(this);
    private ConfirmState state = new ConfirmState();

    public ConfirmState getState() {
        return state;
    }

    public void setState(ConfirmState state) {
        final ConfirmState oldState = this.state;
        if (state == null) {
            this.state = new ConfirmState();
        } else {
            this.state = state;
        }
        support.firePropertyChange("state", oldState, this.state);
    }

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}

