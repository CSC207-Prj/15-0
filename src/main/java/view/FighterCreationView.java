package view;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class FighterCreationView extends JPanel {
    private final JLabel rerollsRemainingLabel;
    private final JPanel fighterPanel;

    public FighterCreationView() {
        setLayout(new BorderLayout(20, 0));
        setBackground(Color.white);

        fighterPanel = createYourFighterPanel();
        add(fighterPanel, BorderLayout.WEST);

        rerollsRemainingLabel = createRerollsRemainingLabel();
        add(rerollsRemainingLabel, BorderLayout.NORTH);
    }

    /**
     * Creates the left side of the fighter creation screen.
     * This contains the stats of the player's fighter and progress.
     */
    private JPanel createYourFighterPanel() {
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        return panel;
    }

    private JPanel createFighterProgressPanel()  {
        JPanel panel = new JPanel();
        return panel;
    }

    private JPanel createAttributeTablePanel() {
        JPanel panel = new JPanel();
        return panel;
    }

    private JPanel createAttributePanel() {
        JPanel panel = new JPanel();
        return panel;
    }

    /**
     * Creates the fighter draft panel on the right side of the screen.
     * The player spins the for a fighter and assigns their attributes
     */
    private JPanel createFighterDraftPanel() {
        JPanel panel = new JPanel();
        return panel;
    }

    private JPanel createDraftHeaderPanel(){
        JPanel panel = new JPanel();
        return panel;
    }

    private JPanel createAttributeHeaderPanel() {
        JPanel panel = new JPanel();
        return panel;
    }

    private JPanel CreatedraftContentPanel(){
        JPanel panel = new JPanel();
        return panel;
    }

    private JPanel createPreRollPanel() {
        JPanel panel = new JPanel();
        return panel;
    }

    private JPanel createFighterCardPanel() {
        JPanel panel = new JPanel();
        return panel;
    }

    private JPanel createFighterHeaderPanel() {
        JPanel panel = new JPanel();
        return panel;
    }

    private JPanel createFighterStatsTablePanel() {
        JPanel panel = new JPanel();
        return panel;
    }

    private JPanel createFighterStatsPanel() {
        JPanel panel = new JPanel();
        return panel;
    }

    private JPanel createBuildActionsPanel() {
        JPanel panel = new JPanel();
        return panel;
    }










    // Displays the total number of rerolls the player has prior to spinning (Based on the difficulty selected)
    private JLabel createRerollsRemainingLabel() {
        JLabel label = new JLabel("Total Rerolls: 3");

        label.setForeground(new Color(240, 240, 240));
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setBackground(new Color(0x36393E));
        label.setOpaque(true);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setHorizontalAlignment(JLabel.CENTER);

        return label;
    }



    public static void main(String[] args){

        JFrame frame = new JFrame("Fighter Creation Preview");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new FighterCreationView());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);


        frame.setVisible(true);
    }
}