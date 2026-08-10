package view;

import interface_adapter.confirm_fighter.ConfirmController;
import interface_adapter.confirm_fighter.ConfirmState;
import interface_adapter.confirm_fighter.ConfirmViewModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.*;

/**
 *Naming, spin, and rating controls are visual placeholders for now.
 */
public final class ConfirmView extends JPanel implements PropertyChangeListener {
    private final ConfirmController confirmController;
    private final ConfirmViewModel confirmViewModel;
    private final Runnable continueAction;
    private static final String[] ATTRIBUTE_NAMES = {"STRIKING", "DEFENSE", "TAKEDOWNS", "HEIGHT", "REACH", "CARDIO"};
    private final JLabel[] attributeValueLabels = new JLabel[ATTRIBUTE_NAMES.length];
    private final JTextField name = UfcTheme.textField(24);
    private final JButton spin = UfcTheme.primaryButton("Spin Weight Class");
    private final JButton confirm = UfcTheme.primaryButton("Confirm Fighter");
    private final JLabel division = UfcTheme.centeredLabel("TBD", new Font(Font.SANS_SERIF, Font.BOLD, 30), UfcTheme.TEXT);
    private final JLabel overall = UfcTheme.centeredLabel("--", new Font(Font.SANS_SERIF, Font.BOLD, 58), UfcTheme.ACCENT);


    public ConfirmView(ConfirmController confirmController, ConfirmViewModel confirmViewModel,Runnable backAction, Runnable continueAction) {
        this.confirmController = confirmController;
        this.confirmViewModel = confirmViewModel;
        this.continueAction = continueAction;

        setLayout(new BorderLayout());
        setBackground(UfcTheme.BACKGROUND);
        this.confirmViewModel.addPropertyChangeListener(this);
        final JPanel header = UfcTheme.panel(new BorderLayout());
        header.setBackground(UfcTheme.HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(22, 32, 22, 32));
        header.add(UfcTheme.title("FIGHTER OVERVIEW"), BorderLayout.WEST);
        header.add(UfcTheme.body(
                        "Review the build before entering the ranked gauntlet"),
                BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        final JPanel content = UfcTheme.panel(new GridLayout(1, 2, 24, 0));
        content.setBackground(UfcTheme.BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(28, 38, 28, 38));
        content.add(createFighterPanel());
        content.add(createDivisionPanel());
        add(content, BorderLayout.CENTER);

        final JPanel actions = UfcTheme.panel( new FlowLayout(FlowLayout.CENTER, 18, 16));
        actions.setBackground(UfcTheme.HEADER);

        final JButton back = UfcTheme.secondaryButton("Back to Draft");


        back.addActionListener(event -> backAction.run());
        spin.addActionListener(event -> {
            final ConfirmState state = confirmViewModel.getState();
            confirmController.spin(name.getText(), state.getAttributePoints(), state.getWeightClass());
        });
        confirm.addActionListener(event -> {
            final ConfirmState state = confirmViewModel.getState();
            confirmController.confirm(name.getText(), state.getAttributePoints(), state.getWeightClass());
        });

        actions.add(back);
        actions.add(spin);
        actions.add(confirm);
        add(actions, BorderLayout.SOUTH);
        updateView(confirmViewModel.getState());
    }

    private JPanel createFighterPanel() {
        final JPanel panel = UfcTheme.panel(new BorderLayout());
        panel.setBorder(UfcTheme.cardBorder());

        final JPanel namePanel = UfcTheme.panel(null);
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.add(UfcTheme.section("NAME YOUR FIGHTER"));
        namePanel.add(Box.createVerticalStrut(12));

        namePanel.add(name);
        namePanel.add(Box.createVerticalStrut(22));
        namePanel.add(UfcTheme.section("SIX-ATTRIBUTE BUILD"));
        panel.add(namePanel, BorderLayout.NORTH);

        final JPanel list = UfcTheme.panel(null);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));

        for (int index = 0; index < ATTRIBUTE_NAMES.length; index++) {
            final JPanel row = UfcTheme.panel(new BorderLayout(12, 0));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
            row.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UfcTheme.BORDER), BorderFactory.createEmptyBorder(10, 8, 10, 8)));
            final JLabel label = UfcTheme.body(ATTRIBUTE_NAMES[index]);
            label.setFont(UfcTheme.BODY_BOLD);
            label.setForeground(UfcTheme.TEXT);
            row.add(label, BorderLayout.WEST);
            attributeValueLabels[index] = UfcTheme.body("TBD");
            row.add(attributeValueLabels[index], BorderLayout.EAST);

            list.add(row);
        }

        panel.add(list, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDivisionPanel() {
        final JPanel panel = UfcTheme.panel(null);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(UfcTheme.cardBorder());

        final JLabel wheelTitle = UfcTheme.centeredLabel(
                "WEIGHT-CLASS WHEEL", UfcTheme.SECTION, UfcTheme.TEXT);
        final JLabel wheelHelp = UfcTheme.centeredLabel(
                "One spin will lock the division in a later stage.",
                UfcTheme.BODY, UfcTheme.MUTED);
        // division and overall are fields so propertyChange can update them.
        panel.add(Box.createVerticalGlue());
        panel.add(wheelTitle);
        panel.add(Box.createVerticalStrut(8));
        panel.add(wheelHelp);
        panel.add(Box.createVerticalStrut(34));
        panel.add(division);
        panel.add(Box.createVerticalStrut(42));
        panel.add(UfcTheme.centeredLabel("WEIGHTED OVERALL", UfcTheme.BODY_BOLD, UfcTheme.MUTED));
        panel.add(overall);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateView(confirmViewModel.getState());
    }

    private void updateView(ConfirmState state) {
        final List<String> attributes = state.getAttributePoints();

        for (int index = 0; index < ATTRIBUTE_NAMES.length; index++) {
            String value = "TBD";

            if (attributes != null
                    && index < attributes.size()
                    && attributes.get(index) != null) {
                value = attributes.get(index);
            }
            attributeValueLabels[index].setText(value);
        }

        if (state.getWeightClass() == null) {
            division.setText("TBD");
        } else {
            division.setText(state.getWeightClass());
        }

        if (state.getOverall() == null) {
            overall.setText("--");
        } else {
            overall.setText(state.getOverall());
        }

        spin.setEnabled(!state.isWeightClassLocked());
        confirm.setEnabled(state.isWeightClassLocked()
                && !state.isConfirmed());

        if (state.getErrorMessage() != null) {
            JOptionPane.showMessageDialog(this, state.getErrorMessage(), "Confirm Fighter", JOptionPane.ERROR_MESSAGE);
        }

        if (state.isConfirmed()) {
            continueAction.run();
        }
    }
}