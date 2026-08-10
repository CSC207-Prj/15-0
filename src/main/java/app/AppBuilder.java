package app;

import view.FighterCreationView;

import javax.swing.JFrame;

/**
 * Builds the application frame.
 */
public class AppBuilder {

    private FighterCreationView fighterCreationView;

    public AppBuilder addFighterCreationView(
            FighterCreationView fighterCreationView) {
        this.fighterCreationView = fighterCreationView;
        return this;
    }

    public JFrame build() {
        final JFrame application =
                new JFrame("UFC Build-A-Fighter");

        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setContentPane(fighterCreationView);
        application.setExtendedState(JFrame.MAXIMIZED_BOTH);

        return application;
    }
}