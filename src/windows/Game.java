package windows;

import operations.TableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import static java.awt.event.KeyEvent.*;

public class Game extends JFrame implements KeyListener {
    private final JFrame jframe = new JFrame("Snake");
    private final TableModel tableModel = new TableModel();
    private final JPanel panelTable = new JPanel(new BorderLayout());
    private static int i = 7;
    private static int j = 12;
    private static int keyPressed;
    private static int score = 0;
    private static int appleI = 7;
    private static int appleJ = 17;
    private static boolean inGame = true;

    public static void setI(int i) {
        Game.i = i;
    }

    public static void setJ(int j) {
        Game.j = j;
    }

    public static void setAppleI(int appleI) {
        Game.appleI = appleI;
    }

    public static void setAppleJ(int appleJ) {
        Game.appleJ = appleJ;
    }

    public Game() {
        Image frameImage = new ImageIcon("src/images/icon.png").getImage();
        jframe.setIconImage(frameImage);
        jframe.pack();
        jframe.setSize(1200, 750);
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        jframe.setResizable(false);

        JTable jTable = new JTable(tableModel);
        jTable.setBackground(Color.BLACK);

        JPanel scorePanel = new JPanel();
        scorePanel.setBackground(Color.BLACK);

        JLabel scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        scoreLabel.setForeground(Color.RED);
        scorePanel.add(scoreLabel, new FlowLayout(FlowLayout.CENTER));


        panelTable.setLayout(new BorderLayout());
        panelTable.add(jTable, BorderLayout.CENTER);

        JPanel parentPanel = new JPanel();
        parentPanel.setLayout(new BorderLayout());
        parentPanel.add(panelTable, BorderLayout.CENTER);
        parentPanel.add(scorePanel, BorderLayout.SOUTH);

        jframe.add(parentPanel, BorderLayout.CENTER);
        parentPanel.setFocusable(true);
        parentPanel.requestFocusInWindow();
        parentPanel.addKeyListener(this);

        new Thread(() -> {
            while (inGame)
                moveSnake();
            System.exit(-100);
        }).start();

        new Thread(() -> {
            while (inGame) {
                scoreLabel.setText("Score: " + score);
                scoreLabel.repaint();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        jTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JLabel) c).setText(null);
                table.setShowGrid(false);
                table.setIntercellSpacing(new Dimension(0, 0));
                JLabel label = new JLabel();

                if (value instanceof ImageIcon) {
                    ImageIcon imageIcon = (ImageIcon) value;

                    label.setIcon(imageIcon);
                } else if (value instanceof JLabel) {
                    JLabel jLabel = (JLabel) value;
                    panelTable.repaint();
                    return jLabel;
                }
                panelTable.repaint();
                return label;
            }
        });

        jTable.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                try {
                    jTable.setRowHeight(jTable.getHeight() / jTable.getRowCount());
                } catch (IllegalArgumentException ex) {
                    System.out.println("So small window");
                }
            }
        });
    }

    public void moveSnakeCoordinates(int iN, int jN) {
        switch (tableModel.getItems()[i + iN][j + jN]) {
            case 0 -> {
                inGame = false;
                return;
            }
            case 3 -> {
                score += 100;
                Random random = new Random();
                int randomI = random.nextInt(14) + 1;
                int randomJ = random.nextInt(23) + 1;
                tableModel.getItems()[randomI][randomJ] = 3;
            }
        }
        tableModel.getItems()[i][j] = 1;
        tableModel.getItems()[i + iN][j + jN] = 4;
        i += iN;
        j += jN;
    }

    public void moveSnake() {
        switch (keyPressed) {
            case KeyEvent.VK_RIGHT -> moveSnakeCoordinates(0, 1);
            case KeyEvent.VK_LEFT -> moveSnakeCoordinates(0, -1);
            case KeyEvent.VK_DOWN -> moveSnakeCoordinates(1, 0);
            case KeyEvent.VK_UP -> moveSnakeCoordinates(-1, 0);
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT -> {
                if (keyPressed != KeyEvent.VK_LEFT)
                    keyPressed = KeyEvent.VK_RIGHT;
            }
            case KeyEvent.VK_LEFT -> {
                if (keyPressed != VK_RIGHT)
                    keyPressed = KeyEvent.VK_LEFT;
            }
            case KeyEvent.VK_DOWN -> {
                if (keyPressed != VK_UP)
                    keyPressed = KeyEvent.VK_DOWN;
            }
            case KeyEvent.VK_UP -> {
                if (keyPressed != VK_DOWN)
                    keyPressed = KeyEvent.VK_UP;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
