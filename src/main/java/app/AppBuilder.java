package app;

import interface_adapter.fighter_creation.FighterCreationViewModel;
import view.FighterCreationView;

import javax.swing.JFrame;

/**
 * Builds and connects the application views and their dependencies.
 */
public class AppBuilder {

    private FighterCreationView fighterCreationView;

    public AppBuilder addFighterCreationView() {
        FighterCreationViewModel viewModel =
                new FighterCreationViewModel();

        fighterCreationView =
                new FighterCreationView(viewModel);

        return this;
    }

    public JFrame build() {
        JFrame application = new JFrame("UFC Build-A-Fighter");

        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setContentPane(fighterCreationView);
        application.setExtendedState(JFrame.MAXIMIZED_BOTH);

        return application;
    }
}