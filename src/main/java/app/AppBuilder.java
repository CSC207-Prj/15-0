package app;

import view.FighterCreationView;

import javax.swing.JFrame;

/**
 * Small frame builder retained for compatibility with earlier project stages.
 *
 * Main is currently the application's composition root.
 */
public class AppBuilder {

    private FighterCreationView fighterCreationView;

    public AppBuilder addFighterCreationView(
            FighterCreationView fighterCreationView) {
        this.fighterCreationView = fighterCreationView;
        return this;
    }

    public JFrame build() {
        if (fighterCreationView == null) {
            throw new IllegalStateException(
                    "FighterCreationView must be supplied before build()."
            );
        }

        final JFrame application =
                new JFrame("UFC Build-A-Fighter");

        application.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );
        application.setContentPane(
                fighterCreationView
        );
        application.setExtendedState(
                JFrame.MAXIMIZED_BOTH
        );

        return application;
    }
}
