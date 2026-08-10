package view;

import entity.Attribute;
import entity.Difficulty;
import entity.UfcEra;
import entity.WeightClass;
import interface_adapter.ViewManagerModel;
import interface_adapter.fighter_browser.FighterBrowserController;
import interface_adapter.fighter_browser.FighterBrowserRow;
import interface_adapter.fighter_browser.FighterBrowserState;
import interface_adapter.fighter_browser.FighterBrowserViewModel;
import interface_adapter.game_setting.GameSettingController;
import interface_adapter.simulation.SimulationController;
import interface_adapter.simulation.SimulationState;
import interface_adapter.simulation.SimulationViewModel;
import org.junit.jupiter.api.Test;
import use_case.game_setting.GameSettingInputData;
import use_case.browse_fighters.BrowseFightersInputData;
import use_case.simulation.SimulationInputData;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ViewBehaviorTest {

    static {
        // Swing component tests do not open windows. Headless mode also keeps
        // macOS AppKit from requiring the Maven test JVM to own the UI thread.
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    void themeFactoriesCreateConsistentlyConfiguredComponents() throws Exception {
        onEdt(() -> {
            final JPanel panel = UfcTheme.panel(new BorderLayout());
            assertSame(UfcTheme.PANEL, panel.getBackground());
            assertSame(UfcTheme.TEXT, panel.getForeground());

            assertEquals("Title", UfcTheme.title("Title").getText());
            assertEquals(UfcTheme.TITLE, UfcTheme.title("Title").getFont());
            assertEquals(UfcTheme.SECTION,
                    UfcTheme.section("Section").getFont());
            assertEquals(UfcTheme.BODY, UfcTheme.body("Body").getFont());

            final JButton primary = UfcTheme.primaryButton("Primary");
            final JButton secondary = UfcTheme.secondaryButton("Secondary");
            final JButton danger = UfcTheme.dangerButton("Danger");
            assertEquals(UfcTheme.ACCENT, primary.getBackground());
            assertEquals(UfcTheme.PANEL_ALT, secondary.getBackground());
            assertEquals(UfcTheme.ACCENT_DARK, danger.getBackground());
            assertFalse(primary.isBorderPainted());

            final JTextField field = UfcTheme.textField(12);
            assertEquals(12, field.getColumns());
            assertEquals(UfcTheme.PANEL_ALT, field.getBackground());

            final JComboBox<String> combo =
                    UfcTheme.comboBox(new String[]{"One", "Two"});
            assertEquals(2, combo.getItemCount());
            final Component rendered = combo.getRenderer()
                    .getListCellRendererComponent(
                            new JList<>(), "One", 0, true, false);
            assertTrue(rendered instanceof JLabel);
            assertEquals(UfcTheme.ACCENT, rendered.getBackground());
            final Component unselected = combo.getRenderer()
                    .getListCellRendererComponent(
                            new JList<>(), "Two", 1, false, false);
            assertEquals(UfcTheme.PANEL_ALT, unselected.getBackground());

            assertNotNull(UfcTheme.cardBorder());
            final JProgressBar bar = UfcTheme.statBar(75.6);
            assertEquals(76, bar.getValue());
            final JScrollPane scroll = UfcTheme.scroll(new JPanel());
            assertEquals(16, scroll.getVerticalScrollBar().getUnitIncrement());
            final JLabel centered = UfcTheme.centeredLabel(
                    "Centered", UfcTheme.HERO, UfcTheme.SUCCESS);
            assertEquals(JLabel.CENTER, centered.getHorizontalAlignment());
        });
    }

    @Test
    void splashAndWelcomeButtonsInvokeNavigationActions() throws Exception {
        onEdt(() -> {
            final int[] splashCount = {0};
            final SplashView splash = new SplashView(() -> splashCount[0]++);
            button(splash, "Enter the Octagon").doClick();
            assertEquals(1, splashCount[0]);

            final List<String> actions = new ArrayList<>();
            final WelcomeView welcome = new WelcomeView(
                    () -> actions.add("new"),
                    () -> actions.add("saved"),
                    () -> actions.add("browse"),
                    () -> actions.add("exit"));
            button(welcome, "Start New Run").doClick();
            button(welcome, "Browse UFC Fighters").doClick();
            button(welcome, "Saved Fighters").doClick();
            button(welcome, "Exit").doClick();
            assertEquals(List.of("new", "browse", "saved", "exit"), actions);
        });
    }

    @Test
    void settingsViewSubmitsSelectionsAndNavigation() throws Exception {
        onEdt(() -> {
            final AtomicReference<GameSettingInputData> input =
                    new AtomicReference<>();
            final int[] back = {0};
            final int[] continued = {0};
            final GameSettingsView view = new GameSettingsView(
                    new GameSettingController(input::set),
                    () -> back[0]++, () -> continued[0]++);

            for (JComboBox<?> combo : components(view, JComboBox.class)) {
                if (combo.getItemAt(0) instanceof Difficulty) {
                    combo.setSelectedItem(Difficulty.HARD);
                }
                else if (combo.getItemAt(0) instanceof Integer) {
                    combo.setSelectedItem(5);
                }
                else if (combo.getItemAt(0) instanceof UfcEra) {
                    combo.setSelectedItem(UfcEra.MODERN);
                }
            }
            components(view, JCheckBox.class).get(0).setSelected(true);

            button(view, "Create Fighter").doClick();
            assertEquals(Difficulty.HARD, input.get().getDifficulty());
            assertEquals(5, input.get().getRoundsPerFight());
            assertEquals(UfcEra.MODERN, input.get().getEra());
            assertTrue(input.get().isHideOpponentStats());
            assertEquals(1, continued[0]);

            button(view, "Back").doClick();
            assertEquals(1, back[0]);
        });
    }

    @Test
    void simulationViewRendersStateAndForwardsActions() throws Exception {
        onEdt(() -> {
            final List<SimulationInputData.Action> actions = new ArrayList<>();
            final SimulationController controller =
                    new SimulationController(input -> actions.add(input.getAction()));
            final SimulationViewModel viewModel = new SimulationViewModel();
            final int[] home = {0};
            final int[] saved = {0};
            final SimulationView view = new SimulationView(
                    controller, viewModel,
                    () -> home[0]++, () -> saved[0]++);
            assertEquals(List.of(SimulationInputData.Action.LOAD), actions);

            view.refreshRun();
            assertEquals(2, actions.size());

            viewModel.setState(new SimulationState(
                    "Lightweight", "RECORD  1-0", "A vs B",
                    "Striking 90 & Reach < 80",
                    List.of("#15 B • NEXT"),
                    List.of("#15 B • WIN • 1:00"),
                    true, "Ready"));
            assertTrue(labels(view).stream()
                    .anyMatch(label -> "A vs B".equals(label.getText())));
            assertTrue(labels(view).stream().anyMatch(
                    label -> label.getText().contains("&amp;")
                            && label.getText().contains("&lt;")));
            button(view, "Simulate Next Fight").doClick();
            button(view, "Auto Simulate Remaining").doClick();
            assertTrue(actions.contains(SimulationInputData.Action.SIMULATE_NEXT));
            assertTrue(actions.contains(SimulationInputData.Action.AUTO_SIMULATE));

            viewModel.setState(new SimulationState(
                    "Lightweight", "RECORD  15-0", "Complete", "",
                    List.of("#1 Champion • WIN"), List.of(),
                    false, "Done"));
            assertFalse(button(view, "Simulate Next Fight").isEnabled());
            assertTrue(button(view, "Save Fighter").isEnabled());
            button(view, "Save Fighter").doClick();
            button(view, "Home").doClick();
            assertEquals(1, saved[0]);
            assertEquals(1, home[0]);
        });
    }

    @Test
    void fighterBrowserViewLoadsRendersFiltersSelectsAndClears() throws Exception {
        onEdt(() -> {
            final List<BrowseFightersInputData> inputs = new ArrayList<>();
            final FighterBrowserController controller =
                    new FighterBrowserController(inputs::add);
            final FighterBrowserViewModel viewModel =
                    new FighterBrowserViewModel();
            final int[] back = {0};
            final FighterBrowserView view = new FighterBrowserView(
                    controller, viewModel, () -> back[0]++);
            assertEquals(1, inputs.size());
            assertEquals("", inputs.get(0).getSearchText());

            final FighterBrowserState state = new FighterBrowserState();
            state.setRows(List.of(
                    new FighterBrowserRow("Alpha", "Lightweight • Modern"),
                    new FighterBrowserRow("Bravo", "Welterweight • Zuffa")));
            state.setSelectedName("Alpha");
            state.setSelectedDetails("Lightweight • Modern • Record: 10-0");
            state.setRankText("Rank #1");
            state.setResultText("2 fighters found");
            state.setAttributes(java.util.Map.of(
                    Attribute.STRIKING, 91,
                    Attribute.CARDIO, 88));
            viewModel.setState(state);
            viewModel.firePropertyChanged();

            final JList<?> list = components(view, JList.class).stream()
                    .filter(candidate -> candidate.getModel().getSize() == 2)
                    .findFirst().orElseThrow();
            assertEquals(0, list.getSelectedIndex());
            list.setSelectedIndex(1);
            assertEquals("Bravo",
                    inputs.get(inputs.size() - 1).getSelectedFighterName());

            final JTextField search = components(view, JTextField.class).get(0);
            search.setText("alpha");
            for (JComboBox<?> combo : components(view, JComboBox.class)) {
                if ("All Divisions".equals(combo.getItemAt(0))) {
                    combo.setSelectedItem(
                            WeightClass.LIGHTWEIGHT.getDisplayName());
                }
                else if ("All Eras".equals(combo.getItemAt(0))) {
                    combo.setSelectedItem(UfcEra.MODERN.getDisplayName());
                }
            }
            button(view, "Apply Filters").doClick();
            final BrowseFightersInputData filtered =
                    inputs.get(inputs.size() - 1);
            assertEquals("alpha", filtered.getSearchText());
            assertEquals(WeightClass.LIGHTWEIGHT, filtered.getWeightClass());
            assertEquals(UfcEra.MODERN, filtered.getEra());

            search.postActionEvent();
            button(view, "Clear").doClick();
            final BrowseFightersInputData cleared =
                    inputs.get(inputs.size() - 1);
            assertEquals("", cleared.getSearchText());
            assertEquals(UfcEra.ALL_TIME, cleared.getEra());
            button(view, "Back to Home").doClick();
            assertEquals(1, back[0]);
        });
    }

    @Test
    void viewManagerRespondsToNavigationChanges() throws Exception {
        onEdt(() -> {
            final ViewManagerModel model = new ViewManagerModel();
            final ViewManager manager = new ViewManager(model);
            final JPanel first = new JPanel();
            final JPanel second = new JPanel();
            manager.addView("first", first);
            manager.addView("second", second);
            model.setActiveView("second");
            assertEquals("second", model.getActiveView());
            assertEquals(2, manager.getComponentCount());
            manager.propertyChange(new java.beans.PropertyChangeEvent(
                    model, "state", null, 123));
        });
    }

    private static JButton button(Container root, String text) {
        return components(root, JButton.class).stream()
                .filter(candidate -> text.equals(candidate.getText()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Button not found: " + text));
    }

    private static List<JLabel> labels(Container root) {
        return components(root, JLabel.class);
    }

    private static <T extends Component> List<T> components(
            Container root, Class<T> type) {
        final List<T> matches = new ArrayList<>();
        for (Component component : root.getComponents()) {
            if (type.isInstance(component)) {
                matches.add(type.cast(component));
            }
            if (component instanceof Container child) {
                matches.addAll(components(child, type));
            }
        }
        return matches;
    }

    private static void onEdt(ThrowingRunnable action) throws Exception {
        final AtomicReference<Throwable> failure = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            try {
                action.run();
            }
            catch (Throwable throwable) {
                failure.set(throwable);
            }
        });
        if (failure.get() instanceof Exception exception) {
            throw exception;
        }
        if (failure.get() instanceof Error error) {
            throw error;
        }
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}
