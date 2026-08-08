package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import interface_adapter.fighter_creation.FighterCreationViewModel;
import java.beans.PropertyChangeEvent;

public class FighterCreationView extends JPanel implements PropertyChangeListener {
    private final FighterCreationViewModel viewModel;
    private JLabel fighterNameLabel;
    private JLabel fighterDetailsLabel;
    private JLabel overallValueLabel;
    private final Map<String, JProgressBar> fighterStatBars = new HashMap<>();
    private final Map<String, JLabel> fighterStatValueLabels = new HashMap<>();
    private final Map<String, JLabel> attributeValueLabels = new HashMap<>();
    private final Map<String, JLabel> attributeSourceLabels = new HashMap<>();
    private static final String PRE_ROLL = "preRoll";
    private static final String FIGHTER_CARD = "fighterCard";
    private final CardLayout draftCardLayout = new CardLayout();
    private final JPanel draftCards = new JPanel(draftCardLayout);
    private JPanel footerPanel;
    private JPanel selectedStatRow;
    private String selectedAttribute;
    private JButton assignButton;
    private JButton rerollButton;
    private JButton cancelButton;
    private static final Color BACKGROUND = new Color(0x1b1b1b);
    private static final Color PANEL = new Color(0x2B2B2B);
    private static final Color ACCENT_RED = new Color(0xDB3216);
    private static final Color TEXT = new Color(0xF5F5F5);
    private static final Color SUBTEXT = new Color(0xBDBDBD);
    private static final Color BORDER = new Color(0x555555);

    private final JLabel attributesFilledLabel;
    private final JProgressBar progressBar;


    public FighterCreationView(FighterCreationViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        attributesFilledLabel = new JLabel("0 / 6");
        progressBar = new JProgressBar(0, 6);

        add(createYourFighterPanel(), BorderLayout.WEST);
        add(createFighterDraftPanel(), BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (FighterCreationViewModel.STATE_PROPERTY.equals(event.getPropertyName())) {
            setFighterName(viewModel.getFighterName());
            setFighterDetails(viewModel.getFighterDetails());
            setOverall(viewModel.getOverall());

            for (Map.Entry<String, Integer> entry : viewModel.getFighterStats().entrySet()) {
                setStat(entry.getKey(), entry.getValue());
            }

            for (String attribute : attributeValueLabels.keySet()) {
                Integer value = viewModel.getAssignedValues().get(attribute);
                String fighterName = viewModel.getAssignedFighters().get(attribute);

                if (value != null) {
                    assignAttribute(attribute, value, fighterName);
                }
                else {
                    attributeValueLabels.get(attribute).setText("—");
                    attributeSourceLabels.get(attribute).setText("Not Assigned");
                }
            }

            int attributesFilled = viewModel.getAttributesFilled();
            attributesFilledLabel.setText(attributesFilled + " / 6");
            progressBar.setValue(attributesFilled);

            rerollButton.setText(
                    "↻  Reroll Fighter (" + viewModel.getRerollsLeft() + " left)");

            if (viewModel.isFighterRevealed()) {
                showFighterCard();
            }
            else {
                showPreRoll();
            }
        }
    }

    /**
     * Creates the left side of the fighter creation screen.
     * This contains the stats of the player's fighter and progress.
     */
    private JPanel createYourFighterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBackground(PANEL);
        panel.setBorder(BorderFactory.createLineBorder(BORDER));

        JLabel titleLabel = new JLabel("YOUR FIGHTER", SwingConstants.CENTER);
        titleLabel.setForeground(TEXT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(PANEL);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 6, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, titlePanel.getPreferredSize().height));
        panel.add(titlePanel);
        panel.add(Box.createVerticalStrut(12));
        panel.add(createFighterProgressPanel());
        panel.add(Box.createVerticalStrut(16));
        panel.add(createAttributeTablePanel());

