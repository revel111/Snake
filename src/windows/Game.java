package windows;

import operations.TableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import static java.awt.event.KeyEvent.*;

public class Game extends JFrame implements KeyListener {
    private final JFrame jframe = new JFrame("Snake");
    private final TableModel tableModel = new TableModel();
    private final JPanel panelTable = new JPanel(new BorderLayout());
    private static int i = 7;
    private static int j = 12;
    private static ArrayList<ArrayList<Integer>> tail = new ArrayList<>();
    private static int keyPressed;
    private static int score = 0;
    private static boolean inGame = true;

    public static void setI(int i) {
        Game.i = i;
    }

    public static void setJ(int j) {
        Game.j = j;
    }

    public static ArrayList<ArrayList<Integer>> getTail() {
        return tail;
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
            String name = JOptionPane.showInputDialog(null, "Enter a nickname.", "Input name", JOptionPane.PLAIN_MESSAGE);

            if (name == null || name.isEmpty())
                JOptionPane.showMessageDialog(null, "You wrote nothing.", "Error", JOptionPane.ERROR_MESSAGE);
            else {
                Score.writeInFile(name, score, name.length());
                SwingUtilities.invokeLater(Score::new);
            }
            reset();
            jframe.dispose();
        }).start();

        new Thread(() -> {
            while (inGame) {
                scoreLabel.setText("Score: " + score);
                scoreLabel.repaint();
                if (score == 32000)
                    inGame = false;
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

                if (value instanceof ImageIcon imageIcon)
                    label.setIcon(imageIcon);
                else if (value instanceof JLabel jLabel) {
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
            case 0, 5 -> {
                inGame = false;
                return;
            }
            case 3 -> {
                score += 100;
                spawnApple();
                ArrayList<Integer> integers = new ArrayList<>();
                switch (keyPressed) {
                    case KeyEvent.VK_RIGHT -> {
                        integers.add(tail.get(tail.size() - 1).get(0));
                        integers.add(tail.get(tail.size() - 1).get(1) - 1);
                    }
                    case KeyEvent.VK_LEFT -> {
                        integers.add(tail.get(tail.size() - 1).get(0));
                        integers.add(tail.get(tail.size() - 1).get(1) + 1);
                    }
                    case KeyEvent.VK_DOWN -> {
                        integers.add(tail.get(tail.size() - 1).get(0) - 1);
                        integers.add(tail.get(tail.size() - 1).get(1));
                    }
                    case KeyEvent.VK_UP -> {
                        integers.add(tail.get(tail.size() - 1).get(0) + 1);
                        integers.add(tail.get(tail.size() - 1).get(1));
                    }
                }
                getTail().add(integers);
            }
        }
        tableModel.getItems()[i + iN][j + jN] = 4;
        tableModel.getItems()[i][j] = 5;
        for (int iT = 0; iT < tail.size(); iT++)
            if (iT == tail.size() - 1)
                tableModel.getItems()[tail.get(iT).get(0)][tail.get(iT).get(1)] = 1;
            else
                tableModel.getItems()[tail.get(iT + 1).get(0)][tail.get(iT + 1).get(1)] = 5;

        for (int iT = tail.size() - 1; iT >= 0; iT--)
            if (iT == 0) {
                tail.get(iT).set(0, i);
                tail.get(iT).set(1, j);
            } else {
                tail.get(iT).set(0, tail.get(iT - 1).get(0));
                tail.get(iT).set(1, tail.get(iT - 1).get(1));
            }
        i += iN;
        j += jN;
    }

    public void spawnApple() {
        Random random = new Random();
        int randomI = random.nextInt(14) + 1;
        int randomJ = random.nextInt(23) + 1;
        if (tableModel.getItems()[randomI][randomJ] != 4 && tableModel.getItems()[randomI][randomJ] != 5)
            tableModel.getItems()[randomI][randomJ] = 3;
        else
            spawnApple();
    }

    public void moveSnake() {
        switch (keyPressed) {
            case KeyEvent.VK_RIGHT -> moveSnakeCoordinates(0, 1);
            case KeyEvent.VK_LEFT -> moveSnakeCoordinates(0, -1);
            case KeyEvent.VK_DOWN -> moveSnakeCoordinates(1, 0);
            case KeyEvent.VK_UP -> moveSnakeCoordinates(-1, 0);
        }
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void reset() {
        keyPressed = 0;
        tail = new ArrayList<>();
        score = 0;
        inGame = true;
        keyPressed = 0;
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