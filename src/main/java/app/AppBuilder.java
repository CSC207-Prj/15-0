package app;

import javax.swing.JFrame;

import view.FighterCreationView;

/**
 * Builds the application frame.
 */
public class AppBuilder {

    private FighterCreationView fighterCreationView;

    /**
     * Adds the fighter creation view to the application builder.
     *
     * @param creationView fighter creation view to display in the application
     * @return this builder for continued configuration
     */
    public AppBuilder addFighterCreationView(
            FighterCreationView creationView) {
        this.fighterCreationView = creationView;
        return this;
    }

    /**
     * Builds and configures the main application frame.
     *
     * @return configured application frame
     */
    public JFrame build() {
        final JFrame application =
                new JFrame("UFC Build-A-Fighter");

        application.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE);
        application.setContentPane(fighterCreationView);
        application.setExtendedState(
                JFrame.MAXIMIZED_BOTH);

        return application;
    }
}
