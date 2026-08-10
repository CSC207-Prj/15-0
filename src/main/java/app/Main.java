package app;

import data_access.InMemorySimulationDataAccessObject;
import data_access.JavaRandomSource;
import data_access.JsonFighterRosterDataAccess;

import interface_adapter.ViewManagerModel;
import view.*;
import view.ConfirmView;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Dimension;
import data_access.InMemoryFighterDataAccessObject;
import interface_adapter.fighter_creation.FighterCreationViewModel;
import interface_adapter.game_setting.GameSettingViewModel;
import use_case.fighter_creation.FighterDataAccessInterface;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::showApplication);
    }

    private static void showApplication() {
        configureSwingDefaults();
        final ViewManagerModel navigation = new ViewManagerModel();
        final ViewManager viewManager = new ViewManager(navigation);

        final SplashView splash = new SplashView(
                () -> navigation.setActiveView(ViewNames.WELCOME));
        final WelcomeView welcome = new WelcomeView(
                () -> navigation.setActiveView(ViewNames.SETTINGS),
                () -> navigation.setActiveView(ViewNames.SAVED_FIGHTERS),
                () -> navigation.setActiveView(ViewNames.FIGHTER_BROWSER),
                () -> System.exit(0));
        final FighterDataAccessInterface fighterDataAccess =
                new InMemoryFighterDataAccessObject();

        final GameSettingViewModel gameSettingViewModel =
                new GameSettingViewModel();

        final FighterCreationViewModel fighterCreationViewModel =
                new FighterCreationViewModel();

        final FighterCreationView fighterCreation = FighterCreationUseCaseFactory.create(
                fighterDataAccess,
                new JavaRandomSource(),
                fighterCreationViewModel,
                () -> navigation.setActiveView(ViewNames.SETTINGS),
                () -> navigation.setActiveView(ViewNames.CHARACTER_OVERVIEW));

        final GameSettingsView settings = GameSettingUseCaseFactory.create(
                fighterDataAccess,
                gameSettingViewModel,
                () -> navigation.setActiveView(ViewNames.WELCOME),
                () -> {
                    if (gameSettingViewModel.getState().isConfigured()) {
                        fighterCreationViewModel.initializeRun(
                                gameSettingViewModel.getState().getSettings(),
                                gameSettingViewModel.getState().getCustomFighter());

                        navigation.setActiveView(ViewNames.FIGHTER_CREATION);
                    }
                });

        final ConfirmView overview = ConfirmUseCaseFactory.create(
                () -> navigation.setActiveView(ViewNames.FIGHTER_CREATION),
                () -> navigation.setActiveView(ViewNames.SIMULATION));

        final InMemorySimulationDataAccessObject simulationDataAccess =
                new InMemorySimulationDataAccessObject();
        final SimulationView simulation = SimulationUseCaseFactory.create(
                simulationDataAccess,
                new JavaRandomSource(),
                () -> navigation.setActiveView(ViewNames.WELCOME),
                () -> navigation.setActiveView(ViewNames.SAVED_FIGHTERS));

        final JsonFighterRosterDataAccess fighterRoster =
                new JsonFighterRosterDataAccess("saved_fighters.json");
        final SavedFightersView savedFighters = SavedFightersUseCaseFactory.create(
                fighterRoster, fighterRoster, fighterRoster, fighterRoster,
                new JavaRandomSource(),
                () -> navigation.setActiveView(ViewNames.WELCOME));
        final FighterBrowserView fighterBrowser = new FighterBrowserView(
                () -> navigation.setActiveView(ViewNames.WELCOME));

        viewManager.addView(ViewNames.SPLASH, splash);
        viewManager.addView(ViewNames.WELCOME, welcome);
        viewManager.addView(ViewNames.SETTINGS, settings);
        viewManager.addView(ViewNames.FIGHTER_CREATION, fighterCreation);
        viewManager.addView(ViewNames.CHARACTER_OVERVIEW, overview);
        viewManager.addView(ViewNames.SIMULATION, simulation);
        viewManager.addView(ViewNames.SAVED_FIGHTERS, savedFighters);
        viewManager.addView(ViewNames.FIGHTER_BROWSER, fighterBrowser);

        final JFrame frame = new JFrame("15-0: Build-A-Fighter Gauntlet — Stage 2 Preview");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(viewManager);
        frame.setMinimumSize(new Dimension(1200, 760));
        frame.setSize(1500, 900);
        frame.setLocationRelativeTo(null);
        navigation.setActiveView(ViewNames.SPLASH);
        frame.setVisible(true);
    }

    private static void configureSwingDefaults() {
        UIManager.put("Panel.background", UfcTheme.PANEL);
        UIManager.put("Label.foreground", UfcTheme.TEXT);
        UIManager.put("Button.foreground", UfcTheme.TEXT);
        UIManager.put("ComboBox.foreground", UfcTheme.TEXT);
        UIManager.put("ComboBox.background", UfcTheme.PANEL_ALT);
        UIManager.put("TextField.foreground", UfcTheme.TEXT);
        UIManager.put("TextField.background", UfcTheme.PANEL_ALT);
        UIManager.put("CheckBox.foreground", UfcTheme.TEXT);
        UIManager.put("CheckBox.background", UfcTheme.PANEL);
        UIManager.put("Spinner.foreground", UfcTheme.TEXT);
        UIManager.put("Spinner.background", UfcTheme.PANEL_ALT);
    }
}
