package BrickDestroy.GameUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Highscore {
    private final String filename = "src/BrickDestroy/Asset/highscore.dat";
    private final int MAX_HIGHSCORE = 10;
    ArrayList<Integer> scores;

    private JDialog highscorePanel;
    private JLabel[] panelItem;

    public Highscore() {
        loadScores();
        createPanel();
    }

    private void loadScores() {
        scores = new ArrayList<>();
        try (FileReader f = new FileReader(filename)) {
            StringBuffer sb = new StringBuffer();
            while (f.ready()) {
                char c = (char) f.read();
                if (c == '\n') {
                    scores.add(Integer.valueOf(sb.toString()));
                    sb = new StringBuffer();
                } else {
                    sb.append(c);
                }
            }
            if (sb.length() > 0) {
                scores.add(Integer.valueOf(sb.toString())); // case where no trailing \n
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                new File(filename).createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void addScore(int score) {
        scores.add(score);
        sortScores();
        saveScores();
    }

    private void saveScores() {
        try {
            FileWriter writer = new FileWriter(filename);
            int size = scores.size();
            for (Integer score : scores) {
                String str = score.toString();
                writer.write(str);
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sort then remove extra score if more than max
     */
    private void sortScores() {
        Collections.sort(scores);
        Collections.reverse(scores);
        while (scores.size() > MAX_HIGHSCORE) {
            scores.remove(scores.size()-1);
        }
    }

    private void createPanel() {
        highscorePanel = new JDialog();
        highscorePanel.setSize(50, 200);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (size.width - highscorePanel.getWidth()) / 2;
        int y = (size.height - highscorePanel.getHeight()) / 2;
        highscorePanel.setLocation(x,y);
        highscorePanel.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        highscorePanel.setLayout(new GridLayout(11,1));
        highscorePanel.setBackground(Theme.COL00);
        JLabel title = new JLabel("HIGHSCORES", JLabel.CENTER);
        title.setBackground(Theme.COL00);
        title.setForeground(Theme.COL03);
        title.setOpaque(true);
        highscorePanel.add(title);
        panelItem = new JLabel[10];
        for (int i = 0; i < 10; i++) {
            panelItem[i] = new JLabel(" ", JLabel.CENTER );
            panelItem[i].setBackground(Theme.COL00);
            panelItem[i].setForeground(Theme.COL04);
            panelItem[i].setOpaque(true);
            highscorePanel.add(panelItem[i]);
        }
    }

    public void viewPanel() {
        try {
            for (int i = 0; i < 10; i++) {
                if (i >= scores.size()) {
                    panelItem[i].setText(" ");
                } else {
                    panelItem[i].setText(String.valueOf(scores.get(i)));
                }
            }
            highscorePanel.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
