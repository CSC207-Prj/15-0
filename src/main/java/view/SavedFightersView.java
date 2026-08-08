package view;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

/** User story 5 view. Saved roster and exhibition controls use placeholder data in Stage 2. */
public final class SavedFightersView extends JPanel {
    public SavedFightersView(Runnable backAction) {
        setLayout(new BorderLayout());
        setBackground(UfcTheme.BACKGROUND);

        final JPanel header = UfcTheme.panel(new BorderLayout());
        header.setBackground(UfcTheme.HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(22, 32, 22, 32));
        header.add(UfcTheme.title("SAVED FIGHTERS"), BorderLayout.WEST);
        header.add(UfcTheme.body("Roster, rankings, and exhibition fights"), BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        final JPanel content = UfcTheme.panel(new GridLayout(1, 2, 22, 0));
        content.setBackground(UfcTheme.BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(26, 34, 26, 34));
        content.add(createRosterPanel());
        content.add(createRightColumn());
        add(content, BorderLayout.CENTER);

        final JPanel actions = UfcTheme.panel(new FlowLayout(FlowLayout.CENTER, 16, 16));
        actions.setBackground(UfcTheme.HEADER);
        final JButton back = UfcTheme.secondaryButton("Back to Home");
        final JButton load = UfcTheme.primaryButton("Load Selected");
        final JButton delete = UfcTheme.dangerButton("Delete Selected");
        back.addActionListener(event -> backAction.run());
        actions.add(back);
        actions.add(load);
        actions.add(delete);
        add(actions, BorderLayout.SOUTH);
    }

    private JScrollPane createRosterPanel() {
        final JPanel panel = UfcTheme.panel(null);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(UfcTheme.cardBorder());
        panel.add(UfcTheme.section("YOUR ROSTER"));
        panel.add(Box.createVerticalStrut(14));
        panel.add(fighterCard("The Prospect", "Lightweight", "15-0", "94.2", "12 finishes"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(fighterCard("Night Shift", "Welterweight", "12-3", "91.6", "9 finishes"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(fighterCard("Southpaw", "Featherweight", "11-4", "89.8", "8 finishes"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(fighterCard("Pressure Test", "Middleweight", "9-6", "87.4", "7 finishes"));
        return UfcTheme.scroll(panel);
    }

    private JPanel createRightColumn() {
        final JPanel column = UfcTheme.panel(new GridLayout(2, 1, 0, 18));
        column.add(createTopThreePanel());
        column.add(createExhibitionPanel());
        return column;
    }

    private JPanel createTopThreePanel() {
        final JPanel panel = UfcTheme.panel(null);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(UfcTheme.cardBorder());
        panel.add(UfcTheme.section("TOP 3 FIGHTERS"));
        panel.add(Box.createVerticalStrut(14));
        panel.add(rankRow("1", "The Prospect", "15-0 • 12 finishes"));
        panel.add(rankRow("2", "Night Shift", "12-3 • 9 finishes"));
        panel.add(rankRow("3", "Southpaw", "11-4 • 8 finishes"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(UfcTheme.body("Tie-break logic is added with User Story 5 in Stage 7."));
        return panel;
    }

    private JPanel createExhibitionPanel() {
        final JPanel panel = UfcTheme.panel(null);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(UfcTheme.cardBorder());
        panel.add(UfcTheme.section("EXHIBITION MATCH"));
        panel.add(Box.createVerticalStrut(16));
        final String[] names = {"The Prospect", "Night Shift", "Southpaw", "Pressure Test"};
        final JComboBox<String> fighterA = UfcTheme.comboBox(names);
        final JComboBox<String> fighterB = UfcTheme.comboBox(names);
        fighterB.setSelectedIndex(1);
        panel.add(UfcTheme.body("Fighter A"));
        panel.add(fighterA);
        panel.add(Box.createVerticalStrut(12));
        panel.add(UfcTheme.body("Fighter B"));
        panel.add(fighterB);
        panel.add(Box.createVerticalStrut(18));
        final JButton fight = UfcTheme.primaryButton("Run Exhibition");
        fight.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(fight);
        panel.add(Box.createVerticalStrut(10));
        panel.add(UfcTheme.body("Preview only — no persistence or simulation logic is active yet."));
        return panel;
    }

    private JPanel fighterCard(String name, String division, String record, String overall, String finishes) {
        final JPanel row = UfcTheme.panel(new BorderLayout(12, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 86));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UfcTheme.BORDER),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));
        final JPanel text = UfcTheme.panel(null);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        final JLabel nameLabel = UfcTheme.body(name);
        nameLabel.setFont(UfcTheme.BODY_BOLD);
        nameLabel.setForeground(UfcTheme.TEXT);
        text.add(nameLabel);
        text.add(UfcTheme.body(division + " • " + record + " • " + finishes));
        final JLabel rating = UfcTheme.centeredLabel(overall, UfcTheme.SECTION, UfcTheme.ACCENT);
        row.add(text, BorderLayout.CENTER);
        row.add(rating, BorderLayout.EAST);
        return row;
    }

    private JPanel rankRow(String rank, String name, String record) {
        final JPanel row = UfcTheme.panel(new BorderLayout(12, 0));
        row.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));
        final JLabel badge = UfcTheme.centeredLabel(rank, UfcTheme.BODY_BOLD, UfcTheme.TEXT);
        badge.setOpaque(true);
        badge.setBackground(UfcTheme.ACCENT);
        badge.setPreferredSize(new Dimension(34, 34));
        final JPanel text = UfcTheme.panel(null);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        final JLabel nameLabel = UfcTheme.body(name);
        nameLabel.setFont(UfcTheme.BODY_BOLD);
        nameLabel.setForeground(UfcTheme.TEXT);
        text.add(nameLabel);
        text.add(UfcTheme.body(record));
        row.add(badge, BorderLayout.WEST);
        row.add(text, BorderLayout.CENTER);
        return row;
    }
}
