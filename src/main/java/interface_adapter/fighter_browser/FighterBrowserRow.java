package interface_adapter.fighter_browser;

/**
 * Display row for one fighter in the browser catalogue.
 */
public class FighterBrowserRow {
    private final String name;
    private final String details;

    public FighterBrowserRow(String name, String details) {
        this.name = name;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return name + " — " + details;
    }
}
