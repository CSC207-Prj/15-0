package view;
import javax.swing.*;
import java.awt.*;

public class FighterCreationView extends JPanel {
    private static final Color BACKGROUND = new Color(0x1E1E1E);
    private static final Color PANEL = new Color(0x2B2B2B);
    private static final Color ACCENT_RED = new Color(0xDB3216);
    private static final Color TEXT = new Color(0xF5F5F5);
    private static final Color SUBTEXT = new Color(0xBDBDBD);
    private static final Color BORDER = new Color(0x555555);

    private final JLabel attributesFilledLabel;
    private final JProgressBar progressBar;


    public FighterCreationView() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        attributesFilledLabel = new JLabel("0 / 6");
        progressBar = new JProgressBar(0, 6);

        add(createYourFighterPanel(), BorderLayout.WEST);
        add(createFighterDraftPanel(), BorderLayout.CENTER);
//        add(createRerollsRemainingLabel(), BorderLayout.NORTH);

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

        JLabel attributesLabel = new JLabel("Attributes Filled: ");
        attributesLabel.setForeground(SUBTEXT);
        attributesLabel.setFont(new Font("Arial", Font.BOLD, 18));

        attributesFilledLabel.setForeground(ACCENT_RED);
        attributesFilledLabel.setFont(new Font("Arial", Font.BOLD, 18));

        progressBar.setValue(0);

        panel.add(attributesLabel);
        panel.add(attributesFilledLabel);
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
        panel.add(createAttributePanel("GRAPPLING"));
        panel.add(createAttributePanel("WRESTLING"));
        panel.add(createAttributePanel("CARDIO"));
        panel.add(createAttributePanel("CHIN"));
        panel.add(createAttributePanel("FIGHT IQ"));

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

        attributeLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        sourceFighterLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));

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
        panel.add(createBuildActionsPanel(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createDraftHeaderPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PANEL);
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
        panel.setBackground(PANEL);

        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        panel.add(createPreRollPanel(), BorderLayout.CENTER);

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
        JPanel panel = new JPanel();
        panel.setBackground(PANEL);
        panel.setBorder(BorderFactory.createLineBorder(BORDER));
        return panel;
    }

    private JPanel createFighterHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        return panel;
    }

    private JPanel createFighterStatsTablePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL);
        panel.setLayout(new GridLayout(6, 1));
        return panel;
    }

    private JPanel createFighterStatsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        return panel;
    }

    private JPanel createBuildActionsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND);
        return panel;
    }

//    // Displays the total number of rerolls the player has prior to spinning (Based on the difficulty selected)
//    private JLabel createRerollsRemainingLabel() {
//        JLabel label = new JLabel("Total Rerolls: 3");
//
//        label.setFont(new Font("Arial", Font.BOLD, 30));
//        label.setForeground(TEXT);
//        label.setBackground(ACCENT_RED);
//        label.setBorder(BorderFactory.createLineBorder(BORDER));
//        label.setOpaque(true);
//        label.setHorizontalAlignment(JLabel.CENTER);
//
//        return label;
//    }



    public static void main(String[] args){

        JFrame frame = new JFrame("Fighter Creation Preview");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new FighterCreationView());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);


        frame.setVisible(true);
    }
}