package interface_adapter;

/** Minimal shared navigation state for the application's CardLayout. */
public final class ViewManagerModel extends ViewModel {
    private String activeView;

    public ViewManagerModel() {
        super("view manager");
    }

    public String getActiveView() {
        return activeView;
    }

    public void setActiveView(String activeView) {
        final String oldView = this.activeView;
        this.activeView = activeView;
        firePropertyChanged(oldView, activeView);
    }
}
