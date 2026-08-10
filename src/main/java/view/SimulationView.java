package view;

import interface_adapter.simulation.SimulationController;
import interface_adapter.simulation.SimulationState;
import interface_adapter.simulation.SimulationViewModel;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

/**
 * Swing view for User Story 4.
 *
 * The view reads display data only from SimulationViewModel and sends user
 * actions only to SimulationController.
 */
public final class SimulationView extends JPanel implements PropertyChangeListener {
    private final SimulationController controller;
    private final SimulationViewModel viewModel;

    private final JLabel divisionLabel = UfcTheme.body("");
    private final JLabel recordLabel = UfcTheme.centeredLabel(
            "RECORD  0-0",
            new Font(Font.SANS_SERIF, Font.BOLD, 28),
            UfcTheme.ACCENT);
    private final JLabel matchupLabel = UfcTheme.centeredLabel(
            "No active matchup",
            new Font(Font.SANS_SERIF, Font.BOLD, 24),
            UfcTheme.TEXT);
    private final JLabel opponentStatsLabel = UfcTheme.centeredLabel(
            "",
            UfcTheme.BODY,
            UfcTheme.MUTED);
    private final JLabel statusLabel = UfcTheme.body("");

    private final JPanel opponentRows = UfcTheme.panel(null);
    private final JPanel historyRows = UfcTheme.panel(null);

    private final JButton simulateOne = UfcTheme.primaryButton("Simulate Next Fight");
    private final JButton simulateAll = UfcTheme.secondaryButton("Auto Simulate Remaining");
    private final JButton saveFighter = UfcTheme.primaryButton("Save Fighter");

    public SimulationView(SimulationController controller,
                          SimulationViewModel viewModel,
                          Runnable backHomeAction,
                          Runnable savedFightersAction) {
        this.controller = Objects.requireNonNull(controller, "controller");
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        Objects.requireNonNull(backHomeAction, "backHomeAction");
        Objects.requireNonNull(savedFightersAction, "savedFightersAction");

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(UfcTheme.BACKGROUND);

        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
        add(createActions(backHomeAction, savedFightersAction), BorderLayout.SOUTH);

        render(viewModel.getState());
        controller.loadRun();
    }

    /**
     * Reloads the current GameRun after US3 creates it.
     */
    public void refreshRun() {
        controller.loadRun();
    }

    private JPanel createHeader() {
        final JPanel header = UfcTheme.panel(new BorderLayout());
        header.setBackground(UfcTheme.HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        final JPanel titleBlock = UfcTheme.panel(null);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.add(UfcTheme.title("THE GAUNTLET"));
        divisionLabel.setForeground(UfcTheme.MUTED);
        titleBlock.add(divisionLabel);

        header.add(titleBlock, BorderLayout.WEST);
        header.add(recordLabel, BorderLayout.EAST);
        return header;
    }

    private JPanel createContent() {
        final JPanel content = UfcTheme.panel(new GridLayout(1, 2, 20, 0));
        content.setBackground(UfcTheme.BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));
        content.add(createOpponentList());
        content.add(createFightPanel());
        return content;
    }

    private JScrollPane createOpponentList() {
        final JPanel container = UfcTheme.panel(new BorderLayout());
        container.setBorder(UfcTheme.cardBorder());
        container.add(UfcTheme.section("DIVISION TOP 15"), BorderLayout.NORTH);

        opponentRows.setLayout(new BoxLayout(opponentRows, BoxLayout.Y_AXIS));
        container.add(opponentRows, BorderLayout.CENTER);
        return UfcTheme.scroll(container);
    }

    private JPanel createFightPanel() {
        final JPanel panel = UfcTheme.panel(new BorderLayout());
        panel.setBorder(UfcTheme.cardBorder());

        final JPanel top = UfcTheme.panel(null);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(UfcTheme.section("CURRENT MATCHUP"));
        top.add(Box.createVerticalStrut(20));
        top.add(matchupLabel);
        top.add(Box.createVerticalStrut(8));
        top.add(opponentStatsLabel);
        panel.add(top, BorderLayout.NORTH);

        historyRows.setLayout(new BoxLayout(historyRows, BoxLayout.Y_AXIS));
        final JPanel history = UfcTheme.panel(new BorderLayout());
        history.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0));
        history.add(UfcTheme.section("FIGHT HISTORY"), BorderLayout.NORTH);
        history.add(UfcTheme.scroll(historyRows), BorderLayout.CENTER);
        panel.add(history, BorderLayout.CENTER);

        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.add(statusLabel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createActions(Runnable backHomeAction, Runnable savedFightersAction) {
        final JPanel actions = UfcTheme.panel(new FlowLayout(FlowLayout.CENTER, 14, 16));
        actions.setBackground(UfcTheme.HEADER);

        final JButton home = UfcTheme.secondaryButton("Home");

        simulateOne.addActionListener(event -> controller.simulateNextFight());
        simulateAll.addActionListener(event -> controller.autoSimulateRun());
        saveFighter.addActionListener(event -> savedFightersAction.run());
        home.addActionListener(event -> backHomeAction.run());

        actions.add(simulateOne);
        actions.add(simulateAll);
        actions.add(saveFighter);
        actions.add(home);
        return actions;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        render(viewModel.getState());
    }

    private void render(SimulationState state) {
        divisionLabel.setText(state.getDivisionText());
        recordLabel.setText(state.getRecordText());
        matchupLabel.setText(state.getMatchupText());
        opponentStatsLabel.setText(toHtml(state.getOpponentStatsText()));
        statusLabel.setText(state.getStatusMessage());

        simulateOne.setEnabled(state.isSimulationEnabled());
        simulateAll.setEnabled(state.isSimulationEnabled());
        saveFighter.setEnabled(
                !state.isSimulationEnabled() && !state.getOpponentRows().isEmpty());

        opponentRows.removeAll();
        for (String rowText : state.getOpponentRows()) {
            opponentRows.add(opponentRow(rowText));
        }

        historyRows.removeAll();
        if (state.getHistoryRows().isEmpty()) {
            historyRows.add(UfcTheme.body("No fights simulated yet."));
        }
        else {
            for (String rowText : state.getHistoryRows()) {
                historyRows.add(historyRow(rowText));
            }
        }

        opponentRows.revalidate();
        opponentRows.repaint();
        historyRows.revalidate();
        historyRows.repaint();
    }

    private JPanel opponentRow(String text) {
        final JPanel row = UfcTheme.panel(new BorderLayout());
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UfcTheme.BORDER),
                BorderFactory.createEmptyBorder(10, 6, 10, 6)));

        final JLabel label = UfcTheme.body(text);
        label.setForeground(UfcTheme.TEXT);
        row.add(label, BorderLayout.CENTER);
        return row;
    }

    private JPanel historyRow(String text) {
        final JPanel row = UfcTheme.panel(new BorderLayout());
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UfcTheme.BORDER),
                BorderFactory.createEmptyBorder(11, 6, 11, 6)));

        final JLabel label = UfcTheme.body(text);
        label.setForeground(UfcTheme.TEXT);
        row.add(label, BorderLayout.CENTER);
        return row;
    }

    private String toHtml(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }
        return "<html><div style='text-align:center; width:520px;'>"
                + text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                + "</div></html>";
    }
}
