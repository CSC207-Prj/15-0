package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

/** Shared observable model base used by Swing-facing state in later stages. */
public abstract class ViewModel {
    private final String viewName;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    protected ViewModel(String viewName) {
        this.viewName = Objects.requireNonNull(viewName, "viewName");
    }

    public String getViewName() {
        return viewName;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    protected void firePropertyChanged(Object oldValue, Object newValue) {
        support.firePropertyChange("state", oldValue, newValue);
    }
}
