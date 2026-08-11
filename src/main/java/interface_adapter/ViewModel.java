package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

/**
 * Provides shared Observer-pattern support for interface-adapter view models.
 *
 * Concrete view models hold display state while Swing views register
 * PropertyChangeListener instances and react to state-change events.
 */
public abstract class ViewModel {

    /** Property name used for ViewModel state-change notifications. */
    public static final String STATE_PROPERTY = "state";

    private final String viewName;

    private final PropertyChangeSupport support =
            new PropertyChangeSupport(this);

    /**
     * Creates an observable view model with a navigation name.
     *
     * @param viewName name used to identify the corresponding view
     * @throws NullPointerException if viewName is null
     */
    protected ViewModel(String viewName) {
        this.viewName =
                Objects.requireNonNull(
                        viewName,
                        "viewName");
    }

    /**
     * Returns the navigation name associated with this view model.
     *
     * @return view name
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Registers an observer for view-model state changes.
     *
     * @param listener observer to notify when state changes
     */
    public void addPropertyChangeListener(
            PropertyChangeListener listener) {

        support.addPropertyChangeListener(listener);
    }

    /**
     * Notifies registered observers that the state property changed.
     *
     * @param oldValue previous state value
     * @param newValue replacement state value
     */
    protected void firePropertyChanged(
            Object oldValue,
            Object newValue) {

        support.firePropertyChange(
                STATE_PROPERTY,
                oldValue,
                newValue);
    }
}