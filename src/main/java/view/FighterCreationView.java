package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import entity.Attribute;
import interface_adapter.assign_attribute.AssignAttributeController;
import interface_adapter.fighter_creation.FighterCreationViewModel;
import interface_adapter.fighter_creation.SpinFighterController;
import interface_adapter.reroll_fighter.RerollFighterController;

/** View for building a custom fighter. */
public final class FighterCreationView extends JPanel implements PropertyChangeListener {

    private static final int HEADER_VERTICAL_INSET = 20;
    private static final int PAGE_HORIZONTAL_INSET = 30;
    private static final int CONTENT_VERTICAL_INSET = 24;
    private static final int BUILD_ROW_MAX_HEIGHT = 68;
    private static final int BUILD_ROW_VERTICAL_INSET = 10;
    private static final int BUILD_ROW_HORIZONTAL_INSET = 8;
    private static final int FIGHTER_NAME_FONT_SIZE = 34;
    private static final int STAT_ROW_VERTICAL_INSET = 8;
    private static final int STAT_LABEL_WIDTH = 150;
    private static final int STAT_LABEL_HEIGHT = 24;
    private static final int EM_DASH_CODE_POINT = 0x2014;
    private static final String EM_DASH = Character.toString(EM_DASH_CODE_POINT);

    private JLabel rerollsLabel;
    private JButton spinButton;
    private JButton rerollButton;
    private JLabel fighterNameLabel;
    private JLabel fighterDetailsLabel;
    private Attribute selectedAttribute;
    private JPanel selectedRow;
    private final SpinFighterController spinFighterController;
    private final RerollFighterController rerollFighterController;
    private final AssignAttributeController assignAttributeController;
    private final FighterCreationViewModel viewModel;
    private final Map<Attribute, JLabel> assignedLabels = new HashMap<>();
    private final Map<Attribute, JProgressBar> statBars = new HashMap<>();
    private final Map<Attribute, JLabel> statLabels = new HashMap<>();
    private JLabel attributesLabel;

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
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(UfcTheme.BACKGROUND);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);

        final JPanel actions = UfcTheme.panel(new FlowLayout(FlowLayout.CENTER, 14, 16));
        actions.setBackground(UfcTheme.HEADER);

        final JButton back = UfcTheme.secondaryButton("Back to Settings");
        spinButton = UfcTheme.primaryButton("Spin Fighter");
        rerollButton = UfcTheme.secondaryButton("Reroll Fighter");
        final JButton assign = UfcTheme.primaryButton("Assign Attribute");
        final JButton next = UfcTheme.primaryButton("Continue");

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                backAction.run();
            }
        });
        spinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                spinFighterController.execute(
                        viewModel.getGameSettings().getEra()
                );
            }
        });

        rerollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                rerollFighterController.execute(
                        viewModel.getGameSettings().getEra(),
                        viewModel.getRerollsLeft(),
                        viewModel.getCurrentFighter()
                );
            }
        });

        assign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                assignAttributeController.execute(
                        viewModel.getCustomFighter(),
                        viewModel.getCurrentFighter(),
                        selectedAttribute,
                        viewModel.getGameSettings().getEra()
                );
            }
        });

        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                continueAction.run();
            }
        });

        actions.add(back);
        actions.add(spinButton);
        actions.add(rerollButton);
        actions.add(assign);
        actions.add(next);
        add(actions, BorderLayout.SOUTH);
        updateView();
    }

    private JPanel createHeaderPanel() {
        final JPanel header = UfcTheme.panel(new BorderLayout());
        header.setBackground(UfcTheme.HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(
                HEADER_VERTICAL_INSET,
                PAGE_HORIZONTAL_INSET,
                HEADER_VERTICAL_INSET,
                PAGE_HORIZONTAL_INSET));
        header.add(UfcTheme.title("BUILD YOUR FIGHTER"), BorderLayout.WEST);

        final JPanel progressPanel = UfcTheme.panel(null);
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        attributesLabel = UfcTheme.body("Attributes: 0 / 6");
        rerollsLabel = UfcTheme.body("Rerolls: 0");
        progressPanel.add(attributesLabel);
        progressPanel.add(rerollsLabel);
        header.add(progressPanel, BorderLayout.EAST);
        return header;
    }

    private JPanel createContentPanel() {
        final JPanel content = UfcTheme.panel(new GridLayout(1, 2, 20, 0));
        content.setBackground(UfcTheme.BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(
                CONTENT_VERTICAL_INSET,
                PAGE_HORIZONTAL_INSET,
                CONTENT_VERTICAL_INSET,
                PAGE_HORIZONTAL_INSET));
        content.add(createBuildPanel());
        content.add(createDraftPanel());
        return content;
    }

    private JPanel createBuildPanel() {
        final JPanel panel = UfcTheme.panel(new BorderLayout());
        panel.setBorder(UfcTheme.cardBorder());
        panel.add(UfcTheme.section("YOUR FIGHTER"), BorderLayout.NORTH);

        final JPanel rows = UfcTheme.panel(null);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));

        for (Attribute attribute : Attribute.values()) {
            final JPanel row = UfcTheme.panel(new BorderLayout(12, 0));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, BUILD_ROW_MAX_HEIGHT));
            row.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, UfcTheme.BORDER),
                    BorderFactory.createEmptyBorder(
                            BUILD_ROW_VERTICAL_INSET,
                            BUILD_ROW_HORIZONTAL_INSET,
                            BUILD_ROW_VERTICAL_INSET,
                            BUILD_ROW_HORIZONTAL_INSET)));

            final JLabel name = UfcTheme.body(attribute.getDisplayName());
            name.setFont(UfcTheme.BODY_BOLD);
            name.setForeground(UfcTheme.TEXT);

            final JLabel value = UfcTheme.body("--   " + EM_DASH);
            assignedLabels.put(attribute, value);

            row.add(name, BorderLayout.WEST);
            row.add(value, BorderLayout.EAST);
            rows.add(row);
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

        fighterNameLabel = new JLabel("Spin a fighter");
        fighterNameLabel.setFont(
                new Font(Font.SANS_SERIF, Font.BOLD, FIGHTER_NAME_FONT_SIZE)
        );
        fighterNameLabel.setForeground(UfcTheme.TEXT);

        fighterDetailsLabel = UfcTheme.body("");

        fighterInfo.add(fighterNameLabel);
        fighterInfo.add(fighterDetailsLabel);
        fighterHeader.add(fighterInfo, BorderLayout.WEST);

        panel.add(fighterHeader, BorderLayout.NORTH);

        final JPanel stats = UfcTheme.panel(null);
        stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));

        for (final Attribute attribute : Attribute.values()) {
            final JPanel row = UfcTheme.panel(new BorderLayout(10, 0));
            row.setBorder(BorderFactory.createEmptyBorder(
                    STAT_ROW_VERTICAL_INSET, 0, STAT_ROW_VERTICAL_INSET, 0));

            final JLabel label = UfcTheme.body(attribute.getDisplayName());
            label.setPreferredSize(new Dimension(STAT_LABEL_WIDTH, STAT_LABEL_HEIGHT));

            final JProgressBar bar = UfcTheme.statBar(0);
            final JLabel value = UfcTheme.body("--");

            statBars.put(attribute, bar);
            statLabels.put(attribute, value);

            row.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent event) {
                    if (selectedRow != null) {
                        selectedRow.setBorder(BorderFactory.createEmptyBorder(
                                STAT_ROW_VERTICAL_INSET, 0, STAT_ROW_VERTICAL_INSET, 0));
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
        }

        panel.add(stats, BorderLayout.CENTER);
        return panel;
    }

    private void updateView() {
        attributesLabel.setText("Attributes: " + viewModel.getAttributesFilled() + " / " + Attribute.values().length);

        rerollsLabel.setText(
                "Rerolls: " + viewModel.getRerollsLeft()
        );

        if (viewModel.isFighterRevealed()) {
            fighterNameLabel.setText(
                    viewModel.getFighterName()
            );

            fighterDetailsLabel.setText(
                    viewModel.getFighterDetails()
            );
        }
        else {
            fighterNameLabel.setText("Spin a fighter");
            fighterDetailsLabel.setText("");
        }

        for (Attribute attribute : Attribute.values()) {
            final String key = attribute.getDisplayName();

            final Integer stat =
                    viewModel.getFighterStats().get(key);

            if (stat == null) {
                statBars.get(attribute).setValue(0);
                statLabels.get(attribute).setText("--");
            }
            else {
                statBars.get(attribute).setValue(stat);
                statLabels.get(attribute).setText(
                        Integer.toString(stat)
                );
            }

            final Integer assignedValue =
                    viewModel.getAssignedValues().get(key);

            final String source =
                    viewModel.getAssignedFighters().get(key);

            if (assignedValue == null) {
                assignedLabels.get(attribute)
                        .setText("--   " + EM_DASH);
            }
            else {
                final String sourceLabel;
                if (source == null) {
                    sourceLabel = EM_DASH;
                }
                else {
                    sourceLabel = source;
                }
                assignedLabels.get(attribute).setText(
                        assignedValue
                                + "   "
                                + sourceLabel
                );
            }
        }
        spinButton.setEnabled(!viewModel.isFighterRevealed());
        rerollButton.setEnabled(
                viewModel.isFighterRevealed()
                        && viewModel.getRerollsLeft() > 0);

        revalidate();
        repaint();
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        updateView();
    }
}
