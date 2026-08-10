package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import entity.CustomFighter;
import interface_adapter.saved_fighters.DeleteFighterController;
import interface_adapter.saved_fighters.ExhibitionController;
import interface_adapter.saved_fighters.LoadFighterController;
import interface_adapter.saved_fighters.SaveFighterController;
import interface_adapter.saved_fighters.SavedFighterRow;
import interface_adapter.saved_fighters.SavedFightersState;
import interface_adapter.saved_fighters.SavedFightersViewModel;
import interface_adapter.saved_fighters.ViewRosterController;

/**
 * User story 5 view: the live Saved Fighters screen. Renders the ranked
 * roster, top three, and exhibition controls from the SavedFightersViewModel
 * and refreshes itself whenever a presenter updates the state.
 */
public final class SavedFightersView extends JPanel implements PropertyChangeListener {

    private final SavedFightersViewModel viewModel;
    private final ViewRosterController viewRosterController;
    private final DeleteFighterController deleteFighterController;
    private final LoadFighterController loadFighterController;
    private final ExhibitionController exhibitionController;
    private final SaveFighterController saveFighterController;

    private final JPanel rosterPanel = UfcTheme.panel(null);
    private final JPanel topThreePanel = UfcTheme.panel(null);
    private final JComboBox<String> fighterASelect = UfcTheme.comboBox(new String[0]);
    private final JComboBox<String> fighterBSelect = UfcTheme.comboBox(new String[0]);
    private final JLabel exhibitionResultLabel = UfcTheme.body(" ");
    private final JLabel loadedFighterLabel = UfcTheme.body(" ");
    private final JLabel messageLabel = UfcTheme.body(" ");
    private final JLabel errorLabel = UfcTheme.body(" ");

    private String selectedFighterName;

