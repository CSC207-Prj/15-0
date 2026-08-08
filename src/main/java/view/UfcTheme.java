package view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;

/** Shared dark-red visual system used by every Swing screen. */
public final class UfcTheme {
    public static final Color BACKGROUND = new Color(0x151515);
    public static final Color PANEL = new Color(0x242424);
    public static final Color PANEL_ALT = new Color(0x2D2D2D);
    public static final Color HEADER = new Color(0x1B1B1B);
    public static final Color ACCENT = new Color(0xE0361B);
    public static final Color ACCENT_DARK = new Color(0xA72513);
    public static final Color TEXT = new Color(0xF4F4F4);
    public static final Color MUTED = new Color(0xBEBEBE);
    public static final Color BORDER = new Color(0x515151);
    public static final Color SUCCESS = new Color(0x59B36A);
    public static final Color WARNING = new Color(0xE6A23C);

    public static final Font HERO = new Font(Font.SANS_SERIF, Font.BOLD, 54);
    public static final Font TITLE = new Font(Font.SANS_SERIF, Font.BOLD, 34);
    public static final Font SECTION = new Font(Font.SANS_SERIF, Font.BOLD, 22);
    public static final Font BODY = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
    public static final Font BODY_BOLD = new Font(Font.SANS_SERIF, Font.BOLD, 16);
    public static final Font SMALL = new Font(Font.SANS_SERIF, Font.PLAIN, 13);

    private UfcTheme() {
    }

    public static JPanel panel(LayoutManager layout) {
        final JPanel panel = new JPanel(layout);
        panel.setBackground(PANEL);
        panel.setForeground(TEXT);
        return panel;
    }

    public static JLabel title(String text) {
        final JLabel label = new JLabel(text);
        label.setFont(TITLE);
        label.setForeground(TEXT);
        return label;
    }

    public static JLabel section(String text) {
        final JLabel label = new JLabel(text);
        label.setFont(SECTION);
        label.setForeground(TEXT);
        return label;
    }

    public static JLabel body(String text) {
        final JLabel label = new JLabel(text);
        label.setFont(BODY);
        label.setForeground(MUTED);
        return label;
    }

    public static JButton primaryButton(String text) {
        return button(text, ACCENT, TEXT);
    }

    public static JButton secondaryButton(String text) {
        return button(text, PANEL_ALT, TEXT);
    }

    public static JButton dangerButton(String text) {
        return button(text, ACCENT_DARK, TEXT);
    }

    private static JButton button(String text, Color background, Color foreground) {
        final JButton button = new JButton(text);
        button.setFont(BODY_BOLD);
        button.setForeground(foreground);
        button.setBackground(background);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(190, 46));
        button.setMinimumSize(new Dimension(120, 42));
        return button;
    }

    public static JTextField textField(int columns) {
        final JTextField field = new JTextField(columns);
        field.setFont(BODY);
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setBackground(PANEL_ALT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(9, 10, 9, 10)));
        return field;
    }

    public static <T> JComboBox<T> comboBox(T[] values) {
        final JComboBox<T> combo = new JComboBox<>(values);
        combo.setFont(BODY);
        combo.setForeground(TEXT);
        combo.setBackground(PANEL_ALT);
        combo.setFocusable(false);
        return combo;
    }

    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(18, 18, 18, 18));
    }

    public static JProgressBar statBar(double value) {
        final JProgressBar bar = new JProgressBar(0, 100);
        bar.setUI(new BasicProgressBarUI());
        bar.setValue((int) Math.round(value));
        bar.setForeground(ACCENT);
        bar.setBackground(new Color(0x3A3A3A));
        bar.setBorder(BorderFactory.createLineBorder(BORDER));
        bar.setOpaque(true);
        bar.setPreferredSize(new Dimension(280, 14));
        return bar;
    }

    public static JScrollPane scroll(Component component) {
        final JScrollPane scroll = new JScrollPane(component);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(PANEL);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    public static JLabel centeredLabel(String text, Font font, Color color) {
        final JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        label.setForeground(color);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
}
