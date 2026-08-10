package app;

import data_access.InMemorySimulationDataAccessObject;
import data_access.CitoApiClient;
import data_access.CitoConfig;
import data_access.CitoUfcDataAccess;
import data_access.JavaRandomSource;
import data_access.JsonFighterRosterDataAccess;
import data_access.FighterBrowserDataAccessAdapter;

import interface_adapter.ViewManagerModel;
import view.*;
import view.ConfirmView;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Dimension;
import data_access.InMemoryFighterDataAccessObject;
import entity.Attribute;
import entity.CustomFighter;
import entity.Division;
import entity.GameRun;
import entity.GameSettings;
import entity.WeightClass;
import interface_adapter.confirm_fighter.ConfirmState;
import interface_adapter.confirm_fighter.ConfirmViewModel;
import interface_adapter.fighter_creation.FighterCreationViewModel;
import interface_adapter.game_setting.GameSettingViewModel;
import interface_adapter.saved_fighters.SaveFighterController;
import interface_adapter.saved_fighters.SaveFighterPresenter;
import interface_adapter.saved_fighters.SavedFightersViewModel;
import use_case.fighter_creation.FighterDataAccessInterface;
import use_case.confirm.ConfirmInputBoundary;
import use_case.confirm.ConfirmInteractor;
import use_case.confirm.ConfirmOutputBoundary;
import use_case.confirm.ConfirmRunDataAccessInterface;
import use_case.save_fighter.SaveFighterInteractor;
import interface_adapter.confirm_fighter.ConfirmController;
import interface_adapter.confirm_fighter.ConfirmPresenter;

import java.util.ArrayList;
import java.util.List;

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

        final CitoUfcDataAccess citoUfcDataAccess =
                new CitoUfcDataAccess(
                        new CitoApiClient(CitoConfig.load()),
                        new InMemoryFighterDataAccessObject());

        final FighterDataAccessInterface fighterDataAccess =
                citoUfcDataAccess;

        final GameSettingViewModel gameSettingViewModel =
                new GameSettingViewModel();

        final FighterCreationViewModel fighterCreationViewModel =
                new FighterCreationViewModel();
        final ConfirmViewModel confirmViewModel = new ConfirmViewModel();

        final FighterCreationView fighterCreation = FighterCreationUseCaseFactory.create(
                fighterDataAccess,
                new JavaRandomSource(),
                fighterCreationViewModel,
                () -> navigation.setActiveView(ViewNames.SETTINGS),
                () -> {
                    loadConfirmDraft(
                            fighterCreationViewModel.getCustomFighter(),
                            confirmViewModel);
                    navigation.setActiveView(ViewNames.CHARACTER_OVERVIEW);
                });

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

        final JsonFighterRosterDataAccess fighterRoster =
                new JsonFighterRosterDataAccess("saved_fighters.json");
        final SavedFightersViewModel savedFightersViewModel =
                new SavedFightersViewModel();
        final SaveFighterController saveFighterController =
                new SaveFighterController(new SaveFighterInteractor(
                        fighterRoster,
                        new SaveFighterPresenter(savedFightersViewModel)));

        final InMemorySimulationDataAccessObject simulationDataAccess =
                new InMemorySimulationDataAccessObject();
        final SimulationView simulation = SimulationUseCaseFactory.create(
                simulationDataAccess,
                new JavaRandomSource(),
                () -> navigation.setActiveView(ViewNames.WELCOME),
                () -> {
                    final GameRun completedRun = simulationDataAccess.getGameRun();
                    if (completedRun != null && completedRun.isComplete()) {
                        saveFighterController.execute(completedRun.getPlayer());
                        navigation.setActiveView(ViewNames.SAVED_FIGHTERS);
                    }
                });

        final ConfirmRunDataAccessInterface confirmRunDataAccess =
                new ConfirmRunDataAccessInterface() {
                    @Override
                    public CustomFighter getCustomFighter() {
                        return fighterCreationViewModel.getCustomFighter();
                    }

                    @Override
                    public GameSettings getGameSettings() {
                        return fighterCreationViewModel.getGameSettings();
                    }

                    @Override
                    public Division getDivision(WeightClass weightClass) {
                        return citoUfcDataAccess.getDivision(weightClass);
                    }

                    @Override
                    public void saveGameRun(GameRun gameRun) {
                        simulationDataAccess.saveGameRun(gameRun);
                    }
                };

        final ConfirmOutputBoundary confirmPresenter =
                new ConfirmPresenter(confirmViewModel);
        final ConfirmInputBoundary confirmInteractor =
                new ConfirmInteractor(confirmPresenter, confirmRunDataAccess);
        final ConfirmController confirmController =
                new ConfirmController(confirmInteractor);

        final ConfirmView overview = new ConfirmView(
                confirmController,
                confirmViewModel,
                () -> navigation.setActiveView(ViewNames.FIGHTER_CREATION),
                () -> {
                    simulation.refreshRun();
                    navigation.setActiveView(ViewNames.SIMULATION);
                });

        final SavedFightersView savedFighters = SavedFightersUseCaseFactory.create(
                fighterRoster, fighterRoster, fighterRoster, fighterRoster,
                new JavaRandomSource(),
                saveFighterController,
                savedFightersViewModel,
                () -> navigation.setActiveView(ViewNames.WELCOME));
        final FighterBrowserView fighterBrowser = FighterBrowserUseCaseFactory.create(
                new FighterBrowserDataAccessAdapter(fighterDataAccess),
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

    /**
     * Transfers the completed US2 draft into the existing US3 view model.
     */
    private static void loadConfirmDraft(CustomFighter customFighter,
                                         ConfirmViewModel confirmViewModel) {
        final List<String> attributePoints = new ArrayList<>();

        for (Attribute attribute : Attribute.values()) {
            if (customFighter != null && customFighter.hasAttribute(attribute)) {
                attributePoints.add(Integer.toString(
                        (int) Math.round(customFighter.getAttribute(attribute))));
            }
            else {
                attributePoints.add("TBD");
            }
        }

        final ConfirmState state = confirmViewModel.getState();
        state.setFighterName("");
        state.setAttributePoints(attributePoints);
        state.setWeightClass("TBD");
        state.setOverall("--");
        state.setWeightClassLocked(false);
        state.setConfirmed(false);
        state.setErrorMessage(null);
        confirmViewModel.firePropertyChanged();
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