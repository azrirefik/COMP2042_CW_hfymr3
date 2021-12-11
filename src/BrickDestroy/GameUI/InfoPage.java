package BrickDestroy.GameUI;

import javax.swing.*;
import java.awt.*;

public class InfoPage extends JDialog {
    private static String CONTENT_TEXT;

    public InfoPage() {
        CONTENT_TEXT = "<HTML>";
        appendContent("This is a simple arcade video game.");
        appendContent("You will have a rectangular bar as a player.");
        appendContent("It will bounce a ball to hit and break the bricks.");
        appendContent("Your aim is to destroy all the bricks");
        appendContent("The game has very simple commands:");
        appendContent("<br><table border='1'><tr><th>SPACE</th><td>start/pause the game</td></tr>");
        appendContent("<tr><th>A</th><td>move the player right</td></tr>");
        appendContent("<tr><th>D</th><td>move the player right</td></tr>");
        appendContent("<tr><th>ESC</th><td>enter/exit pause menu</td></tr></table>");
        appendContent("The game automatically pause if the frame loses focus");

        this.setSize(700,350);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (size.width - this.getWidth()) / 2;
        int y = (size.height - this.getHeight()) / 2;
        this.setLocation(x,y);
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setLayout(new GridLayout(1,2));
        this.setBackground(Theme.COL00);
        JLabel title = new JLabel("HOW TO PLAY", JLabel.CENTER);
        title.setBackground(Theme.COL00);
        title.setForeground(Theme.COL03);
        title.setOpaque(true);
        this.add(title);
        JLabel content = new JLabel(CONTENT_TEXT, JLabel.CENTER );
        content.setFont(new Font(Theme.font,Font.PLAIN,12));
        content.setBackground(Theme.COL00);
        content.setForeground(Theme.COL04);
        content.setOpaque(true);
        this.add(content);
        this.setVisible(false);
    }

    private void appendContent(String line) {
        CONTENT_TEXT += line;
        CONTENT_TEXT += "<br>";
    }
}
