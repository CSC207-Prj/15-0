package view;

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

/** User story 4 view. Opponents and results are placeholder display data in Stage 2. */
public final class SimulationView extends JPanel {
    public SimulationView(Runnable backHomeAction, Runnable savedFightersAction) {
        setLayout(new BorderLayout());
        setBackground(UfcTheme.BACKGROUND);

        final JPanel header = UfcTheme.panel(new BorderLayout());
        header.setBackground(UfcTheme.HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        final JPanel titleBlock = UfcTheme.panel(null);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.add(UfcTheme.title("THE GAUNTLET"));
        titleBlock.add(UfcTheme.body("Lightweight • 15 ranked opponents"));
        header.add(titleBlock, BorderLayout.WEST);
        final JLabel record = UfcTheme.centeredLabel("RECORD  4-2",
                new Font(Font.SANS_SERIF, Font.BOLD, 28), UfcTheme.ACCENT);
        header.add(record, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        final JPanel content = UfcTheme.panel(new GridLayout(1, 2, 20, 0));
        content.setBackground(UfcTheme.BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));
        content.add(createOpponentList());
        content.add(createFightPanel());
        add(content, BorderLayout.CENTER);

        final JPanel actions = UfcTheme.panel(new FlowLayout(FlowLayout.CENTER, 14, 16));
        actions.setBackground(UfcTheme.HEADER);
        final JButton simulateOne = UfcTheme.primaryButton("Simulate Next Fight");
        final JButton simulateAll = UfcTheme.secondaryButton("Auto Simulate Remaining");
        final JButton saved = UfcTheme.secondaryButton("Saved Fighters");
        final JButton home = UfcTheme.secondaryButton("Home");
        saved.addActionListener(event -> savedFightersAction.run());
        home.addActionListener(event -> backHomeAction.run());
        actions.add(simulateOne);
        actions.add(simulateAll);
        actions.add(saved);
        actions.add(home);
        add(actions, BorderLayout.SOUTH);
    }

    private JScrollPane createOpponentList() {
        final JPanel list = UfcTheme.panel(null);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(UfcTheme.cardBorder());
        final JLabel title = UfcTheme.section("DIVISION TOP 15");
        title.setAlignmentX(LEFT_ALIGNMENT);
        list.add(title);
        list.add(Box.createVerticalStrut(12));
        final String[] names = {"Arman Tsarukyan", "Charles Oliveira", "Justin Gaethje", "Dustin Poirier",
                "Mateusz Gamrot", "Dan Hooker", "Beneil Dariush", "Rafael Fiziev", "Renato Moicano",
                "Jalin Turner", "Benoit Saint Denis", "Paddy Pimblett", "Grant Dawson", "Bobby Green", "Joel Alvarez"};
        for (int i = 0; i < names.length; i++) {
            final int rank = i + 1;
            final JPanel row = UfcTheme.panel(new BorderLayout(10, 0));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UfcTheme.BORDER));
            final JLabel rankLabel = UfcTheme.body("#" + rank);
            rankLabel.setPreferredSize(new Dimension(42, 42));
            final JLabel fighter = UfcTheme.body(names[i]);
            fighter.setFont(UfcTheme.BODY_BOLD);
            fighter.setForeground(UfcTheme.TEXT);
            row.add(rankLabel, BorderLayout.WEST);
            row.add(fighter, BorderLayout.CENTER);
            row.add(UfcTheme.body(rank <= 6 ? (rank % 2 == 0 ? "LOSS" : "WIN") : "UP NEXT"), BorderLayout.EAST);
            list.add(row);
        }
        return UfcTheme.scroll(list);
    }

    private JPanel createFightPanel() {
        final JPanel panel = UfcTheme.panel(new BorderLayout());
        panel.setBorder(UfcTheme.cardBorder());

        final JPanel top = UfcTheme.panel(null);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(UfcTheme.section("CURRENT MATCHUP"));
        top.add(Box.createVerticalStrut(20));
        final JLabel matchup = UfcTheme.centeredLabel("THE PROSPECT  vs  RENATO MOICANO",
                new Font(Font.SANS_SERIF, Font.BOLD, 24), UfcTheme.TEXT);
        top.add(matchup);
        top.add(Box.createVerticalStrut(8));
        top.add(UfcTheme.centeredLabel("Opponent stats may be hidden based on settings", UfcTheme.BODY, UfcTheme.MUTED));
        panel.add(top, BorderLayout.NORTH);

        final JPanel history = UfcTheme.panel(null);
        history.setLayout(new BoxLayout(history, BoxLayout.Y_AXIS));
        history.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0));
        history.add(UfcTheme.section("FIGHT HISTORY"));
        history.add(Box.createVerticalStrut(12));
        history.add(result("#15 Joel Alvarez", "WIN • KO • Round 2 • 2:14"));
        history.add(result("#14 Bobby Green", "WIN • Decision • Round 3"));
        history.add(result("#13 Grant Dawson", "LOSS • Submission • Round 2 • 3:47"));
        history.add(result("#12 Paddy Pimblett", "WIN • KO • Round 1 • 4:02"));
        history.add(result("#11 Benoit Saint Denis", "WIN • Decision • Round 3"));
        history.add(result("#10 Jalin Turner", "LOSS • KO • Round 3 • 1:18"));
        panel.add(history, BorderLayout.CENTER);

        final JLabel note = UfcTheme.body("Stage 6 will simulate all 15 fights even after losses.");
        note.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.add(note, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel result(String opponent, String outcome) {
        final JPanel row = UfcTheme.panel(new BorderLayout());
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UfcTheme.BORDER),
                BorderFactory.createEmptyBorder(11, 6, 11, 6)));
        final JLabel opponentLabel = UfcTheme.body(opponent);
        opponentLabel.setForeground(UfcTheme.TEXT);
        opponentLabel.setFont(UfcTheme.BODY_BOLD);
        row.add(opponentLabel, BorderLayout.WEST);
        row.add(UfcTheme.body(outcome), BorderLayout.EAST);
        return row;
    }
}
