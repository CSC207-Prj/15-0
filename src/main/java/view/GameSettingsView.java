package view;

import entity.Difficulty;
import entity.UfcEra;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/** User story 1 view. Stage 2 shows controls but contains no configuration use-case logic. */
public final class GameSettingsView extends JPanel {
    public GameSettingsView(Runnable backAction, Runnable continueAction) {
        setLayout(new BorderLayout());
        setBackground(UfcTheme.BACKGROUND);

        final JPanel header = UfcTheme.panel(new BorderLayout());
        header.setBackground(UfcTheme.HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(22, 32, 22, 32));
        header.add(UfcTheme.title("GAME SETTINGS"), BorderLayout.WEST);
        header.add(UfcTheme.body("Shape the fighter pool and gauntlet rules"), BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        final JComboBox<Difficulty> difficultyBox = UfcTheme.comboBox(Difficulty.values());
        difficultyBox.setSelectedItem(Difficulty.NORMAL);
        final JSpinner roundsSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
        roundsSpinner.setFont(UfcTheme.BODY);
        final JComboBox<UfcEra> eraBox = UfcTheme.comboBox(UfcEra.values());
        eraBox.setSelectedItem(UfcEra.ALL_TIME);
        final JCheckBox hideStatsBox = new JCheckBox("Hide opponent ratings during the gauntlet");
        hideStatsBox.setFont(UfcTheme.BODY);
        hideStatsBox.setForeground(UfcTheme.TEXT);
        hideStatsBox.setBackground(UfcTheme.PANEL);
        hideStatsBox.setFocusPainted(false);

        final JPanel wrapper = UfcTheme.panel(new GridBagLayout());
        wrapper.setBackground(UfcTheme.BACKGROUND);
        final JPanel card = UfcTheme.panel(new GridBagLayout());
        card.setBorder(UfcTheme.cardBorder());
        card.setPreferredSize(new Dimension(780, 500));

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(12, 16, 4, 16);
        card.add(fieldTitle("DIFFICULTY"), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(4, 16, 14, 16);
        card.add(difficultyBox, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(12, 16, 4, 16);
        card.add(fieldTitle("ROUNDS PER FIGHT"), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(4, 16, 14, 16);
        card.add(roundsSpinner, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(12, 16, 4, 16);
        card.add(fieldTitle("UFC ERA"), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(4, 16, 5, 16);
        card.add(eraBox, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 16, 18, 16);
        card.add(UfcTheme.body("The selected era will filter the real-fighter pool when Stage 3/4 logic is connected."), gbc);
        gbc.gridy++;
        card.add(hideStatsBox, gbc);
        gbc.gridy++;
        card.add(UfcTheme.body("Preview only — settings are not submitted in Stage 2."), gbc);

        wrapper.add(card);
        add(wrapper, BorderLayout.CENTER);

        final JPanel actions = UfcTheme.panel(new FlowLayout(FlowLayout.CENTER, 18, 18));
        actions.setBackground(UfcTheme.HEADER);
        final JButton back = UfcTheme.secondaryButton("Back");
        final JButton start = UfcTheme.primaryButton("Create Fighter");
        back.addActionListener(event -> backAction.run());
        start.addActionListener(event -> continueAction.run());
        actions.add(back);
        actions.add(start);
        add(actions, BorderLayout.SOUTH);
    }

    private JLabel fieldTitle(String text) {
        final JLabel label = UfcTheme.body(text);
        label.setFont(UfcTheme.BODY_BOLD);
        label.setForeground(UfcTheme.TEXT);
        return label;
    }
}
