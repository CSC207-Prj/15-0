package view;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

/** Home/rules screen. Buttons perform navigation only in Stage 2. */
public final class WelcomeView extends JPanel {
    public WelcomeView(Runnable newRunAction,
                       Runnable savedFightersAction,
                       Runnable fighterBrowserAction,
                       Runnable exitAction) {
        setLayout(new BorderLayout());
        setBackground(UfcTheme.BACKGROUND);

        final JPanel header = UfcTheme.panel(new BorderLayout());
        header.setBackground(UfcTheme.HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(24, 34, 24, 34));
        header.add(UfcTheme.title("15-0"), BorderLayout.WEST);
        header.add(UfcTheme.body("Build-A-Fighter Gauntlet"), BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        final JPanel content = UfcTheme.panel(new GridLayout(1, 2, 24, 0));
        content.setBackground(UfcTheme.BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(35, 45, 35, 45));
        content.add(createHeroPanel());
        content.add(createRulesPanel());
        add(content, BorderLayout.CENTER);

        final JPanel actions = UfcTheme.panel(new FlowLayout(FlowLayout.CENTER, 14, 18));
        actions.setBackground(UfcTheme.HEADER);
        final JButton newRun = UfcTheme.primaryButton("Start New Run");
        final JButton browse = UfcTheme.secondaryButton("Browse UFC Fighters");
        final JButton saved = UfcTheme.secondaryButton("Saved Fighters");
        final JButton exit = UfcTheme.secondaryButton("Exit");
        newRun.addActionListener(event -> newRunAction.run());
        browse.addActionListener(event -> fighterBrowserAction.run());
        saved.addActionListener(event -> savedFightersAction.run());
        exit.addActionListener(event -> exitAction.run());
        actions.add(newRun);
        actions.add(browse);
        actions.add(saved);
        actions.add(exit);
        add(actions, BorderLayout.SOUTH);
    }

    private JPanel createHeroPanel() {
        final JPanel panel = UfcTheme.panel(null);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(UfcTheme.cardBorder());

        final JLabel small = UfcTheme.body("YOUR ROAD TO AN UNDEFEATED RECORD");
        small.setForeground(UfcTheme.ACCENT);
        small.setFont(UfcTheme.BODY_BOLD);
        final JLabel headline = new JLabel("BUILD. FIGHT. GO 15-0.");
        headline.setForeground(UfcTheme.TEXT);
        headline.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 42));
        final JLabel description = UfcTheme.body(
                "<html>Draft six attributes from real UFC fighters, lock in a division,<br>and complete all 15 ranked fights.</html>");

        small.setAlignmentX(Component.LEFT_ALIGNMENT);
        headline.setAlignmentX(Component.LEFT_ALIGNMENT);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(Box.createVerticalGlue());
        panel.add(small);
        panel.add(Box.createVerticalStrut(14));
        panel.add(headline);
        panel.add(Box.createVerticalStrut(20));
        panel.add(description);
        panel.add(Box.createVerticalStrut(28));
        panel.add(UfcTheme.body("Stage 2 preview data only — gameplay is connected in later stages."));
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createRulesPanel() {
        final JPanel panel = UfcTheme.panel(null);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(UfcTheme.cardBorder());
        panel.add(UfcTheme.section("HOW THE GAUNTLET WORKS"));
        panel.add(Box.createVerticalStrut(18));
        panel.add(rule("1", "Configure", "Choose difficulty, fight length, UFC era, and stat visibility."));
        panel.add(Box.createVerticalStrut(14));
        panel.add(rule("2", "Draft", "Spin for fighters and assign one source stat to each attribute."));
        panel.add(Box.createVerticalStrut(14));
        panel.add(rule("3", "Finalize", "Name the fighter, spin a division, and review the overall."));
        panel.add(Box.createVerticalStrut(14));
        panel.add(rule("4", "Fight", "Fight rank 15 through rank 1. All 15 fights are completed."));
        return panel;
    }

    private JPanel rule(String number, String title, String description) {
        final JPanel row = UfcTheme.panel(new BorderLayout(14, 0));
        final JLabel badge = UfcTheme.centeredLabel(number, UfcTheme.SECTION, UfcTheme.TEXT);
        badge.setOpaque(true);
        badge.setBackground(UfcTheme.ACCENT);
        badge.setBorder(BorderFactory.createEmptyBorder(8, 13, 8, 13));
        final JPanel text = UfcTheme.panel(null);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        final JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UfcTheme.BODY_BOLD);
        titleLabel.setForeground(UfcTheme.TEXT);
        text.add(titleLabel);
        text.add(UfcTheme.body(description));
        row.add(badge, BorderLayout.WEST);
        row.add(text, BorderLayout.CENTER);
        return row;
    }
}
