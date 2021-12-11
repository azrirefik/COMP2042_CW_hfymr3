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

import BrickDestroy.Cheat.DebugConsole;
import BrickDestroy.GameElement.Ball;
import BrickDestroy.GameElement.Brick;
import BrickDestroy.GameElement.Player;
import BrickDestroy.GameLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;



public class GameBoard extends JComponent implements KeyListener,MouseListener,MouseMotionListener {

    private static final String CONTINUE = "Continue";
    private static final String RESTART = "Restart";
    private static final String EXIT = "Exit";
    private static final String PAUSE = "Pause Menu";
    private static final Color MENU_COLOR = Theme.COL04;


    private static final int DEF_WIDTH = 600;
    private static final int DEF_HEIGHT = 450;

    private static final Color BG_COLOR = Theme.COL00;
    private static final Color INFO_COLOR = Theme.COL06;

    private Timer gameTimer;

    private GameLogic gameLogic;

    private String message;

    private boolean showPauseMenu;

    private Font menuFont;
    private Font guideFont;
    private Font keyFont;

    private Rectangle continueButtonRect;
    private Rectangle exitButtonRect;
    private Rectangle restartButtonRect;
    private int strLen;

    private DebugConsole debugConsole;


    public GameBoard(JFrame owner){
        super();

        strLen = 0;
        showPauseMenu = false;

        menuFont = new Font(Theme.font,Font.PLAIN,30);
        guideFont = new Font(Theme.font,Font.PLAIN,20);
        keyFont = new Font(Theme.font,Font.PLAIN,16);
        this.initialize();
        message = "";
        gameLogic = new GameLogic(new Rectangle(0,0,DEF_WIDTH,DEF_HEIGHT),30,3,6/2,new Point(300,430));

        debugConsole = new DebugConsole(owner, gameLogic,this);
        //initialize the first level
        gameLogic.nextLevel();

        gameTimer = new Timer(10,e ->{
            gameLogic.move();
            gameLogic.findImpacts();

            if(gameLogic.isBallLost()){
                gameTimer.stop();
                if(gameLogic.ballEnd()){
                    gameLogic.wallReset();
                    message = "Game over";
                    gameLogic.saveHighscore();
                    gameLogic.showHighscores();
                    gameLogic.gameReset();
                } else {
                    gameLogic.actorReset();
                    message = currentStats();
                }
            }
            else if(gameLogic.isDone()){
                gameTimer.stop();
                if(gameLogic.hasLevel()){
                    message = "Congrats. Welcome to New Level!";
                    gameLogic.actorReset();
                    gameLogic.wallReset();
                    gameLogic.nextLevel();
                }
                else{
                    message = "ALL LEVELS CLEARED!";
                    gameLogic.saveHighscore();
                    gameLogic.showHighscores();
                    gameLogic.gameReset();
                }
            } else {
                message = currentStats();
            }

            repaint();
        });

        message = "Press SPACE to start";
        repaint();
    }



    private void initialize(){
        this.setPreferredSize(new Dimension(DEF_WIDTH,DEF_HEIGHT));
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }


    public void paint(Graphics g){

        Graphics2D g2d = (Graphics2D) g;


        clear(g2d);

        g2d.setColor(INFO_COLOR);
        g2d.drawString(message,(DEF_WIDTH-(int)g2d.getFontMetrics().getStringBounds(message, g2d).getWidth())/2,225);

        drawBall(gameLogic.ball,g2d);

        for(Brick b : gameLogic.bricks)
            if(!b.isBroken())
                drawBrick(b,g2d);

        drawPlayer(gameLogic.player,g2d);

        if(showPauseMenu)
            drawMenu(g2d);

        Toolkit.getDefaultToolkit().sync();
    }

    private void clear(Graphics2D g2d){
        Color tmp = g2d.getColor();
        g2d.setColor(BG_COLOR);
        g2d.fillRect(0,0,getWidth(),getHeight());
        g2d.setColor(tmp);
    }

    private void drawBrick(Brick brick,Graphics2D g2d){
        Color tmp = g2d.getColor();

        g2d.setColor(brick.getInnerColor());
        g2d.fill(brick.getBrick());

        g2d.setColor(brick.getBorderColor());
        g2d.draw(brick.getBrick());


        g2d.setColor(tmp);
    }

    private void drawBall(Ball ball, Graphics2D g2d){
        Color tmp = g2d.getColor();

        Shape s = ball.getBallFace();

        g2d.setColor(ball.getInnerColor());
        g2d.fill(s);

        g2d.setColor(ball.getBorderColor());
        g2d.draw(s);

        g2d.setColor(tmp);
    }

    private void drawPlayer(Player p, Graphics2D g2d){
        Color tmp = g2d.getColor();

        Shape s = p.getPlayerFace();
        g2d.setColor(Player.INNER_COLOR);
        g2d.fill(s);

        g2d.setColor(Player.BORDER_COLOR);
        g2d.draw(s);

        g2d.setColor(tmp);
    }

    private void drawMenu(Graphics2D g2d){
        obscureGameBoard(g2d);
        drawPauseMenu(g2d);
    }