        return panel;
    }

    private JPanel createFighterProgressPanel()  {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 18, 12, 18));

        JLabel attributesLabel = new JLabel("Attributes Filled: ");
        attributesLabel.setForeground(SUBTEXT);
        attributesLabel.setFont(new Font("Arial", Font.BOLD, 18));

        attributesFilledLabel.setForeground(ACCENT_RED);
        attributesFilledLabel.setFont(new Font("Arial", Font.BOLD, 18));

        progressBar.setUI(new BasicProgressBarUI());
        progressBar.setForeground(ACCENT_RED);
        progressBar.setBackground(new Color(0x3A3A3A));
        progressBar.setOpaque(true);
        progressBar.setBorder(BorderFactory.createLineBorder(BORDER));

        panel.add(attributesLabel);
        panel.add(attributesFilledLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(progressBar);

        panel.setBackground(PANEL);
        return panel;
    }

    private JPanel createAttributeTablePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PANEL);
        panel.setBorder(BorderFactory.createLineBorder(BORDER));

        panel.add(createAttributeHeaderPanel());
        panel.add(createAttributePanel("STRIKING"));
        panel.add(createAttributePanel("STRIKE DEF"));
        panel.add(createAttributePanel("TAKEDOWNS"));
        panel.add(createAttributePanel("TD DEFENSE"));
        panel.add(createAttributePanel("SUBMISSION"));
        panel.add(createAttributePanel("CONTROL"));

        return panel;
    }

    private JPanel createAttributeHeaderPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3));
        panel.setBackground(new Color(0x333333));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        panel.setPreferredSize(new Dimension(0, 40));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        JLabel attributeLabel = new JLabel("ATTRIBUTE");
        JLabel valueLabel = new JLabel("VALUE");
        JLabel sourceFighterLabel = new JLabel("FIGHTER");

        attributeLabel.setForeground(SUBTEXT);
        valueLabel.setForeground(SUBTEXT);
        sourceFighterLabel.setForeground(SUBTEXT);

        Font headerFont = new Font("Arial", Font.BOLD, 18);
        attributeLabel.setFont(headerFont);
        valueLabel.setFont(headerFont);
        sourceFighterLabel.setFont(headerFont);

        attributeLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        sourceFighterLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));

        panel.add(attributeLabel);
        panel.add(valueLabel);
        panel.add(sourceFighterLabel);

        return panel;
    }

    private JPanel createAttributePanel(String attributeName) {
        JPanel panel = new JPanel(new GridLayout(1, 3));
        panel.setBackground(PANEL);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));

        JLabel attributeLabel = new JLabel(attributeName);
        JLabel valueLabel = new JLabel("—");
        JLabel sourceFighterLabel = new JLabel("Not Assigned");

        attributeLabel.setForeground(SUBTEXT);
        valueLabel.setForeground(SUBTEXT);
        sourceFighterLabel.setForeground(SUBTEXT);
        attributeValueLabels.put(attributeName, valueLabel);
        attributeSourceLabels.put(attributeName, sourceFighterLabel);

        attributeLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        sourceFighterLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        attributeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        sourceFighterLabel.setFont(new Font("Arial", Font.PLAIN, 17));

        panel.add(attributeLabel);
        panel.add(valueLabel);
        panel.add(sourceFighterLabel);

        return panel;
    }

    /**
     * Creates the fighter draft panel on the right side of the screen.
     * The player spins for a fighter and assigns their attributes.
     */
    private JPanel createFighterDraftPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL);
        panel.add(createDraftHeaderPanel(), BorderLayout.NORTH);
        panel.add(createDraftContentPanel(), BorderLayout.CENTER);
        footerPanel = createBuildActionsPanel();
        footerPanel.setVisible(false);
        panel.add(footerPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createDraftHeaderPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(0x161616));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel titleLabel = new JLabel("FIGHTER DRAFT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(TEXT);

        JLabel instructionLabel = new JLabel("Spin to reveal a random UFC fighter.");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionLabel.setForeground(SUBTEXT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(4));
        panel.add(instructionLabel);

        return panel;
    }

    private JPanel createDraftContentPanel(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0x161616));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        draftCards.add(createPreRollPanel(), PRE_ROLL);
        draftCards.add(createFighterCardPanel(), FIGHTER_CARD);
        panel.add(draftCards, BorderLayout.CENTER);
        draftCardLayout.show(draftCards, PRE_ROLL);

        return panel;
    }

    private JPanel createPreRollPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PANEL);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createDashedBorder(BORDER),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        ImageIcon originalIcon = new ImageIcon("src/main/resources/images/diceroll.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(new ImageIcon(scaledImage));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Ready to Spin");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(TEXT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel line1 = new JLabel("Click the button below to reveal");
        line1.setFont(new Font("Arial", Font.PLAIN, 16));
        line1.setForeground(SUBTEXT);
        line1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel line2 = new JLabel("a random UFC fighter.");
        line2.setFont(new Font("Arial", Font.PLAIN, 16));
        line2.setForeground(SUBTEXT);
        line2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton spinButton = new JButton("Spin Fighter");
        spinButton.setFont(new Font("Arial", Font.BOLD, 18));
        spinButton.setForeground(TEXT);
        spinButton.setBackground(ACCENT_RED);
        spinButton.setOpaque(true);
        spinButton.setContentAreaFilled(true);
        spinButton.setBorderPainted(false);
        spinButton.setFocusPainted(false);
        Dimension buttonSize = new Dimension(220, 50);
        spinButton.setPreferredSize(buttonSize);
        spinButton.setMinimumSize(buttonSize);
        spinButton.setMaximumSize(buttonSize);
        spinButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        spinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFighterCard();
            }
        });

        panel.add(Box.createVerticalGlue());
        panel.add(iconLabel);
        panel.add(Box.createVerticalStrut(12));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(line1);
        panel.add(Box.createVerticalStrut(4));
        panel.add(line2);
        panel.add(Box.createVerticalStrut(30));
        panel.add(spinButton);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createFighterCardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL);
        panel.setBorder(BorderFactory.createLineBorder(BORDER));

        panel.add(createFighterHeaderPanel(), BorderLayout.NORTH);
        panel.add(createFighterStatsTablePanel(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFighterHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0x222222));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JPanel fighterInfoPanel = new JPanel();
        fighterInfoPanel.setLayout(new BoxLayout(fighterInfoPanel, BoxLayout.Y_AXIS));
        fighterInfoPanel.setBackground(new Color(0x222222));

        fighterNameLabel = new JLabel("Fighter Name");
        fighterNameLabel.setFont(new Font("Arial", Font.BOLD, 40));
        fighterNameLabel.setForeground(TEXT);

        fighterDetailsLabel = new JLabel("Record • Weight");
        fighterDetailsLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        fighterDetailsLabel.setForeground(SUBTEXT);

        JPanel overallPanel = new JPanel();
        overallPanel.setLayout(new BoxLayout(overallPanel, BoxLayout.Y_AXIS));
        overallPanel.setBackground(BACKGROUND);
        overallPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));

        JLabel overallTitleLabel = new JLabel("OVR");
        overallTitleLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        overallTitleLabel.setForeground(SUBTEXT);
        overallTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        overallValueLabel = new JLabel("Value");
        overallValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        overallValueLabel.setForeground(ACCENT_RED);
        overallValueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        fighterInfoPanel.add(fighterNameLabel);
        fighterInfoPanel.add(Box.createVerticalStrut(8));
        fighterInfoPanel.add(fighterDetailsLabel);

        overallPanel.add(Box.createVerticalGlue());
        overallPanel.add(overallTitleLabel);
        overallPanel.add(Box.createVerticalStrut(3));
        overallPanel.add(overallValueLabel);
        overallPanel.add(Box.createVerticalGlue());

        panel.add(fighterInfoPanel, BorderLayout.WEST);
        panel.add(overallPanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createFighterStatsTablePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PANEL);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));

        panel.add(createFighterStatsPanel("STRIKING"));
        panel.add(createFighterStatsPanel("STRIKE DEF"));
        panel.add(createFighterStatsPanel("TAKEDOWNS"));
        panel.add(createFighterStatsPanel("TD DEFENSE"));
        panel.add(createFighterStatsPanel("SUBMISSION"));
        panel.add(createFighterStatsPanel("CONTROL"));

        return panel;
    }

    private JPanel createFighterStatsPanel(String attributeName) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)));

        JLabel attributeLabel = new JLabel(attributeName);
        attributeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        attributeLabel.setForeground(TEXT);
        Dimension attributeSize = new Dimension(150, 30);
        attributeLabel.setPreferredSize(attributeSize);

        JProgressBar statBar = new JProgressBar(0, 100);
        statBar.setUI(new BasicProgressBarUI());
        statBar.setValue(50);
        statBar.setForeground(ACCENT_RED);
        statBar.setBorder(BorderFactory.createLineBorder(BORDER));
        statBar.setPreferredSize(new Dimension(850, 16));
        statBar.setMinimumSize(new Dimension(500, 16));
        statBar.setBackground(new Color(0x3A3A3A));
        statBar.setOpaque(true);

        JPanel statBarWrapper = new JPanel(new GridBagLayout());
        statBarWrapper.setBackground(BACKGROUND);
        statBarWrapper.add(statBar);

        JLabel valueLabel = new JLabel("Value");
        valueLabel.setForeground(TEXT);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 18));

        fighterStatBars.put(attributeName, statBar);
        fighterStatValueLabels.put(attributeName, valueLabel);

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                selectedAttribute = attributeName;
                selectStatRow(panel);
            }
        });

        panel.add(attributeLabel, BorderLayout.WEST);
        panel.add(statBarWrapper, BorderLayout.CENTER);
        panel.add(valueLabel, BorderLayout.EAST);

        return panel;
    }

    private void selectStatRow(JPanel row) {
        if (selectedStatRow != null) {
            selectedStatRow.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)));
        }

        selectedStatRow = row;

        selectedStatRow.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_RED, 2),
                BorderFactory.createEmptyBorder(10, 18, 10, 18)));
        assignButton.setEnabled(true);
        repaint();
    }

    private JPanel createBuildActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 18));
        panel.setBackground(new Color(0x161616));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));

        assignButton = new JButton("✓ Assign Selected Attribute");
        assignButton.setEnabled(false);
        assignButton.setFont(new Font("Arial", Font.BOLD, 18));
        assignButton.setForeground(TEXT);
        assignButton.setBackground(ACCENT_RED);
        assignButton.setOpaque(true);
        assignButton.setContentAreaFilled(true);
        assignButton.setBorderPainted(false);
        assignButton.setFocusPainted(false);
        Dimension assignButtonSize = new Dimension(290, 50);
        assignButton.setPreferredSize(assignButtonSize);
        assignButton.setMinimumSize(assignButtonSize);
        assignButton.setMaximumSize(assignButtonSize);
        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked Assign: " + selectedAttribute);
            }
        });

        rerollButton = new JButton("↻  Reroll Fighter (X left)");
        rerollButton.setFont(new Font("Arial", Font.BOLD, 18));
        rerollButton.setForeground(TEXT);
        rerollButton.setBackground(PANEL);
        rerollButton.setOpaque(true);
        rerollButton.setContentAreaFilled(true);
        rerollButton.setBorderPainted(false);
        rerollButton.setFocusPainted(false);
        Dimension rerollButtonSize = new Dimension(250, 50);
        rerollButton.setPreferredSize(rerollButtonSize);
        rerollButton.setMinimumSize(rerollButtonSize);
        rerollButton.setMaximumSize(rerollButtonSize);
        rerollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked Reroll");
            }
        });

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 18));
        cancelButton.setForeground(TEXT);
        cancelButton.setBackground(PANEL);
        cancelButton.setOpaque(true);
        cancelButton.setContentAreaFilled(true);
        cancelButton.setBorderPainted(false);
        cancelButton.setFocusPainted(false);
        Dimension cancelButtonSize = new Dimension(170, 50);
        cancelButton.setPreferredSize(cancelButtonSize);
        cancelButton.setMinimumSize(cancelButtonSize);
        cancelButton.setMaximumSize(cancelButtonSize);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked Cancel");
            }
        });

        panel.add(assignButton);
        panel.add(rerollButton);
        panel.add(cancelButton);

        return panel;
    }

    public void setFighterName(String name) {
        fighterNameLabel.setText(name);
    }

    public void setFighterDetails(String details) {
        fighterDetailsLabel.setText(details);
    }

    public void setOverall(int overall) {
        overallValueLabel.setText(String.valueOf(overall));
    }

    public void setStat(String attribute, int value) {
        fighterStatBars.get(attribute).setValue(value);
        fighterStatValueLabels.get(attribute).setText(String.valueOf(value));
    }

    public void assignAttribute(String attribute, int value, String fighterName) {
        attributeValueLabels.get(attribute).setText(String.valueOf(value));
        attributeSourceLabels.get(attribute).setText(fighterName);
    }

    public void showPreRoll() {
        draftCardLayout.show(draftCards, PRE_ROLL);
        footerPanel.setVisible(false);
    }

    public void showFighterCard() {
        draftCardLayout.show(draftCards, FIGHTER_CARD);
        footerPanel.setVisible(true);
    }

    public static void main(String[] args){

        JFrame frame = new JFrame("Fighter Creation Preview");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FighterCreationViewModel viewModel = new FighterCreationViewModel();
        frame.setContentPane(new FighterCreationView(viewModel));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);


        frame.setVisible(true);
    }
}