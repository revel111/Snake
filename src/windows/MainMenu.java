package windows;

import javax.swing.*;

public class MainMenu extends JFrame {
    public MainMenu() {
        JFrame jframe = new JFrame("Snake");
        JLabel title = new JLabel("Snake Game", JLabel.CENTER);



        jframe.pack();
        jframe.setSize(1200, 750);
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
    }
}
