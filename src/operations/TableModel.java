package operations;

import windows.Game;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TableModel extends AbstractTableModel {
    private final int[][] items;

    public TableModel() {
        this.items = createBoard();
    }

    public int[][] getItems() {
        return items;
    }

    @Override
    public int getRowCount() {
        return items.length;
    }

    @Override
    public int getColumnCount() {
        return items[0].length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (items[rowIndex][columnIndex]) {
            case 0 -> {
                JLabel border = new JLabel();
                border.setBackground(Color.RED);
                border.setBorder(BorderFactory.createLineBorder(Color.RED));
                border.setOpaque(true);
                return border;
            }
            case 3 -> {
                return (new ImageIcon("src/images/apple.png"));
            }
            case 4 -> {
                JLabel snake = new JLabel();
                snake.setBackground(new Color(254, 0, 237));
                snake.setBorder(BorderFactory.createLineBorder(new Color(254, 0, 237)));
                snake.setOpaque(true);
                return snake;
            }
            case 5 -> {
                JLabel tail = new JLabel();
                tail.setBackground(Color.WHITE);
                tail.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                tail.setOpaque(true);
                return tail;
            }
        }

        return null;
    }

    public int[][] createBoard() {
        int[][] matrix = new int[16][25];

        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                if (i == 0 || j == 0 || i == 15 || j == 24)
                    matrix[i][j] = 0;
                else
                    matrix[i][j] = 1;

        matrix[7][12] = 4;
        matrix[7][11] = 5;
        matrix[7][17] = 3;
        Game.setI(7);
        Game.setJ(12);
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(7);
        integers.add(11);
        Game.getTail().add(integers);

        return matrix;
    }
}