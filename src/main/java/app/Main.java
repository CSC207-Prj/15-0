package app;

import interface_adapter.ViewManagerModel;
import view.ViewManager;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Dimension;

/** Stage 1 application entry point with only the shared window/navigation shell. */
public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final ViewManagerModel viewManagerModel = new ViewManagerModel();
            final ViewManager viewManager = new ViewManager(viewManagerModel);

            final JFrame frame = new JFrame("15-0: Build-A-Fighter Gauntlet");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(viewManager);
            frame.setMinimumSize(new Dimension(1200, 760));
            frame.setSize(1500, 900);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