    private void obscureGameBoard(Graphics2D g2d){

        Composite tmp = g2d.getComposite();
        Color tmpColor = g2d.getColor();

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.85f);
        g2d.setComposite(ac);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0,DEF_WIDTH,DEF_HEIGHT);

        g2d.setComposite(tmp);
        g2d.setColor(tmpColor);
    }

    private void drawPauseMenu(Graphics2D g2d){
        Font tmpFont = g2d.getFont();
        Color tmpColor = g2d.getColor();


        g2d.setFont(menuFont);
        g2d.setColor(MENU_COLOR);

        if(strLen == 0){
            FontRenderContext frc = g2d.getFontRenderContext();
            strLen = menuFont.getStringBounds(PAUSE,frc).getBounds().width;
        }

        int x = (this.getWidth() - strLen) / 2;
        int y = this.getHeight() / 10;

        g2d.drawString(PAUSE,x,y);

        x = this.getWidth() / 8;
        y = this.getHeight() / 4;


        if(continueButtonRect == null){
            FontRenderContext frc = g2d.getFontRenderContext();
            continueButtonRect = menuFont.getStringBounds(CONTINUE,frc).getBounds();
            continueButtonRect.setLocation(x,y-continueButtonRect.height);
        }

        g2d.drawString(CONTINUE,x,y);

        y *= 2;

        if(restartButtonRect == null){
            restartButtonRect = (Rectangle) continueButtonRect.clone();
            restartButtonRect.setLocation(x,y-restartButtonRect.height);
        }

        g2d.drawString(RESTART,x,y);

        y *= 3.0/2;

        if(exitButtonRect == null){
            exitButtonRect = (Rectangle) continueButtonRect.clone();
            exitButtonRect.setLocation(x,y-exitButtonRect.height);
        }

        g2d.drawString(EXIT,x,y);

        drawGuide(g2d);

        g2d.setFont(tmpFont);
        g2d.setColor(tmpColor);
    }

    private void toggleHighscorePanel () {

    }

    /**
     * Draw keybinding information on screen.
     * @param g2d screen
     */
    private void drawGuide(Graphics2D g2d) {
        g2d.setColor(MENU_COLOR);

        g2d.setFont(guideFont);
        FontRenderContext frc = g2d.getFontRenderContext();
        int guideHeight = menuFont.getStringBounds("Key",frc).getBounds().height;

        int x = this.getWidth() - 250;
        // align key and guide horizontally
        int y = this.getHeight()/2 - guideHeight*2;

        g2d.setFont(keyFont);
        g2d.drawString("Esc", x, y);
        g2d.drawString("Space", x, y + guideHeight);
        g2d.drawString("A", x, y + guideHeight*2);
        g2d.drawString("D", x, y + guideHeight*3);

        x += 80;

        g2d.setFont(guideFont);
        g2d.drawString("Toggle menu", x, y);
        g2d.drawString("Pause/Start", x, y + guideHeight);
        g2d.drawString("Move left", x, y + guideHeight*2);
        g2d.drawString("Move right", x, y + guideHeight*3);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch(keyEvent.getKeyCode()){
            case KeyEvent.VK_A:
                gameLogic.player.moveLeft();
                break;
            case KeyEvent.VK_D:
                gameLogic.player.movRight();
                break;
            case KeyEvent.VK_ESCAPE:
                showPauseMenu = !showPauseMenu;
                repaint();
                if (showPauseMenu) {
                    gameTimer.stop();
                }
                break;
            case KeyEvent.VK_SPACE:
                if(!showPauseMenu)
                    if(!gameTimer.isRunning())
                        gameTimer.start();
                    else
                        gameTimer.stop();
                break;
            case KeyEvent.VK_F1:
                if(keyEvent.isAltDown() && keyEvent.isShiftDown())
                    debugConsole.setVisible(true);
            default:
                gameLogic.player.stop();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        gameLogic.player.stop();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(!showPauseMenu)
            return;
        if(continueButtonRect.contains(p)){
            showPauseMenu = false;
            repaint();
        }
        else if(restartButtonRect.contains(p)){
            message = "Restarting Game...";
            gameLogic.actorReset();
            gameLogic.wallReset();
            showPauseMenu = false;
            repaint();
        }
        else if(exitButtonRect.contains(p)){
            System.exit(0);
        }

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

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
        if(exitButtonRect != null && showPauseMenu) {
            if (exitButtonRect.contains(p) || continueButtonRect.contains(p) || restartButtonRect.contains(p))
                this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            else
                this.setCursor(Cursor.getDefaultCursor());
        }
        else{
            this.setCursor(Cursor.getDefaultCursor());
        }
    }

    public void onLostFocus() {
        gameTimer.stop();
        message = "Press START to resume";
        repaint();
    }

    private String currentStats() {
        return String.format("Level:%d  Bricks:%d  Balls:%d  Score:%d", gameLogic.getLevel(), gameLogic.getBrickCount(), gameLogic.getBallCount(), gameLogic.getScore());
    }

}
