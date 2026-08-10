package view;

import entity.Attribute;
import entity.UfcEra;
import entity.WeightClass;
import interface_adapter.fighter_browser.FighterBrowserController;
import interface_adapter.fighter_browser.FighterBrowserRow;
import interface_adapter.fighter_browser.FighterBrowserState;
import interface_adapter.fighter_browser.FighterBrowserViewModel;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumMap;
import java.util.Map;

/**
 * User Story 6: browse, search, filter, and inspect real UFC fighters.
 */
public final class FighterBrowserView extends JPanel
        implements PropertyChangeListener {

    private static final String ALL_DIVISIONS = "All Divisions";
    private static final String ALL_ERAS = "All Eras";

    private final FighterBrowserController controller;
    private final FighterBrowserViewModel viewModel;

    private final JTextField searchField = UfcTheme.textField(22);
    private final JComboBox<String> divisionFilter =
            UfcTheme.comboBox(buildDivisionOptions());
    private final JComboBox<String> eraFilter =
            UfcTheme.comboBox(buildEraOptions());

    private final DefaultListModel<FighterBrowserRow> listModel =
            new DefaultListModel<>();
    private final JList<FighterBrowserRow> fighterList =
            new JList<>(listModel);

    private final JLabel resultLabel = UfcTheme.body("");
    private final JLabel errorLabel = UfcTheme.body("");
    private final JLabel nameLabel = new JLabel("No fighter selected");
    private final JLabel detailsLabel = UfcTheme.body("");
    private final JLabel rankLabel = UfcTheme.body("");

    private final Map<Attribute, JProgressBar> statBars =
            new EnumMap<>(Attribute.class);
    private final Map<Attribute, JLabel> statValues =
            new EnumMap<>(Attribute.class);

    private boolean rendering;

    public FighterBrowserView(FighterBrowserController controller,
                              FighterBrowserViewModel viewModel,
                              Runnable backAction) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(UfcTheme.BACKGROUND);

        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
        add(createFooter(backAction), BorderLayout.SOUTH);

        controller.load();
    }

    private JPanel createHeader() {
        final JPanel header = UfcTheme.panel(new BorderLayout());
        header.setBackground(UfcTheme.HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(22, 32, 22, 32));
        header.add(UfcTheme.title("UFC FIGHTER BROWSER"), BorderLayout.WEST);
        header.add(
                UfcTheme.body("Search the shared fighter catalogue and inspect gameplay attributes"),
                BorderLayout.EAST);
        return header;
    }

    private JPanel createContent() {
        final JPanel content = UfcTheme.panel(new GridLayout(1, 2, 20, 0));
        content.setBackground(UfcTheme.BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(26, 34, 26, 34));
        content.add(createCataloguePanel());
        content.add(createProfilePanel());
        return content;
    }

    private JPanel createCataloguePanel() {
        final JPanel panel = UfcTheme.panel(new BorderLayout(0, 14));
        panel.setBorder(UfcTheme.cardBorder());

        final JPanel top = UfcTheme.panel(null);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(UfcTheme.section("FIGHTER CATALOGUE"));
        top.add(Box.createVerticalStrut(10));

        searchField.setToolTipText("Search by fighter name");
        searchField.addActionListener(event -> applyFilters());
        top.add(searchField);
        top.add(Box.createVerticalStrut(10));

        final JPanel filters = UfcTheme.panel(new GridLayout(1, 2, 10, 0));
        filters.add(divisionFilter);
        filters.add(eraFilter);
        top.add(filters);
        top.add(Box.createVerticalStrut(10));

        final JPanel filterButtons = UfcTheme.panel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        final JButton apply = UfcTheme.primaryButton("Apply Filters");
        final JButton clear = UfcTheme.secondaryButton("Clear");
        apply.setPreferredSize(new Dimension(150, 42));
        clear.setPreferredSize(new Dimension(110, 42));
        apply.addActionListener(event -> applyFilters());
        clear.addActionListener(event -> clearFilters());
        filterButtons.add(apply);
        filterButtons.add(clear);
        top.add(filterButtons);
        top.add(Box.createVerticalStrut(8));
        top.add(resultLabel);

        panel.add(top, BorderLayout.NORTH);

        fighterList.setFont(UfcTheme.BODY);
        fighterList.setForeground(UfcTheme.TEXT);
        fighterList.setBackground(UfcTheme.PANEL_ALT);
        fighterList.setSelectionBackground(UfcTheme.ACCENT_DARK);
        fighterList.setSelectionForeground(UfcTheme.TEXT);
        fighterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fighterList.setFixedCellHeight(42);
        fighterList.addListSelectionListener(event -> {
            if (event.getValueIsAdjusting() || rendering) {
                return;
            }
            final FighterBrowserRow selected = fighterList.getSelectedValue();
            if (selected != null) {
                controller.selectFighter(
                        searchField.getText(),
                        selectedWeightClass(),
                        selectedEra(),
                        selected.getName());
            }
        });

        final JScrollPane scroll = new JScrollPane(fighterList);
        scroll.setBorder(BorderFactory.createLineBorder(UfcTheme.BORDER));
        panel.add(scroll, BorderLayout.CENTER);

        errorLabel.setForeground(UfcTheme.WARNING);
        panel.add(errorLabel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createProfilePanel() {
        final JPanel panel = UfcTheme.panel(new BorderLayout());
        panel.setBorder(UfcTheme.cardBorder());

        final JPanel identity = UfcTheme.panel(null);
        identity.setLayout(new BoxLayout(identity, BoxLayout.Y_AXIS));

        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 38));
        nameLabel.setForeground(UfcTheme.TEXT);
        identity.add(nameLabel);
        identity.add(Box.createVerticalStrut(6));
        identity.add(detailsLabel);
        identity.add(Box.createVerticalStrut(6));
        identity.add(rankLabel);
        panel.add(identity, BorderLayout.NORTH);

        final JPanel stats = UfcTheme.panel(null);
        stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));
        stats.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0));
        stats.add(UfcTheme.section("GAMEPLAY ATTRIBUTES"));
        stats.add(Box.createVerticalStrut(14));

        for (Attribute attribute : Attribute.values()) {
            final JPanel row = UfcTheme.panel(new BorderLayout(12, 0));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
            row.setBorder(BorderFactory.createEmptyBorder(7, 0, 7, 0));

            final JLabel label = UfcTheme.body(attribute.getDisplayName());
            label.setFont(UfcTheme.BODY_BOLD);
            label.setForeground(UfcTheme.TEXT);
            label.setPreferredSize(new Dimension(170, 24));

            final JProgressBar bar = UfcTheme.statBar(0);
            final JLabel value = UfcTheme.body("--");
            statBars.put(attribute, bar);
            statValues.put(attribute, value);

            row.add(label, BorderLayout.WEST);
            row.add(bar, BorderLayout.CENTER);
            row.add(value, BorderLayout.EAST);
            stats.add(row);
        }

        panel.add(stats, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFooter(Runnable backAction) {
        final JPanel footer = UfcTheme.panel(new FlowLayout(FlowLayout.CENTER, 16, 16));
        footer.setBackground(UfcTheme.HEADER);
        final JButton back = UfcTheme.secondaryButton("Back to Home");
        back.addActionListener(event -> backAction.run());
        footer.add(back);
        return footer;
    }

    private void applyFilters() {
        controller.filter(
                searchField.getText(),
                selectedWeightClass(),
                selectedEra());
    }

    private void clearFilters() {
        searchField.setText("");
        divisionFilter.setSelectedIndex(0);
        eraFilter.setSelectedIndex(0);
        controller.load();
    }

    private WeightClass selectedWeightClass() {
        final String selected = (String) divisionFilter.getSelectedItem();
        if (selected == null || ALL_DIVISIONS.equals(selected)) {
            return null;
        }
        for (WeightClass weightClass : WeightClass.values()) {
            if (weightClass.getDisplayName().equals(selected)) {
                return weightClass;
            }
        }
        return null;
    }

    private UfcEra selectedEra() {
        final String selected = (String) eraFilter.getSelectedItem();
        if (selected == null || ALL_ERAS.equals(selected)) {
            return UfcEra.ALL_TIME;
        }
        for (UfcEra era : UfcEra.values()) {
            if (era != UfcEra.ALL_TIME
                    && era.getDisplayName().equals(selected)) {
                return era;
            }
        }
        return UfcEra.ALL_TIME;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        render(viewModel.getState());
    }

    private void render(FighterBrowserState state) {
        rendering = true;
        final String previouslySelected = fighterList.getSelectedValue() == null
                ? null
                : fighterList.getSelectedValue().getName();

        listModel.clear();
        for (FighterBrowserRow row : state.getRows()) {
            listModel.addElement(row);
        }

        resultLabel.setText(state.getResultText());
        errorLabel.setText(state.getErrorMessage());
        nameLabel.setText(state.getSelectedName());
        detailsLabel.setText(state.getSelectedDetails());
        rankLabel.setText(state.getRankText());

        for (Attribute attribute : Attribute.values()) {
            final Integer value = state.getAttributes().get(attribute);
            final int numericValue = value == null ? 0 : value;
            statBars.get(attribute).setValue(numericValue);
            statValues.get(attribute).setText(
                    value == null ? "--" : Integer.toString(value));
        }

        if (!listModel.isEmpty()) {
            int selectedIndex = 0;
            for (int index = 0; index < listModel.size(); index++) {
                final FighterBrowserRow row = listModel.get(index);
                if (row.getName().equals(state.getSelectedName())
                        || row.getName().equals(previouslySelected)) {
                    selectedIndex = index;
                    if (row.getName().equals(state.getSelectedName())) {
                        break;
                    }
                }
            }
            fighterList.setSelectedIndex(selectedIndex);
        }

        rendering = false;
    }

    private static String[] buildDivisionOptions() {
        final String[] options = new String[WeightClass.values().length + 1];
        options[0] = ALL_DIVISIONS;
        int index = 1;
        for (WeightClass weightClass : WeightClass.values()) {
            options[index] = weightClass.getDisplayName();
            index++;
        }
        return options;
    }

    private static String[] buildEraOptions() {
        int count = 1;
        for (UfcEra era : UfcEra.values()) {
            if (era != UfcEra.ALL_TIME) {
                count++;
            }
        }

        final String[] options = new String[count];
        options[0] = ALL_ERAS;
        int index = 1;
        for (UfcEra era : UfcEra.values()) {
            if (era != UfcEra.ALL_TIME) {
                options[index] = era.getDisplayName();
                index++;
            }
        }
        return options;
    }
}
