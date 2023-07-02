package windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;

public class Score extends JFrame {

    public Score() {
        JFrame jframe = new JFrame("Snake");
        Image frameImage = new ImageIcon("src/images/icon.png").getImage();
        jframe.setIconImage(frameImage);
        jframe.pack();
        jframe.setSize(1200, 750);
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);

        jframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SwingUtilities.invokeLater(MainMenu::new);
            }
        });

        ArrayList<String> stringArrayList = readFile();

        Comparator<String> scoreComparator = new Comparator<>() {
            @Override
            public int compare(String element1, String element2) {
                int score1 = extractScore(element1);
                int score2 = extractScore(element2);
                return Integer.compare(score2, score1);
            }
            private int extractScore(String element) {
                String[] parts = element.split(" ");
                String scoreString = parts[1].trim();
                return Integer.parseInt(scoreString);
            }
        };

        stringArrayList.sort(scoreComparator);

        JList list = new JList(stringArrayList.toArray(new String[0]));
        list.setBackground(Color.BLACK);
        list.setForeground(Color.RED);
        list.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));

        jframe.add(list);
        JScrollPane jScrollPane = new JScrollPane(list);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        jframe.getContentPane().add(jScrollPane);
    }

    public static void writeInFile(String nickname, int score, int length) {
        try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream("src/operations/scores.bin", true))) {
            dataOutputStream.writeInt(length);

            byte[] textBytes = nickname.getBytes(StandardCharsets.UTF_8);
            dataOutputStream.write(textBytes);

            dataOutputStream.writeInt(score);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readFile() {
        ArrayList<String> scores = new ArrayList<>();
        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream("src/operations/scores.bin"))) {
            while (dataInputStream.available() > 0) {
                int length = dataInputStream.readInt();

                byte[] textBytes = new byte[length];
                dataInputStream.readFully(textBytes);
                String text = new String(textBytes, StandardCharsets.UTF_8);

                int score = dataInputStream.readInt();
                scores.add(text + " " + score);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scores;
    }
}
