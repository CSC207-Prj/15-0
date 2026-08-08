package view;

import interface_adapter.ViewManagerModel;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/** Minimal CardLayout infrastructure. Finished screens are added in Stage 2. */
public final class ViewManager extends JPanel implements PropertyChangeListener {
    private final CardLayout cardLayout = new CardLayout();

    public ViewManager(ViewManagerModel model) {
        setLayout(cardLayout);
        model.addPropertyChangeListener(this);
    }

    public void addView(String name, JComponent view) {
        add(view, name);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getNewValue() instanceof String viewName) {
            cardLayout.show(this, viewName);
        }
    }
}
