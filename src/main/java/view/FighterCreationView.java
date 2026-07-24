package view;


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class FighterCreationView extends JPanel {
    private final JLabel rerollsRemainingLabel;

    public FighterCreationView() {
        setLayout(new BorderLayout(20, 0));
        setBackground(Color.white);

//        rerollsRemainingLabel = createRerollsRemainingLabel();
//
//        add(rerollsRemainingLabel, BorderLayout.NORTH);
//    }


    private JPanel createYourFighterPanel() {

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