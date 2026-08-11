package view;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

/** Opening splash screen for the application. */
public final class SplashView extends JPanel {
    public SplashView(Runnable continueAction) {
        setLayout(new BorderLayout());
        setBackground(UfcTheme.BACKGROUND);

        final JPanel center = UfcTheme.panel(null);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(UfcTheme.BACKGROUND);
        center.setBorder(BorderFactory.createEmptyBorder(80, 40, 80, 40));

        final JLabel title = UfcTheme.centeredLabel("15-0",
                new Font(Font.SANS_SERIF, Font.BOLD, 96), UfcTheme.TEXT);
        final JLabel subtitle = UfcTheme.centeredLabel("BUILD-A-FIGHTER GAUNTLET",
                new Font(Font.SANS_SERIF, Font.BOLD, 24), UfcTheme.ACCENT);
        final JLabel note = UfcTheme.centeredLabel("Build. Fight. Go 15-0.", UfcTheme.BODY, UfcTheme.MUTED);
        final JButton enter = UfcTheme.primaryButton("Enter the Octagon");

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        note.setAlignmentX(Component.CENTER_ALIGNMENT);
        enter.setAlignmentX(Component.CENTER_ALIGNMENT);
        enter.addActionListener(event -> continueAction.run());

        center.add(Box.createVerticalGlue());
        center.add(title);
        center.add(Box.createVerticalStrut(10));
        center.add(subtitle);
        center.add(Box.createVerticalStrut(24));
        center.add(note);
        center.add(Box.createVerticalStrut(30));
        center.add(enter);
        center.add(Box.createVerticalGlue());
        add(center, BorderLayout.CENTER);
    }
}