    public SavedFightersView(ViewRosterController viewRosterController,
                             DeleteFighterController deleteFighterController,
                             LoadFighterController loadFighterController,
                             ExhibitionController exhibitionController,
                             SaveFighterController saveFighterController,
                             SavedFightersViewModel viewModel,
                             Runnable backAction) {
        this.viewRosterController = Objects.requireNonNull(viewRosterController);
        this.deleteFighterController = Objects.requireNonNull(deleteFighterController);
        this.loadFighterController = Objects.requireNonNull(loadFighterController);
        this.exhibitionController = Objects.requireNonNull(exhibitionController);
        this.saveFighterController = Objects.requireNonNull(saveFighterController);
        this.viewModel = Objects.requireNonNull(viewModel);
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(UfcTheme.BACKGROUND);
        add(buildHeader(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
        add(buildActions(backAction), BorderLayout.SOUTH);

        // refresh the roster every time this card is shown
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent event) {
                viewRosterController.execute();
            }
        });
        viewRosterController.execute();
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        refreshFromState(viewModel.getState());
    }

    private JPanel buildHeader() {
        final JPanel header = UfcTheme.panel(new BorderLayout());
        header.setBackground(UfcTheme.HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(22, 32, 22, 32));
        header.add(UfcTheme.title(SavedFightersViewModel.TITLE_LABEL), BorderLayout.WEST);
        header.add(UfcTheme.body(SavedFightersViewModel.SUBTITLE_LABEL), BorderLayout.EAST);
        return header;
    }

    private JPanel buildContent() {
        final JPanel content = UfcTheme.panel(new GridLayout(1, 2, 22, 0));
        content.setBackground(UfcTheme.BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(26, 34, 26, 34));

        rosterPanel.setLayout(new BoxLayout(rosterPanel, BoxLayout.Y_AXIS));
        rosterPanel.setBorder(UfcTheme.cardBorder());
        final JScrollPane rosterScroll = UfcTheme.scroll(rosterPanel);
        content.add(rosterScroll);
        content.add(buildRightColumn());
        return content;
    }

    private JPanel buildRightColumn() {
        final JPanel column = UfcTheme.panel(new GridLayout(2, 1, 0, 18));
        topThreePanel.setLayout(new BoxLayout(topThreePanel, BoxLayout.Y_AXIS));
        topThreePanel.setBorder(UfcTheme.cardBorder());
        column.add(topThreePanel);
        column.add(buildExhibitionPanel());
        return column;
    }

    private JPanel buildExhibitionPanel() {
        final JPanel panel = UfcTheme.panel(null);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(UfcTheme.cardBorder());
        panel.add(UfcTheme.section(SavedFightersViewModel.EXHIBITION_LABEL));
        panel.add(Box.createVerticalStrut(16));
        panel.add(UfcTheme.body("Fighter A"));
        panel.add(fighterASelect);
        panel.add(Box.createVerticalStrut(12));
        panel.add(UfcTheme.body("Fighter B"));
        panel.add(fighterBSelect);
        panel.add(Box.createVerticalStrut(18));

        final JButton fight = UfcTheme.primaryButton(SavedFightersViewModel.EXHIBITION_BUTTON_LABEL);
        fight.setAlignmentX(LEFT_ALIGNMENT);
        fight.addActionListener(event -> exhibitionController.execute(
                selectedItem(fighterASelect), selectedItem(fighterBSelect)));
        panel.add(fight);
        panel.add(Box.createVerticalStrut(10));
        exhibitionResultLabel.setFont(UfcTheme.BODY_BOLD);
        exhibitionResultLabel.setForeground(UfcTheme.TEXT);
        panel.add(exhibitionResultLabel);
        return panel;
    }

    private JPanel buildActions(Runnable backAction) {
        final JPanel south = UfcTheme.panel(null);
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.setBackground(UfcTheme.HEADER);

        final JPanel statusRow = UfcTheme.panel(new FlowLayout(FlowLayout.CENTER, 16, 4));
        statusRow.setBackground(UfcTheme.HEADER);
        loadedFighterLabel.setForeground(UfcTheme.TEXT);
        messageLabel.setForeground(UfcTheme.SUCCESS);
        errorLabel.setForeground(UfcTheme.WARNING);
        statusRow.add(loadedFighterLabel);
        statusRow.add(messageLabel);
        statusRow.add(errorLabel);
        south.add(statusRow);

        final JPanel buttonsRow = UfcTheme.panel(new FlowLayout(FlowLayout.CENTER, 16, 12));
        buttonsRow.setBackground(UfcTheme.HEADER);
        final JButton back = UfcTheme.secondaryButton(SavedFightersViewModel.BACK_BUTTON_LABEL);
        final JButton load = UfcTheme.primaryButton(SavedFightersViewModel.LOAD_BUTTON_LABEL);
        final JButton delete = UfcTheme.dangerButton(SavedFightersViewModel.DELETE_BUTTON_LABEL);
        back.addActionListener(event -> backAction.run());
        load.addActionListener(event -> loadFighterController.execute(selectedOrEmpty()));
        delete.addActionListener(event -> {
            deleteFighterController.execute(selectedOrEmpty());
            selectedFighterName = null;
            viewRosterController.execute();
        });
        buttonsRow.add(back);
        buttonsRow.add(load);
        buttonsRow.add(delete);
        south.add(buttonsRow);
        return south;
    }

    private void refreshFromState(SavedFightersState state) {
        rebuildRoster(state.getRows());
        rebuildTopThree(state.getTopThree());
        rebuildCombo(fighterASelect, state.getRows());
        rebuildCombo(fighterBSelect, state.getRows());
        loadedFighterLabel.setText(orSpace(state.getLoadedFighterDetails()));
        exhibitionResultLabel.setText(orSpace(state.getExhibitionResult()));
        messageLabel.setText(orSpace(state.getMessage()));
        errorLabel.setText(orSpace(state.getError()));
        offerRenameForDuplicate(state);
        revalidate();
        repaint();
    }

    /**
     * When a save failed because the name is taken, asks the user for a
     * different name and retries the save. The pending fighter is cleared
     * from the state first so repeated refreshes cannot re-open the dialog.
     */
    private void offerRenameForDuplicate(SavedFightersState state) {
        final CustomFighter pending = state.getDuplicatePending();
        if (pending == null) {
            return;
        }
        state.setDuplicatePending(null);
        SwingUtilities.invokeLater(() -> {
            final String newName = (String) JOptionPane.showInputDialog(
                    this,
                    "A fighter named \"" + pending.getName() + "\" is already in your roster.\n"
                            + "Choose a different name to save this fighter:",
                    "Name already taken",
                    JOptionPane.WARNING_MESSAGE,
                    null, null,
                    pending.getName());
            if (newName == null || newName.trim().isEmpty()
                    || newName.trim().equals(pending.getName())) {
                errorLabel.setText("Fighter was not saved — the name \""
                        + pending.getName() + "\" is already in your roster.");
                return;
            }
            pending.setName(newName.trim());
            saveFighterController.execute(pending);
            viewRosterController.execute();
        });
    }

    private void rebuildRoster(List<SavedFighterRow> rows) {
        rosterPanel.removeAll();
        rosterPanel.add(UfcTheme.section(SavedFightersViewModel.ROSTER_LABEL));
        rosterPanel.add(Box.createVerticalStrut(14));
        if (rows.isEmpty()) {
            rosterPanel.add(UfcTheme.body(SavedFightersViewModel.EMPTY_ROSTER_MESSAGE));
            return;
        }
        for (SavedFighterRow row : rows) {
            rosterPanel.add(fighterCard(row));
            rosterPanel.add(Box.createVerticalStrut(10));
        }
    }

    private JPanel fighterCard(SavedFighterRow row) {
        final boolean selected = row.getName().equals(selectedFighterName);
        final JPanel card = UfcTheme.panel(new BorderLayout(12, 0));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 86));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(selected ? UfcTheme.ACCENT : UfcTheme.BORDER),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));

        final JPanel text = UfcTheme.panel(null);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        final JLabel nameLabel = UfcTheme.body(row.getName());
        nameLabel.setFont(UfcTheme.BODY_BOLD);
        nameLabel.setForeground(UfcTheme.TEXT);
        text.add(nameLabel);
        text.add(UfcTheme.body(row.getWeightClassName() + " • " + row.getRecordText()
                + " • " + row.getFinishes() + " finishes"));
        card.add(text, BorderLayout.CENTER);

        final JLabel record = UfcTheme.centeredLabel(row.getRecordText(),
                UfcTheme.SECTION, UfcTheme.ACCENT);
        card.add(record, BorderLayout.EAST);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                selectedFighterName = row.getName();
                refreshFromState(viewModel.getState());
            }
        });
        return card;
    }

    private void rebuildTopThree(List<SavedFighterRow> topThree) {
        topThreePanel.removeAll();
        topThreePanel.add(UfcTheme.section(SavedFightersViewModel.TOP_THREE_LABEL));
        topThreePanel.add(Box.createVerticalStrut(14));
        if (topThree.isEmpty()) {
            topThreePanel.add(UfcTheme.body(SavedFightersViewModel.EMPTY_ROSTER_MESSAGE));
            return;
        }
        int rank = 1;
        for (SavedFighterRow row : topThree) {
            topThreePanel.add(rankRow(String.valueOf(rank), row));
            rank++;
        }
    }

    private JPanel rankRow(String rank, SavedFighterRow row) {
        final JPanel rowPanel = UfcTheme.panel(new BorderLayout(12, 0));
        rowPanel.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));
        final JLabel badge = UfcTheme.centeredLabel(rank, UfcTheme.BODY_BOLD, UfcTheme.TEXT);
        badge.setOpaque(true);
        badge.setBackground(UfcTheme.ACCENT);
        badge.setPreferredSize(new Dimension(34, 34));
        final JPanel text = UfcTheme.panel(null);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        final JLabel nameLabel = UfcTheme.body(row.getName());
        nameLabel.setFont(UfcTheme.BODY_BOLD);
        nameLabel.setForeground(UfcTheme.TEXT);
        text.add(nameLabel);
        text.add(UfcTheme.body(row.getRecordText() + " • " + row.getFinishes() + " finishes"));
        rowPanel.add(badge, BorderLayout.WEST);
        rowPanel.add(text, BorderLayout.CENTER);
        return rowPanel;
    }

    private static void rebuildCombo(JComboBox<String> combo, List<SavedFighterRow> rows) {
        final Object previous = combo.getSelectedItem();
        combo.removeAllItems();
        for (SavedFighterRow row : rows) {
            combo.addItem(row.getName());
        }
        if (previous != null) {
            combo.setSelectedItem(previous);
        }
    }

    private String selectedOrEmpty() {
        return selectedFighterName == null ? "" : selectedFighterName;
    }

    private static String selectedItem(JComboBox<String> combo) {
        final Object item = combo.getSelectedItem();
        return item == null ? "" : item.toString();
    }

    private static String orSpace(String text) {
        return text == null || text.isEmpty() ? " " : text;
    }
}
