package windows;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    public MainMenu() {
        JFrame jframe = new JFrame("Snake");
        JLabel title = new JLabel("Snake Game", JLabel.CENTER);
        title.setFont(new Font("Comic Sans MS", Font.PLAIN, 70));
        title.setForeground(Color.RED);
        title.setBackground(Color.BLACK);
        title.setOpaque(true);
        jframe.add(title, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridBagLayout());
        buttons.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new java.awt.Insets(10, 20, 10, 20);

        JButton newGame = createButton("New Game");
        JButton exit = createButton("Exit");

        gbc.gridx = 0;
        buttons.add(newGame, gbc);
        gbc.gridx = 1;
        buttons.add(exit, gbc);

        newGame.addActionListener(e -> {
            jframe.dispose();
            SwingUtilities.invokeLater(Game::new);
        });

        exit.addActionListener(e -> {
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to exit?", "Warning", JOptionPane.OK_CANCEL_OPTION, dialogButton);
            if (dialogResult == JOptionPane.YES_OPTION)
                jframe.dispose();
        });

        jframe.add(buttons);
        Image frameImage = new ImageIcon("src/images/icon.png").getImage();
        jframe.setIconImage(frameImage);
        jframe.pack();
        jframe.setSize(1200, 750);
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        jframe.setResizable(false);
    }

    public static JButton createButton(String name) {
        JButton button = new JButton(name);

        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        button.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        button.setForeground(Color.RED);
        button.setBackground(Color.BLACK);

        return button;
    }
}