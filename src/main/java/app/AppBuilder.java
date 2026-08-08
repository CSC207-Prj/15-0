package app;

import view.FighterCreationView;

import javax.swing.JFrame;

/**
 * Builds and connects the application views and their dependencies.
 */
public class AppBuilder {

    private FighterCreationView fighterCreationView;

    public AppBuilder addFighterCreationView() {
        final Runnable backAction = new Runnable() {
            @Override
            public void run() {
                System.out.println("Back to Settings");
            }
        };

        final Runnable continueAction = new Runnable() {
            @Override
            public void run() {
                System.out.println("Continue");
            }
        };

        fighterCreationView =
                new FighterCreationView(backAction, continueAction);

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