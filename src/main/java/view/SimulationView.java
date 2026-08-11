package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import interface_adapter.simulation.SimulationController;
import interface_adapter.simulation.SimulationState;
import interface_adapter.simulation.SimulationViewModel;

/**
 * Renders the User Story 4 gauntlet and forwards user actions to its controller.
 *
 * The view reads only display-ready SimulationState from the
 * observable SimulationViewModel. It does not mutate GameRun
 * or invoke the fight simulator directly, which keeps Swing outside the
 * business-rule layers.
 */
public final class SimulationView
        extends JPanel
        implements PropertyChangeListener {

    private static final int HEADER_VERTICAL_PADDING = 20;
    private static final int HEADER_HORIZONTAL_PADDING = 30;
    private static final int CONTENT_VERTICAL_PADDING = 22;
    private static final int CONTENT_HORIZONTAL_PADDING = 28;
    private static final int MATCHUP_VERTICAL_GAP = 20;
    private static final int STATS_VERTICAL_GAP = 8;
    private static final int HISTORY_TOP_PADDING = 24;
    private static final int STATUS_TOP_PADDING = 10;
    private static final int OPPONENT_ROW_HEIGHT = 46;
    private static final int OPPONENT_ROW_VERTICAL_PADDING = 10;
    private static final int HISTORY_ROW_HEIGHT = 48;
    private static final int HISTORY_ROW_VERTICAL_PADDING = 11;
    private static final int ROW_HORIZONTAL_PADDING = 6;

    /** Controller receiving simulation actions from this view. */
    private final SimulationController controller;

    /** Observable source of display-ready simulation state. */
    private final SimulationViewModel viewModel;

    /** Label showing the active weight division. */
    private final JLabel divisionLabel =
            UfcTheme.body("");

    /** Label showing the player's current gauntlet record. */
    private final JLabel recordLabel =
            UfcTheme.centeredLabel(
                    "RECORD  0-0",
                    new Font(
                            Font.SANS_SERIF,
                            Font.BOLD,
                            28),
                    UfcTheme.ACCENT);

    /** Label showing the current player-versus-opponent matchup. */
    private final JLabel matchupLabel =
            UfcTheme.centeredLabel(
                    "No active matchup",
                    new Font(
                            Font.SANS_SERIF,
                            Font.BOLD,
                            24),
                    UfcTheme.TEXT);

    /** Label showing the current opponent statistics or hidden-stats message. */
    private final JLabel opponentStatsLabel =
            UfcTheme.centeredLabel(
                    "",
                    UfcTheme.BODY,
                    UfcTheme.MUTED);

    /** Label showing the most recent use-case status message. */
    private final JLabel statusLabel =
            UfcTheme.body("");

    /** Container holding the ranked-opponent status rows. */
    private final JPanel opponentRows =
            UfcTheme.panel(null);

    /** Container holding completed fight-history rows. */
    private final JPanel historyRows =
            UfcTheme.panel(null);

    /** Button that requests one additional fight. */
    private final JButton simulateOne =
            UfcTheme.primaryButton(
                    "Simulate Next Fight");

    /** Button that requests simulation of every remaining fight. */
    private final JButton simulateAll =
            UfcTheme.secondaryButton(
                    "Auto Simulate Remaining");

    /** Button that hands a completed fighter to the saved-fighter flow. */
    private final JButton saveFighter =
            UfcTheme.primaryButton(
                    "Save Fighter");

    /**
     * Creates and wires the Swing controls for the simulation screen.
     *
     * @param controller adapter receiving load and simulation actions
     * @param viewModel observable source of display-ready simulation state
     * @param backHomeAction callback for the Home button
     * @param savedFightersAction callback for the Save Fighter button
     * @throws NullPointerException if any supplied dependency or callback is null
     */
    public SimulationView(
            SimulationController controller,
            SimulationViewModel viewModel,
            Runnable backHomeAction,
            Runnable savedFightersAction) {

        this.controller =
                Objects.requireNonNull(
                        controller,
                        "controller");

        this.viewModel =
                Objects.requireNonNull(
                        viewModel,
                        "viewModel");

        Objects.requireNonNull(
                backHomeAction,
                "backHomeAction");

        Objects.requireNonNull(
                savedFightersAction,
                "savedFightersAction");

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(UfcTheme.BACKGROUND);

        add(
                createHeader(),
                BorderLayout.NORTH);

        add(
                createContent(),
                BorderLayout.CENTER);

        add(
                createActions(
                        backHomeAction,
                        savedFightersAction),
                BorderLayout.SOUTH);

        render(viewModel.getState());
        controller.loadRun();
    }

    /**
     * Reloads the active game run after the confirmation use case creates it.
     */
    public void refreshRun() {
        controller.loadRun();
    }

    /**
     * Builds the header containing division and player-record information.
     *
     * @return configured header panel
     */
    private JPanel createHeader() {
        final JPanel header =
                UfcTheme.panel(
                        new BorderLayout());

        header.setBackground(
                UfcTheme.HEADER);

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        HEADER_VERTICAL_PADDING,
                        HEADER_HORIZONTAL_PADDING,
                        HEADER_VERTICAL_PADDING,
                        HEADER_HORIZONTAL_PADDING));

        final JPanel titleBlock =
                UfcTheme.panel(null);

        titleBlock.setLayout(
                new BoxLayout(
                        titleBlock,
                        BoxLayout.Y_AXIS));

        titleBlock.add(
                UfcTheme.title(
                        "THE GAUNTLET"));

        divisionLabel.setForeground(
                UfcTheme.MUTED);

        titleBlock.add(
                divisionLabel);

        header.add(
                titleBlock,
                BorderLayout.WEST);

        header.add(
                recordLabel,
                BorderLayout.EAST);

        return header;
    }

    /**
     * Builds the main two-column simulation content area.
     *
     * @return configured content panel
     */
    private JPanel createContent() {
        final JPanel content =
                UfcTheme.panel(
                        new GridLayout(
                                1,
                                2,
                                20,
                                0));

        content.setBackground(
                UfcTheme.BACKGROUND);

        content.setBorder(
                BorderFactory.createEmptyBorder(
                        CONTENT_VERTICAL_PADDING,
                        CONTENT_HORIZONTAL_PADDING,
                        CONTENT_VERTICAL_PADDING,
                        CONTENT_HORIZONTAL_PADDING));

        content.add(
                createOpponentList());

        content.add(
                createFightPanel());

        return content;
    }

    /**
     * Builds the scrollable ranked-opponent list.
     *
     * @return scroll pane containing opponent rows
     */
    private JScrollPane createOpponentList() {
        final JPanel container =
                UfcTheme.panel(
                        new BorderLayout());

        container.setBorder(
                UfcTheme.cardBorder());

        container.add(
                UfcTheme.section(
                        "DIVISION TOP 15"),
                BorderLayout.NORTH);

        opponentRows.setLayout(
                new BoxLayout(
                        opponentRows,
                        BoxLayout.Y_AXIS));

        container.add(
                opponentRows,
                BorderLayout.CENTER);

        return UfcTheme.scroll(container);
    }

    /**
     * Builds the current-matchup and fight-history panel.
     *
     * @return configured fight panel
     */
    private JPanel createFightPanel() {
        final JPanel panel =
                UfcTheme.panel(
                        new BorderLayout());

        panel.setBorder(
                UfcTheme.cardBorder());

        final JPanel top =
                UfcTheme.panel(null);

        top.setLayout(
                new BoxLayout(
                        top,
                        BoxLayout.Y_AXIS));

        top.add(
                UfcTheme.section(
                        "CURRENT MATCHUP"));

        top.add(
                Box.createVerticalStrut(
                        MATCHUP_VERTICAL_GAP));

        top.add(matchupLabel);

        top.add(
                Box.createVerticalStrut(
                        STATS_VERTICAL_GAP));

        top.add(opponentStatsLabel);

        panel.add(
                top,
                BorderLayout.NORTH);

        historyRows.setLayout(
                new BoxLayout(
                        historyRows,
                        BoxLayout.Y_AXIS));

        final JPanel history =
                UfcTheme.panel(
                        new BorderLayout());

        history.setBorder(
                BorderFactory.createEmptyBorder(
                        HISTORY_TOP_PADDING,
                        0,
                        0,
                        0));

        history.add(
                UfcTheme.section(
                        "FIGHT HISTORY"),
                BorderLayout.NORTH);

        history.add(
                UfcTheme.scroll(historyRows),
                BorderLayout.CENTER);

        panel.add(
                history,
                BorderLayout.CENTER);

        statusLabel.setBorder(
                BorderFactory.createEmptyBorder(
                        STATUS_TOP_PADDING,
                        0,
                        0,
                        0));

        panel.add(
                statusLabel,
                BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Builds the action bar and connects buttons to controller
     * or navigation callbacks.
     *
     * @param backHomeAction callback for returning home
     * @param savedFightersAction callback for saving the completed fighter
     * @return configured action panel
     */
    private JPanel createActions(
            Runnable backHomeAction,
            Runnable savedFightersAction) {

        final JPanel actions =
                UfcTheme.panel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                14,
                                16));

        actions.setBackground(
                UfcTheme.HEADER);

        final JButton home =
                UfcTheme.secondaryButton(
                        "Home");

        simulateOne.addActionListener(
                event ->
                        controller.simulateNextFight());

        simulateAll.addActionListener(
                event ->
                        controller.autoSimulateRun());

        saveFighter.addActionListener(
                event ->
                        savedFightersAction.run());

        home.addActionListener(
                event ->
                        backHomeAction.run());

        actions.add(simulateOne);
        actions.add(simulateAll);
        actions.add(saveFighter);
        actions.add(home);

        return actions;
    }

    /**
     * Re-renders the screen when the observed view-model state changes.
     *
     * @param event property-change event emitted by the view model
     */
    @Override
    public void propertyChange(
            PropertyChangeEvent event) {

        render(viewModel.getState());
    }

    /**
     * Applies a display-ready state snapshot to all Swing controls.
     *
     * @param state state produced by the simulation presenter
     */
    private void render(
            SimulationState state) {

        divisionLabel.setText(
                state.getDivisionText());

        recordLabel.setText(
                state.getRecordText());

        matchupLabel.setText(
                state.getMatchupText());

        opponentStatsLabel.setText(
                toHtml(
                        state.getOpponentStatsText()));

        statusLabel.setText(
                state.getStatusMessage());

        simulateOne.setEnabled(
                state.isSimulationEnabled());

        simulateAll.setEnabled(
                state.isSimulationEnabled());

        saveFighter.setEnabled(
                !state.isSimulationEnabled()
                        && !state.getOpponentRows().isEmpty());

        opponentRows.removeAll();

        for (String rowText :
                state.getOpponentRows()) {

            opponentRows.add(
                    opponentRow(rowText));
        }

        historyRows.removeAll();

        if (state.getHistoryRows().isEmpty()) {
            historyRows.add(
                    UfcTheme.body(
                            "No fights simulated yet."));
        }
        else {
            for (String rowText :
                    state.getHistoryRows()) {

                historyRows.add(
                        historyRow(rowText));
            }
        }

        opponentRows.revalidate();
        opponentRows.repaint();

        historyRows.revalidate();
        historyRows.repaint();
    }

    /**
     * Creates one visual row for a ranked opponent.
     *
     * @param text formatted opponent text
     * @return configured opponent row
     */
    private JPanel opponentRow(
            String text) {

        final JPanel row =
                UfcTheme.panel(
                        new BorderLayout());

        row.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        OPPONENT_ROW_HEIGHT));

        row.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                UfcTheme.BORDER),
                        BorderFactory.createEmptyBorder(
                                OPPONENT_ROW_VERTICAL_PADDING,
                                ROW_HORIZONTAL_PADDING,
                                OPPONENT_ROW_VERTICAL_PADDING,
                                ROW_HORIZONTAL_PADDING)));

        final JLabel label =
                UfcTheme.body(text);

        label.setForeground(
                UfcTheme.TEXT);

        row.add(
                label,
                BorderLayout.CENTER);

        return row;
    }

    /**
     * Creates one visual row for a completed fight.
     *
     * @param text formatted fight-history text
     * @return configured history row
     */
    private JPanel historyRow(
            String text) {

        final JPanel row =
                UfcTheme.panel(
                        new BorderLayout());

        row.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        HISTORY_ROW_HEIGHT));

        row.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                UfcTheme.BORDER),
                        BorderFactory.createEmptyBorder(
                                HISTORY_ROW_VERTICAL_PADDING,
                                ROW_HORIZONTAL_PADDING,
                                HISTORY_ROW_VERTICAL_PADDING,
                                ROW_HORIZONTAL_PADDING)));

        final JLabel label =
                UfcTheme.body(text);

        label.setForeground(
                UfcTheme.TEXT);

        row.add(
                label,
                BorderLayout.CENTER);

        return row;
    }

    /**
     * Escapes plain text and wraps it in centered HTML understood by Swing labels.
     *
     * @param text plain presentation text
     * @return escaped HTML string, or an empty string for blank input
     */
    private String toHtml(
            String text) {

        final String html;

        if (text == null || text.isBlank()) {
            html = "";
        }
        else {
            html =
                    "<html><div style='text-align:center; width:520px;'>"
                            + text.replace(
                                    "&",
                                    "&amp;")
                            .replace(
                                    "<",
                                    "&lt;")
                            .replace(
                                    ">",
                                    "&gt;")
                            + "</div></html>";
        }

        return html;
    }
}
