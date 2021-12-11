/*
 *  Brick Destroy - A simple Arcade video game
 *   Copyright (C) 2017  Filippo Ranza
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package BrickDestroy.GameUI;

import BrickDestroy.GameLogic;
import BrickDestroy.GameUI.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;


public class HomeMenu extends JComponent implements MouseListener, MouseMotionListener {

    private static final String GREETINGS = "Welcome to:";
    private static final String GAME_TITLE = "Brick Destroy";
    private static final String CREDITS = "Version 0.1";
    private static final String START_TEXT = "Start";
    private static final String MENU_TEXT = "Exit";
    private static final String HIGHSCORE_TEXT = "Highscore";
    private static final String INFO_TEXT = "Info";

    private static final Color BG_COLOR = Theme.COLBG;
    private static final Color BORDER_COLOR = Theme.COL00;
    private static final Color DASH_BORDER_COLOR = Theme.COL04;
    private static final Color TEXT_COLOR = Theme.COL03;
    private static final Color CLICKED_BUTTON_COLOR = Theme.COL01;
    private static final Color CLICKED_TEXT = Theme.COL01;
    private static final int BORDER_SIZE = 10;
    private static final float[] DASHES = {10,10};

    private Rectangle menuFace;
    private Rectangle startButton;
    private Rectangle menuButton;
    private Rectangle highscoreButton;
    private Rectangle infoButton;


    private BasicStroke borderStoke;
    private BasicStroke borderStoke_noDashes;

    private Font greetingsFont;
    private Font gameTitleFont;
    private Font creditsFont;
    private Font buttonFont;

    private GameFrame owner;

    private boolean startClicked;
    private boolean menuClicked;
    private boolean highscoreClicked;
    private boolean infoClicked;

    private InfoPage infoPage = new InfoPage();


    public HomeMenu(GameFrame owner,Dimension area){

        this.setFocusable(true);
        this.requestFocusInWindow();

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        this.owner = owner;


        menuFace = new Rectangle(new Point(0,0),area);
        this.setPreferredSize(area);

        Dimension btnDim = new Dimension(area.width / 3, area.height / 12);
        startButton = new Rectangle(btnDim);
        menuButton = new Rectangle(btnDim);
        highscoreButton = new Rectangle(btnDim);
        infoButton = new Rectangle(btnDim);

        borderStoke = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,DASHES,0);
        borderStoke_noDashes = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);

        greetingsFont = new Font(Theme.font,Font.PLAIN,16);
        gameTitleFont = new Font(Theme.font,Font.BOLD,24);
        creditsFont = new Font(Theme.font,Font.PLAIN,16);
        buttonFont = new Font(Theme.font,Font.PLAIN,startButton.height-2);
    }


    public void paint(Graphics g){
        drawMenu((Graphics2D)g);
    }


    public void drawMenu(Graphics2D g2d){

        drawContainer(g2d);

        /*
        all the following method calls need a relative
        painting directly into the HomeMenu rectangle,
        so the translation is made here so the other methods do not do that.
         */
        Color prevColor = g2d.getColor();
        Font prevFont = g2d.getFont();

        double x = menuFace.getX();
        double y = menuFace.getY();

        g2d.translate(x,y);

        //methods calls
        drawText(g2d);
        drawButton(g2d);
        //end of methods calls

        g2d.translate(-x,-y);
        g2d.setFont(prevFont);
        g2d.setColor(prevColor);
    }

    private void drawContainer(Graphics2D g2d){
        Color prev = g2d.getColor();

        g2d.setColor(BG_COLOR);
        g2d.fill(menuFace);

        Stroke tmp = g2d.getStroke();

        g2d.setStroke(borderStoke_noDashes);
        g2d.setColor(DASH_BORDER_COLOR);
        g2d.draw(menuFace);

        g2d.setStroke(borderStoke);
        g2d.setColor(BORDER_COLOR);
        g2d.draw(menuFace);

        g2d.setStroke(tmp);

        g2d.setColor(prev);
    }

    private void drawText(Graphics2D g2d){

        g2d.setColor(TEXT_COLOR);

        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D greetingsRect = greetingsFont.getStringBounds(GREETINGS,frc);
        Rectangle2D gameTitleRect = gameTitleFont.getStringBounds(GAME_TITLE,frc);
        Rectangle2D creditsRect = creditsFont.getStringBounds(CREDITS,frc);

        int sX,sY;

        sX = (int)(menuFace.getWidth() - greetingsRect.getWidth()) / 2;
        sY = (int)(menuFace.getHeight() / 4);

        g2d.setFont(greetingsFont);
        g2d.drawString(GREETINGS,sX,sY);

        sX = (int)(menuFace.getWidth() - gameTitleRect.getWidth()) / 2;
        sY += (int) gameTitleRect.getHeight() * 1.1;//add 10% of String height between the two strings

        g2d.setFont(gameTitleFont);
        g2d.drawString(GAME_TITLE,sX,sY);

        sX = (int)(menuFace.getWidth() - creditsRect.getWidth()) / 2;
        sY += (int) creditsRect.getHeight() * 1.1;

        g2d.setFont(creditsFont);
        g2d.drawString(CREDITS,sX,sY);


    }

    private void drawButton(Graphics2D g2d){

        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D txtRect = buttonFont.getStringBounds(START_TEXT,frc);
        Rectangle2D mTxtRect = buttonFont.getStringBounds(MENU_TEXT,frc);
        Rectangle2D hTxtRect = buttonFont.getStringBounds(HIGHSCORE_TEXT,frc);
        Rectangle2D iTxtRect = buttonFont.getStringBounds(INFO_TEXT,frc);

        g2d.setFont(buttonFont);

        int x = (menuFace.width - startButton.width) / 2;
        int y =(int) ((menuFace.height - startButton.height) * 0.6);

        startButton.setLocation(x,y);

        x = (int)(startButton.getWidth() - txtRect.getWidth()) / 2;
        y = (int)(startButton.getHeight() - txtRect.getHeight()) / 2;

        x += startButton.x;
        y += startButton.y + (startButton.height * 0.9);

        if(startClicked){
            Color tmp = g2d.getColor();
            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(startButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(START_TEXT,x,y);
            g2d.setColor(tmp);
        }
        else{
            g2d.draw(startButton);
            g2d.drawString(START_TEXT,x,y);
        }

        x = startButton.x;
        y = (int) (startButton.y + startButton.height*1.2);

        highscoreButton.setLocation(x,y);

        x = (int)(highscoreButton.getWidth() - hTxtRect.getWidth()) / 2;
        y = (int)(highscoreButton.getHeight() - hTxtRect.getHeight()) / 2;

        x += highscoreButton.x;
        y += highscoreButton.y + (startButton.height * 0.9);

        if(highscoreClicked){
            Color tmp = g2d.getColor();

            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(highscoreButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(HIGHSCORE_TEXT,x,y);
            g2d.setColor(tmp);
        }
        else{
            g2d.draw(highscoreButton);
            g2d.drawString(HIGHSCORE_TEXT,x,y);
        }

        x = highscoreButton.x;
        y = (int) (highscoreButton.y + highscoreButton.height*1.2);

        infoButton.setLocation(x,y);

        x = (int)(infoButton.getWidth() - mTxtRect.getWidth()) / 2;
        y = (int)(infoButton.getHeight() - mTxtRect.getHeight()) / 2;

        x += infoButton.x;
        y += infoButton.y + (highscoreButton.height * 0.9);

        if(infoClicked){
            Color tmp = g2d.getColor();

            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(infoButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(INFO_TEXT,x,y);
            g2d.setColor(tmp);
        }
        else{
            g2d.draw(infoButton);
            g2d.drawString(INFO_TEXT,x,y);
        }

        x = infoButton.x;
        y = (int) (infoButton.y + infoButton.height*1.2);

        menuButton.setLocation(x,y);

        x = (int)(menuButton.getWidth() - mTxtRect.getWidth()) / 2;
        y = (int)(menuButton.getHeight() - mTxtRect.getHeight()) / 2;

        x += menuButton.x;
        y += menuButton.y + (highscoreButton.height * 0.9);

        if(menuClicked){
            Color tmp = g2d.getColor();

            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(menuButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(MENU_TEXT,x,y);
            g2d.setColor(tmp);
        }
        else{
            g2d.draw(menuButton);
            g2d.drawString(MENU_TEXT,x,y);
        }

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(startButton.contains(p)){
           owner.enableGameBoard();

        }
        else if(menuButton.contains(p)){
            System.out.println("Goodbye " + System.getProperty("user.name"));
            System.exit(0);
        } else if (highscoreButton.contains(p)) {
            GameLogic.highscore.viewPanel();
        } else if (infoButton.contains(p)) {
            infoPage.setVisible(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(startButton.contains(p)){
            startClicked = true;
            repaint(startButton.x,startButton.y,startButton.width+1,startButton.height+1);

        } else if(menuButton.contains(p)){
            menuClicked = true;
            repaint(menuButton.x,menuButton.y,menuButton.width+1,menuButton.height+1);
        } else if (highscoreButton.contains(p)) {
            highscoreClicked = true;
            repaint(highscoreButton.x,highscoreButton.y,highscoreButton.width+1,highscoreButton.height+1);
        } else if (infoButton.contains(p)) {
            infoClicked = true;
            repaint(infoButton.x,infoButton.y,infoButton.width+1,infoButton.height+1);
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if(startClicked ){
            startClicked = false;
            repaint(startButton.x,startButton.y,startButton.width+1,startButton.height+1);
        } else if(menuClicked){
            menuClicked = false;
            repaint(menuButton.x,menuButton.y,menuButton.width+1,menuButton.height+1);
        } else if (highscoreClicked) {
            highscoreClicked = false;
            repaint(highscoreButton.x,highscoreButton.y,highscoreButton.width+1,highscoreButton.height+1);
        } else if (infoClicked) {
            infoClicked = false;
            repaint(infoButton.x,infoButton.y,infoButton.width+1,infoButton.height+1);
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }


    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(startButton.contains(p) || menuButton.contains(p) || highscoreButton.contains(p) || infoButton.contains(p))
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        else
            this.setCursor(Cursor.getDefaultCursor());

    }
}
