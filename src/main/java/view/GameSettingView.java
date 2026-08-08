package view;

import entity.Difficulty;
import entity.UfcEra;
import interface_adapter.game_setting.GameSettingController;
import interface_adapter.game_setting.GameSettingState;
import interface_adapter.game_setting.GameSettingViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class GameSettingView extends JPanel implements PropertyChangeListener {

    private static final Color BACKGROUND = new Color(0x161616);
    private static final Color PANEL = new Color(0x262626);
    private static final Color ACCENT_RED = new Color(0xDB3216);
    private static final Color TEXT = new Color(0xF5F5F5);
    private static final Color SUBTEXT = new Color(0xBDBDBD);
    private static final Color BORDER = new Color(0x555555);

    private final GameSettingController controller;
    private final GameSettingViewModel viewModel;

    private JComboBox<Difficulty> difficultyBox;
    private JComboBox<Integer> roundsBox;
    private JComboBox<UfcEra> eraBox;
    private JCheckBox hideStatsBox;
    private JLabel messageLabel;

    public GameSettingView(GameSettingController controller,
                           GameSettingViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createSettingsPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(0x1D1D1D));
        panel.setBorder(new EmptyBorder(25, 35, 25, 35));

        JLabel title = new JLabel("GAME SETTINGS");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Configure the rules and fighter pool for this run.");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(SUBTEXT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(8));
        panel.add(subtitle);

        return panel;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(40, 60, 40, 60));
        difficultyBox = new JComboBox<>(Difficulty.values());
        difficultyBox.setSelectedItem(Difficulty.NORMAL);
        roundsBox = new JComboBox<>(new Integer[]{1, 3, 5});
        roundsBox.setSelectedItem(3);
        eraBox = new JComboBox<>(UfcEra.values());
        eraBox.setSelectedItem(UfcEra.EARLY_UFC);
        hideStatsBox = new JCheckBox("Hide opponent stats during the gauntlet");
        hideStatsBox.setBackground(PANEL);
        hideStatsBox.setForeground(TEXT);
        hideStatsBox.setFocusPainted(false);
        styleComboBox(difficultyBox);
        styleComboBox(roundsBox);
        styleComboBox(eraBox);

        panel.add(createSettingCard(
                "DIFFICULTY",
                "Controls rerolls and simulation balance.",
                difficultyBox));

        panel.add(createSettingCard(
                "ROUNDS PER FIGHT",
                "Choose how long each simulated fight can last.",
                roundsBox));

        panel.add(createSettingCard(
                "UFC ERA",
                "Changes which real fighters can appear on the attribute wheel.",
                eraBox));

        panel.add(createSettingCard(
                "OPPONENT VISIBILITY",
                "Hide ratings to make each matchup less predictable.",
                hideStatsBox));

        return panel;
    }

    private JPanel createSettingCard(String titleText,
                                     String descriptionText,
                                     JComponent input) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(25, 20, 25, 20)
        ));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(PANEL);

        JLabel title = new JLabel(titleText);
        title.setForeground(ACCENT_RED);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel description = new JLabel(descriptionText);
        description.setForeground(SUBTEXT);
        description.setFont(new Font("Arial", Font.PLAIN, 15));

        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(description);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(PANEL);
        inputPanel.setBorder(new EmptyBorder(35, 0, 0, 0));
        inputPanel.add(input, BorderLayout.SOUTH);

        card.add(textPanel, BorderLayout.NORTH);
        card.add(inputPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0x1D1D1D));
        panel.setBorder(new EmptyBorder(18, 35, 18, 40));
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(new Color(0xE6A84A));
        JButton backButton = new JButton("Back");
        JButton continueButton = new JButton("Continue");
        styleButton(backButton, new Color(0x343434));
        styleButton(continueButton, ACCENT_RED);

        backButton.addActionListener(event ->
                messageLabel.setText("Back"));

        continueButton.addActionListener(event -> controller.execute(
                (Difficulty) difficultyBox.getSelectedItem(),
                (Integer) roundsBox.getSelectedItem(),
                (UfcEra) eraBox.getSelectedItem(),
                hideStatsBox.isSelected()
        ));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttons.setBackground(new Color(0x1D1D1D));
        buttons.add(backButton);
        buttons.add(continueButton);

        panel.add(messageLabel, BorderLayout.WEST);
        panel.add(buttons, BorderLayout.EAST);
        return panel;
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Arial", Font.BOLD, 16));
        comboBox.setBackground(new Color(0x303030));
        comboBox.setForeground(TEXT);
        comboBox.setPreferredSize(new Dimension(100, 55));
        comboBox.setFocusable(false);
    }

    private void styleButton(JButton button, Color background) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(TEXT);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
    }


    @Override
    public void propertyChange(PropertyChangeEvent event) {
        GameSettingState state = viewModel.getState();

        if (state.isConfigured()) {
            messageLabel.setForeground(new Color(0x7BC67B));
            messageLabel.setText(
                    "Run configured. Eligible wheel fighters: "
                            + state.getEligibleFighterNames().size()
            );

        }
        else {
            messageLabel.setForeground(new Color(0xE6A84A));
            messageLabel.setText(state.getErrorMessage());
        }
    }
}
