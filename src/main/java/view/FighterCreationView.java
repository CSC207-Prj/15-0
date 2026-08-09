package view;

import entity.Attribute;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import interface_adapter.fighter_creation.AssignAttributeController;
import interface_adapter.fighter_creation.FighterCreationViewModel;
import interface_adapter.fighter_creation.RerollFighterController;
import interface_adapter.fighter_creation.SpinFighterController;

/** View for building a custom fighter. */
public final class FighterCreationView extends JPanel {

    private Attribute selectedAttribute;
    private JPanel selectedRow;
    private final SpinFighterController spinFighterController;
    private final RerollFighterController rerollFighterController;
    private final AssignAttributeController assignAttributeController;
    private final FighterCreationViewModel viewModel;

    public FighterCreationView(SpinFighterController spinFighterController,
        RerollFighterController rerollFighterController,
        AssignAttributeController assignAttributeController,
        FighterCreationViewModel viewModel,
        Runnable backAction,
        Runnable continueAction) {

        this.spinFighterController = spinFighterController;
        this.rerollFighterController = rerollFighterController;
        this.assignAttributeController = assignAttributeController;
        this.viewModel = viewModel;
        setLayout(new BorderLayout());
        setBackground(UfcTheme.BACKGROUND);

        final JPanel header = UfcTheme.panel(new BorderLayout());
        header.setBackground(UfcTheme.HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        header.add(UfcTheme.title("BUILD YOUR FIGHTER"), BorderLayout.WEST);

        final JPanel progressPanel = UfcTheme.panel(null);
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        progressPanel.add(UfcTheme.body("Attributes: 3 / 6"));
        progressPanel.add(UfcTheme.body("Rerolls: 1"));
        header.add(progressPanel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        final JPanel content = UfcTheme.panel(new GridLayout(1, 2, 20, 0));
        content.setBackground(UfcTheme.BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));
        content.add(createBuildPanel());
        content.add(createDraftPanel());
        add(content, BorderLayout.CENTER);

        final JPanel actions = UfcTheme.panel(new FlowLayout(FlowLayout.CENTER, 14, 16));
        actions.setBackground(UfcTheme.HEADER);

        final JButton back = UfcTheme.secondaryButton("Back to Settings");
        final JButton spin = UfcTheme.primaryButton("Spin Fighter");
        final JButton reroll = UfcTheme.secondaryButton("Reroll Fighter");
        final JButton assign = UfcTheme.primaryButton("Assign Attribute");
        final JButton next = UfcTheme.primaryButton("Continue");

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                backAction.run();
            }
        });

        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                continueAction.run();
            }
        });

        actions.add(back);
        actions.add(spin);
        actions.add(reroll);
        actions.add(assign);
        actions.add(next);
        add(actions, BorderLayout.SOUTH);
    }

    private JPanel createBuildPanel() {
        final JPanel panel = UfcTheme.panel(new BorderLayout());
        panel.setBorder(UfcTheme.cardBorder());
        panel.add(UfcTheme.section("YOUR FIGHTER"), BorderLayout.NORTH);

        final JPanel rows = UfcTheme.panel(null);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));

        final String[] sources = {
                "Max Holloway", "Khabib Nurmagomedov", "Georges St-Pierre", "—", "—", "—"
        };
        final int[] values = {91, 95, 94, 0, 0, 0};

        int index = 0;
        for (Attribute attribute : Attribute.values()) {
            final JPanel row = UfcTheme.panel(new BorderLayout(12, 0));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
            row.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, UfcTheme.BORDER),
                    BorderFactory.createEmptyBorder(10, 8, 10, 8)));

            final JLabel name = UfcTheme.body(attribute.getDisplayName());
            name.setFont(UfcTheme.BODY_BOLD);
            name.setForeground(UfcTheme.TEXT);

            final String valueText = values[index] == 0 ? "--" : Integer.toString(values[index]);
            final JLabel value = UfcTheme.body(valueText + "   " + sources[index]);

            row.add(name, BorderLayout.WEST);
            row.add(value, BorderLayout.EAST);
            rows.add(row);
            index++;
        }

        panel.add(rows, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDraftPanel() {
        final JPanel panel = UfcTheme.panel(new BorderLayout());
        panel.setBorder(UfcTheme.cardBorder());

        final JPanel fighterHeader = UfcTheme.panel(new BorderLayout());
        final JPanel fighterInfo = UfcTheme.panel(null);
        fighterInfo.setLayout(new BoxLayout(fighterInfo, BoxLayout.Y_AXIS));

        final JLabel fighter = new JLabel("Islam Makhachev");
        fighter.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 34));
        fighter.setForeground(UfcTheme.TEXT);

        fighterInfo.add(fighter);
        fighterInfo.add(UfcTheme.body("Lightweight • 28-1 • Modern Era"));
        fighterHeader.add(fighterInfo, BorderLayout.WEST);

        final JLabel overall = UfcTheme.centeredLabel(
                "96", new Font(Font.SANS_SERIF, Font.BOLD, 38), UfcTheme.ACCENT);
        fighterHeader.add(overall, BorderLayout.EAST);
        panel.add(fighterHeader, BorderLayout.NORTH);

        final JPanel stats = UfcTheme.panel(null);
        stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));

        final int[] values = {88, 97, 96, 74, 70, 89};
        int index = 0;

        for (final Attribute attribute : Attribute.values()) {
            final JPanel row = UfcTheme.panel(new BorderLayout(10, 0));
            row.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

            final JLabel label = UfcTheme.body(attribute.getDisplayName());
            label.setPreferredSize(new Dimension(150, 24));

            final JProgressBar bar = UfcTheme.statBar(values[index]);
            final JLabel value = UfcTheme.body(Integer.toString(values[index]));

            row.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent event) {
                    if (selectedRow != null) {
                        selectedRow.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
                    }

                    selectedAttribute = attribute;
                    selectedRow = row;
                    row.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                }
            });

            row.add(label, BorderLayout.WEST);
            row.add(bar, BorderLayout.CENTER);
            row.add(value, BorderLayout.EAST);
            stats.add(row);
            index++;
        }

        panel.add(stats, BorderLayout.CENTER);
        return panel;
    }
}