package view;

import entity.Attribute;
import entity.CustomFighter;
import entity.GameSettings;
import interface_adapter.fighter_creation.AssignAttributeController;
import interface_adapter.fighter_creation.FighterCreationViewModel;
import interface_adapter.fighter_creation.LoadFighterCreationController;
import interface_adapter.fighter_creation.RerollFighterController;
import interface_adapter.fighter_creation.SpinFighterController;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumMap;
import java.util.Map;

/**
 * View for building a custom fighter.
 *
 * All drafting actions are delegated to controllers. The view only renders
 * FighterCreationViewModel state.
 */
public final class FighterCreationView extends JPanel
        implements PropertyChangeListener {

    private final SpinFighterController spinFighterController;
    private final RerollFighterController rerollFighterController;
    private final AssignAttributeController assignAttributeController;
    private final LoadFighterCreationController loadController;
    private final FighterCreationViewModel viewModel;

    private Attribute selectedAttribute;
    private JPanel selectedRow;

    private final JLabel attributesProgress =
            UfcTheme.body("Attributes: 0 / 6");
    private final JLabel rerollsLabel =
            UfcTheme.body("Rerolls: 0");

    private final Map<Attribute, JLabel> assignedLabels =
            new EnumMap<>(Attribute.class);
    private final Map<Attribute, JPanel> statRows =
            new EnumMap<>(Attribute.class);
    private final Map<Attribute, JProgressBar> statBars =
            new EnumMap<>(Attribute.class);
    private final Map<Attribute, JLabel> statValues =
            new EnumMap<>(Attribute.class);

    private final JLabel fighterName =
            new JLabel("Spin a Fighter");
    private final JLabel fighterDetails =
            UfcTheme.body("Choose a source fighter to begin.");
    private final JLabel overall =
            UfcTheme.centeredLabel(
                    "--",
                    new Font(Font.SANS_SERIF, Font.BOLD, 38),
                    UfcTheme.ACCENT
            );
    private final JLabel errorLabel =
            UfcTheme.body("");

    private final JButton spinButton =
            UfcTheme.primaryButton("Spin Fighter");
    private final JButton rerollButton =
            UfcTheme.secondaryButton("Reroll Fighter");
    private final JButton assignButton =
            UfcTheme.primaryButton("Assign Attribute");
    private final JButton nextButton =
            UfcTheme.primaryButton("Continue");

    public FighterCreationView(
            SpinFighterController spinFighterController,
            RerollFighterController rerollFighterController,
            AssignAttributeController assignAttributeController,
            LoadFighterCreationController loadController,
            FighterCreationViewModel viewModel,
            Runnable backAction,
            Runnable continueAction) {

        this.spinFighterController =
                spinFighterController;
        this.rerollFighterController =
                rerollFighterController;
        this.assignAttributeController =
                assignAttributeController;
        this.loadController = loadController;
        this.viewModel = viewModel;

        viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(UfcTheme.BACKGROUND);

        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
        add(
                createActions(backAction, continueAction),
                BorderLayout.SOUTH
        );

        render();
    }

    /**
     * Called by application navigation immediately after US1 successfully
     * configures a new run.
     */
    public void loadConfiguredRun() {
        loadController.execute();
    }

    private JPanel createHeader() {
        final JPanel header =
                UfcTheme.panel(new BorderLayout());
        header.setBackground(UfcTheme.HEADER);
        header.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 30, 20, 30
                )
        );
        header.add(
                UfcTheme.title("BUILD YOUR FIGHTER"),
                BorderLayout.WEST
        );

        final JPanel progressPanel =
                UfcTheme.panel(null);
        progressPanel.setLayout(
                new BoxLayout(
                        progressPanel,
                        BoxLayout.Y_AXIS
                )
        );
        progressPanel.add(attributesProgress);
        progressPanel.add(rerollsLabel);
        header.add(
                progressPanel,
                BorderLayout.EAST
        );
        return header;
    }

    private JPanel createContent() {
        final JPanel content =
                UfcTheme.panel(
                        new GridLayout(1, 2, 20, 0)
                );
        content.setBackground(UfcTheme.BACKGROUND);
        content.setBorder(
                BorderFactory.createEmptyBorder(
                        24, 30, 24, 30
                )
        );
        content.add(createBuildPanel());
        content.add(createDraftPanel());
        return content;
    }

    private JPanel createBuildPanel() {
        final JPanel panel =
                UfcTheme.panel(new BorderLayout());
        panel.setBorder(UfcTheme.cardBorder());
        panel.add(
                UfcTheme.section("YOUR FIGHTER"),
                BorderLayout.NORTH
        );

        final JPanel rows =
                UfcTheme.panel(null);
        rows.setLayout(
                new BoxLayout(rows, BoxLayout.Y_AXIS)
        );

        for (Attribute attribute : Attribute.values()) {
            final JPanel row =
                    UfcTheme.panel(
                            new BorderLayout(12, 0)
                    );
            row.setMaximumSize(
                    new Dimension(
                            Integer.MAX_VALUE,
                            68
                    )
            );
            row.setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(
                                    0, 0, 1, 0,
                                    UfcTheme.BORDER
                            ),
                            BorderFactory.createEmptyBorder(
                                    10, 8, 10, 8
                            )
                    )
            );

            final JLabel name =
                    UfcTheme.body(
                            attribute.getDisplayName()
                    );
            name.setFont(UfcTheme.BODY_BOLD);
            name.setForeground(UfcTheme.TEXT);

            final JLabel value =
                    UfcTheme.body("--   —");
            assignedLabels.put(attribute, value);

            row.add(name, BorderLayout.WEST);
            row.add(value, BorderLayout.EAST);
            rows.add(row);
        }

        panel.add(rows, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDraftPanel() {
        final JPanel panel =
                UfcTheme.panel(new BorderLayout());
        panel.setBorder(UfcTheme.cardBorder());

        final JPanel fighterHeader =
                UfcTheme.panel(new BorderLayout());

        final JPanel fighterInfo =
                UfcTheme.panel(null);
        fighterInfo.setLayout(
                new BoxLayout(
                        fighterInfo,
                        BoxLayout.Y_AXIS
                )
        );

        fighterName.setFont(
                new Font(
                        Font.SANS_SERIF,
                        Font.BOLD,
                        34
                )
        );
        fighterName.setForeground(UfcTheme.TEXT);

        fighterInfo.add(fighterName);
        fighterInfo.add(fighterDetails);
        fighterInfo.add(Box.createVerticalStrut(6));
        errorLabel.setForeground(UfcTheme.ACCENT);
        fighterInfo.add(errorLabel);

        fighterHeader.add(
                fighterInfo,
                BorderLayout.WEST
        );
        fighterHeader.add(
                overall,
                BorderLayout.EAST
        );
        panel.add(
                fighterHeader,
                BorderLayout.NORTH
        );

        final JPanel stats =
                UfcTheme.panel(null);
        stats.setLayout(
                new BoxLayout(stats, BoxLayout.Y_AXIS)
        );

        for (final Attribute attribute
                : Attribute.values()) {
            final JPanel row =
                    UfcTheme.panel(
                            new BorderLayout(10, 0)
                    );
            row.setBorder(
                    BorderFactory.createEmptyBorder(
                            8, 0, 8, 0
                    )
            );

            final JLabel label =
                    UfcTheme.body(
                            attribute.getDisplayName()
                    );
            label.setPreferredSize(
                    new Dimension(150, 24)
            );

            final JProgressBar bar =
                    UfcTheme.statBar(0);
            final JLabel value =
                    UfcTheme.body("--");

            statRows.put(attribute, row);
            statBars.put(attribute, bar);
            statValues.put(attribute, value);

            row.addMouseListener(
                    new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(
                                java.awt.event.MouseEvent event) {
                            selectAttribute(
                                    attribute,
                                    row
                            );
                        }
                    }
            );

            row.add(label, BorderLayout.WEST);
            row.add(bar, BorderLayout.CENTER);
            row.add(value, BorderLayout.EAST);
            stats.add(row);
        }

        panel.add(stats, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createActions(
            Runnable backAction,
            Runnable continueAction) {

        final JPanel actions =
                UfcTheme.panel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                14,
                                16
                        )
                );
        actions.setBackground(UfcTheme.HEADER);

        final JButton back =
                UfcTheme.secondaryButton(
                        "Back to Settings"
                );

        back.addActionListener(
                event -> backAction.run()
        );

        spinButton.addActionListener(event -> {
            final GameSettings settings =
                    viewModel.getGameSettings();
            final CustomFighter customFighter =
                    viewModel.getCustomFighter();

            if (settings != null
                    && customFighter != null) {
                clearSelection();
                spinFighterController.execute(
                        settings.getEra(),
                        customFighter
                );
            }
        });

        rerollButton.addActionListener(event -> {
            final GameSettings settings =
                    viewModel.getGameSettings();

            if (settings != null) {
                clearSelection();
                rerollFighterController.execute(
                        settings.getEra(),
                        viewModel.getRerollsLeft(),
                        viewModel.getCurrentFighter(),
                        viewModel.getCustomFighter()
                );
            }
        });

        assignButton.addActionListener(event -> {
            final GameSettings settings =
                    viewModel.getGameSettings();

            if (settings != null) {
                assignAttributeController.execute(
                        viewModel.getCustomFighter(),
                        viewModel.getCurrentFighter(),
                        selectedAttribute,
                        settings.getEra()
                );
            }
        });

        nextButton.addActionListener(event -> {
            final CustomFighter customFighter =
                    viewModel.getCustomFighter();

            if (customFighter != null
                    && customFighter.hasAllAttributes()) {
                continueAction.run();
            }
            else {
                viewModel.setErrorMessage(
                        "Assign all six attributes before continuing."
                );
            }
        });

        actions.add(back);
        actions.add(spinButton);
        actions.add(rerollButton);
        actions.add(assignButton);
        actions.add(nextButton);

        return actions;
    }

    private void selectAttribute(
            Attribute attribute,
            JPanel row) {

        if (!viewModel.isFighterRevealed()
                || viewModel.getFighterStats().get(
                        attribute.getDisplayName()
                ) == null) {
            return;
        }

        if (viewModel.getCustomFighter() != null
                && viewModel.getCustomFighter()
                        .hasAttribute(attribute)) {
            return;
        }

        clearSelection();

        selectedAttribute = attribute;
        selectedRow = row;
        selectedRow.setBorder(
                BorderFactory.createLineBorder(
                        Color.WHITE,
                        2
                )
        );
        renderButtonStates();
    }

    private void clearSelection() {
        if (selectedRow != null) {
            selectedRow.setBorder(
                    BorderFactory.createEmptyBorder(
                            8, 0, 8, 0
                    )
            );
        }
        selectedRow = null;
        selectedAttribute = null;
    }

    @Override
    public void propertyChange(
            PropertyChangeEvent event) {
        clearSelection();
        render();
    }

    private void render() {
        attributesProgress.setText(
                "Attributes: "
                        + viewModel.getAttributesFilled()
                        + " / "
                        + Attribute.values().length
        );
        rerollsLabel.setText(
                "Rerolls: "
                        + viewModel.getRerollsLeft()
        );

        for (Attribute attribute
                : Attribute.values()) {
            final String key =
                    attribute.getDisplayName();

            final Integer assignedValue =
                    viewModel.getAssignedValues()
                            .get(key);
            final String source =
                    viewModel.getAssignedFighters()
                            .get(key);

            if (assignedValue == null) {
                assignedLabels.get(attribute)
                        .setText("--   —");
            }
            else {
                assignedLabels.get(attribute)
                        .setText(
                                assignedValue
                                        + "   "
                                        + (source == null
                                        ? "—"
                                        : source)
                        );
            }
        }

        if (viewModel.isFighterRevealed()) {
            fighterName.setText(
                    viewModel.getFighterName()
            );
            fighterDetails.setText(
                    viewModel.getFighterDetails()
            );
        }
        else {
            fighterName.setText("Spin a Fighter");
            fighterDetails.setText(
                    "Choose a source fighter to continue."
            );
        }

        int total = 0;
        int count = 0;

        for (Attribute attribute
                : Attribute.values()) {
            final Integer value =
                    viewModel.getFighterStats()
                            .get(
                                    attribute.getDisplayName()
                            );

            final int displayValue =
                    value == null ? 0 : value;

            statBars.get(attribute)
                    .setValue(displayValue);
            statValues.get(attribute)
                    .setText(
                            value == null
                                    ? "--"
                                    : Integer.toString(value)
                    );

            if (value != null) {
                total += value;
                count++;
            }
        }

        overall.setText(
                count == 0
                        ? "--"
                        : Integer.toString(
                                Math.round(
                                        (float) total / count
                                )
                        )
        );

        errorLabel.setText(
                viewModel.getErrorMessage()
        );

        renderButtonStates();
        revalidate();
        repaint();
    }

    private void renderButtonStates() {
        final CustomFighter customFighter =
                viewModel.getCustomFighter();

        final boolean configured =
                viewModel.getGameSettings() != null
                        && customFighter != null;

        final boolean complete =
                customFighter != null
                        && customFighter.hasAllAttributes();

        final boolean hasCurrent =
                viewModel.getCurrentFighter() != null;

        spinButton.setEnabled(
                configured
                        && !complete
                        && !hasCurrent
        );

        rerollButton.setEnabled(
                configured
                        && !complete
                        && hasCurrent
                        && viewModel.getRerollsLeft() > 0
        );

        assignButton.setEnabled(
                configured
                        && !complete
                        && hasCurrent
                        && selectedAttribute != null
                        && !customFighter.hasAttribute(
                                selectedAttribute
                        )
        );

        nextButton.setEnabled(complete);
    }
}
