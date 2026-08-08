package view;

import entity.Attribute;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

/** User story 6 view. */
// The catalogue is placeholder UI data until actual implementation of the use case - Mahin
public final class FighterBrowserView extends JPanel {
    public FighterBrowserView(Runnable backAction) {
        setLayout(new BorderLayout());
        setBackground(UfcTheme.BACKGROUND);

        final JPanel header = UfcTheme.panel(new BorderLayout());
        header.setBackground(UfcTheme.HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(22, 32, 22, 32));
        header.add(UfcTheme.title("UFC FIGHTER BROWSER"), BorderLayout.WEST);
        header.add(UfcTheme.body("Explore the real fighters represented in 15-0"), BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        final JPanel content = UfcTheme.panel(new GridLayout(1, 2, 20, 0));
        content.setBackground(UfcTheme.BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(26, 34, 26, 34));
        content.add(createCataloguePanel());
        content.add(createProfilePanel());
        add(content, BorderLayout.CENTER);

        final JPanel footer = UfcTheme.panel(new FlowLayout(FlowLayout.CENTER, 16, 16));
        footer.setBackground(UfcTheme.HEADER);
        final JButton back = UfcTheme.secondaryButton("Back to Home");
        back.addActionListener(event -> backAction.run());
        footer.add(back);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel createCataloguePanel() {
        final JPanel panel = UfcTheme.panel(new BorderLayout(0, 14));
        panel.setBorder(UfcTheme.cardBorder());

        final JPanel top = UfcTheme.panel(null);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(UfcTheme.section("FIGHTER CATALOGUE"));
        top.add(Box.createVerticalStrut(10));
        final JTextField search = UfcTheme.textField(24);
        search.setText("Search fighters...");
        top.add(search);
        top.add(Box.createVerticalStrut(8));
        top.add(UfcTheme.body("Search/filter behaviour is connected in Stage 8."));
        panel.add(top, BorderLayout.NORTH);

        final String[] fighters = {
                "Islam Makhachev — Lightweight",
                "Jon Jones — Heavyweight",
                "Alexander Volkanovski — Featherweight",
                "Max Holloway — Featherweight",
                "Khabib Nurmagomedov — Lightweight",
                "Georges St-Pierre — Welterweight",
                "Amanda Nunes — Bantamweight",
                "Valentina Shevchenko — Flyweight",
                "Anderson Silva — Middleweight",
                "Demetrious Johnson — Flyweight",
                "José Aldo — Featherweight",
                "Kamaru Usman — Welterweight"
        };
        final JList<String> list = new JList<>(fighters);
        list.setFont(UfcTheme.BODY);
        list.setForeground(UfcTheme.TEXT);
        list.setBackground(UfcTheme.PANEL_ALT);
        list.setSelectionBackground(UfcTheme.ACCENT_DARK);
        list.setSelectionForeground(UfcTheme.TEXT);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.setFixedCellHeight(38);
        final JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createLineBorder(UfcTheme.BORDER));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createProfilePanel() {
        final JPanel panel = UfcTheme.panel(new BorderLayout());
        panel.setBorder(UfcTheme.cardBorder());

        final JPanel identity = UfcTheme.panel(null);
        identity.setLayout(new BoxLayout(identity, BoxLayout.Y_AXIS));
        final JLabel name = new JLabel("Islam Makhachev");
        name.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 38));
        name.setForeground(UfcTheme.TEXT);
        identity.add(name);
        identity.add(Box.createVerticalStrut(6));
        identity.add(UfcTheme.body("Lightweight • Modern Era • UFC Record: 17-1"));
        identity.add(Box.createVerticalStrut(6));
        identity.add(UfcTheme.body("Selected fighter profile preview"));
        panel.add(identity, BorderLayout.NORTH);

        final JPanel stats = UfcTheme.panel(null);
        stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));
        stats.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0));
        stats.add(UfcTheme.section("GAMEPLAY ATTRIBUTES"));
        stats.add(Box.createVerticalStrut(14));
        final int[] values = {88, 97, 96, 94, 91, 89};
        int index = 0;
        for (Attribute attribute : Attribute.values()) {
            final JPanel row = UfcTheme.panel(new BorderLayout(12, 0));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
            row.setBorder(BorderFactory.createEmptyBorder(7, 0, 7, 0));
            final JLabel label = UfcTheme.body(attribute.name().replace('_', ' '));
            label.setFont(UfcTheme.BODY_BOLD);
            label.setForeground(UfcTheme.TEXT);
            label.setPreferredSize(new Dimension(170, 24));
            row.add(label, BorderLayout.WEST);
            row.add(UfcTheme.statBar(values[index]), BorderLayout.CENTER);
            row.add(UfcTheme.body(Integer.toString(values[index])), BorderLayout.EAST);
            stats.add(row);
            index++;
        }
        panel.add(stats, BorderLayout.CENTER);
        return panel;
    }
}
